package com.masterwok.shrimplesearch.features.query.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.masterwok.shrimplesearch.R
import com.masterwok.shrimplesearch.common.constants.AnalyticEvent
import com.masterwok.shrimplesearch.common.data.models.UserSettings
import com.masterwok.shrimplesearch.common.data.services.contracts.AnalyticService
import com.masterwok.shrimplesearch.common.extensions.*
import com.masterwok.shrimplesearch.common.utils.DialogUtil
import com.masterwok.shrimplesearch.common.utils.notNull
import com.masterwok.shrimplesearch.di.AppInjector
import com.masterwok.shrimplesearch.features.query.adapters.QueryResultsAdapter
import com.masterwok.shrimplesearch.features.query.components.SortComponent
import com.masterwok.shrimplesearch.features.query.constants.OrderBy
import com.masterwok.shrimplesearch.features.query.constants.QueryResultSortBy
import com.masterwok.xamarininterface.enums.QueryState
import com.masterwok.shrimplesearch.features.query.viewmodels.QueryViewModel
import com.masterwok.xamarininterface.enums.IndexerQueryState
import com.masterwok.xamarininterface.enums.IndexerType
import com.masterwok.xamarininterface.models.Indexer
import com.masterwok.xamarininterface.models.IndexerQueryResult
import com.masterwok.xamarininterface.models.Query
import kotlinx.android.synthetic.main.fragment_query.*
import kotlinx.android.synthetic.main.include_toolbar_query.*
import javax.inject.Inject
import javax.inject.Named


class QueryFragment : Fragment() {

    @Inject
    @Named("aggregate_indexer_id")
    lateinit var aggregateIndexerId: String

    @Inject
    lateinit var analyticService: AnalyticService

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: QueryViewModel by viewModels(this::requireActivity) { viewModelFactory }

    private lateinit var linearLayoutManager: LinearLayoutManager

    private val queryResultsAdapter = QueryResultsAdapter {
        viewModel.setSelectedIndexer(it.indexer)

        findNavController().apply {
            // Guard against navigation crash: https://stackoverflow.com/a/53737537/563509
            if (this.currentDestination?.id == R.id.queryFragment) {
                navigate(R.id.action_queryFragment_to_indexerQueryResultsFragment)
            }
        }
    }

    private val userSettings: UserSettings get() = viewModel.getUserSettings()

    private var isToolbarScrollEnabled: Boolean = false
        set(value) {
            field = value

            if (value) {
                appBarLayoutQuery.enableScroll(
                    toolbarQuery, AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                            or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                            or AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
                )
            } else {
                appBarLayoutQuery.disableScroll(toolbarQuery)
            }
        }

    private var snackbarNewResults: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_sort, menu)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(
        R.layout.fragment_query, container, false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        subscribeToViewComponents()
        subscribeToLiveData()

        isToolbarScrollEnabled = false
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        AppInjector
            .queryComponent
            .inject(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_item_sort) {
            analyticService.logEvent(AnalyticEvent.MenuItemSortTapped)
            presentSortDialog()

            return true
        }

        return false
    }

    private fun initRecyclerView() {
        linearLayoutManager = LinearLayoutManager(context)

        recyclerView.apply {
            layoutManager = linearLayoutManager
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = queryResultsAdapter
        }
    }

    override fun onResume() {
        super.onResume()

        analyticService.logScreen(QueryFragment::class.java)
    }

    private fun presentQueryCancelledSnack() = context.notNull { context ->
        coordinatorLayoutQuery.showSnackbar(
            context.getString(R.string.snack_query_cancelled),
            Snackbar.LENGTH_SHORT,
            backgroundColor = context.getColorByAttribute(
                R.attr.color_snack_query_cancelled_background
            ),
            textColor = context.getColorByAttribute(
                R.attr.color_snack_query_cancelled_text
            )
        )
    }

    private fun subscribeToViewComponents() {
        autoCompleteTextViewSearch.setOnTextClearedListener {
            if (viewModel.liveDataQueryState.value == QueryState.Pending) {
                viewModel.cancelQuery()
                presentQueryCancelledSnack()
            }
        }

        autoCompleteTextViewSearch.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.setQuery(Query(textView.text.toString()))
                queryResultsAdapter.configure(emptyList())
                activity?.hideSoftKeyboard()
                progressBar.isVisible = true
                linearLayoutQueryHint.isVisible = false
                true
            } else {
                false
            }
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    snackbarNewResults?.dismiss()
                    snackbarNewResults = null
                }
            }
        })
    }

    private fun subscribeToLiveData() {
        viewModel.liveDataQueryResults.observe(
            viewLifecycleOwner,
            this::onIndexerQueryResultsChange
        )

        viewModel.liveDataQueryState.observe(viewLifecycleOwner, ::onQueryStateChange)
    }

    private fun onQueryStateChange(queryState: QueryState?) {
        val resultCount = viewModel
            .liveDataQueryResults
            .value
            ?.count()
            ?: 0

        linearLayoutQueryHint.isVisible = when (queryState) {
            QueryState.Pending -> false
            QueryState.Completed -> false
            QueryState.Aborted -> resultCount == 0
            null -> true
        }

        progressBar.isVisible = when (queryState) {
            QueryState.Pending -> true
            else -> false
        }
    }

    private fun onIndexerQueryResultsChange(queryResults: List<IndexerQueryResult>) {
        if (queryResults.count() == 0) {
            queryResultsAdapter.configure(queryResults)
            isToolbarScrollEnabled = false
            return
        }

        // Don't set scroll if already enabled, doing so results in toolbar expanding.
        if (!isToolbarScrollEnabled) {
            isToolbarScrollEnabled = true
        }

        val aggregateIndexerQueryResult = createAggregateIndexerQueryResult(queryResults)

        queryResultsAdapter.configure(listOf(aggregateIndexerQueryResult) + queryResults)

        if (
            userSettings.isScrollToTopNotificationsEnabled
            && snackbarNewResults == null
            && linearLayoutManager.findFirstCompletelyVisibleItemPosition() > 0
        ) {
            presentNewResultsSnack()
        }
    }

    private fun presentNewResultsSnack() = context.notNull { context ->
        snackbarNewResults = coordinatorLayoutQuery.showSnackbar(
            message = context.getString(R.string.snack_new_query_results),
            length = Snackbar.LENGTH_INDEFINITE,
            actionMessage = context.getString(R.string.snack_scroll_to_top),
            backgroundColor = context.getColorByAttribute(R.attr.color_snack_background),
            textColor = context.getColorByAttribute(R.attr.color_snack_text)
        ) {
            recyclerView.scrollToPosition(0)
            appBarLayoutQuery.setExpanded(true, true)
        }.apply {
            addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    if (event == DISMISS_EVENT_ACTION) {
                        snackbarNewResults = null
                    }
                }
            })
        }
    }

    private fun createAggregateIndexerQueryResult(
        results: Collection<IndexerQueryResult>
    ): IndexerQueryResult = IndexerQueryResult(
        Indexer(
            id = aggregateIndexerId,
            type = IndexerType.Aggregate,
            displayName = getString(R.string.indexer_aggregate_display_name),
            displayDescription = null
        ),
        results.flatMap { it.items },
        queryState = IndexerQueryState.Success,
        failureReason = null,
        magnetCount = results.sumBy { it.magnetCount },
        linkCount = results.sumBy { it.linkCount }
    )

    private fun presentSortDialog() = context.notNull { context ->
        val sortBy = checkNotNull(viewModel.liveDataSortQueryResults.value).first
        val orderBy = checkNotNull(viewModel.liveDataSortQueryResults.value).second

        DialogUtil.presentSortDialog(
            context,
            SortComponent.Model(
                QueryResultSortBy
                    .values()
                    .map { SortComponent.Pill(it.id, it::getDisplayValue) },
                OrderBy
                    .values()
                    .map { SortComponent.Pill(it.id, it::getDisplayValue) },
                SortComponent.Pill(sortBy.id, sortBy::getDisplayValue),
                SortComponent.Pill(orderBy.id, orderBy::getDisplayValue)
            )
        ) { sortModel ->
            viewModel.setSortQueryResults(
                QueryResultSortBy.getByValue(sortModel.selectedSortPill.id),
                OrderBy.getByValue(sortModel.selectedOrderPill.id)
            )

            recyclerView.scrollToPosition(0)
        }
    }


}

