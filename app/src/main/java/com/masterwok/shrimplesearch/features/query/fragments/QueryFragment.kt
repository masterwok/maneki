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
import com.masterwok.shrimplesearch.R
import com.masterwok.shrimplesearch.common.constants.AnalyticEvent
import com.masterwok.shrimplesearch.common.data.services.contracts.AnalyticService
import com.masterwok.shrimplesearch.common.extensions.hideSoftKeyboard
import com.masterwok.shrimplesearch.common.utils.DialogUtil
import com.masterwok.shrimplesearch.common.utils.notNull
import com.masterwok.shrimplesearch.di.AppInjector
import com.masterwok.shrimplesearch.features.query.adapters.QueryResultsAdapter
import com.masterwok.shrimplesearch.features.query.components.SortComponent
import com.masterwok.shrimplesearch.features.query.constants.OrderBy
import com.masterwok.shrimplesearch.features.query.constants.QueryResultSortBy
import com.masterwok.shrimplesearch.features.query.constants.QueryState
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
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    @Named("aggregate_indexer_id")
    lateinit var aggregateIndexerId: String

    @Inject
    lateinit var analyticService: AnalyticService

    private val viewModel: QueryViewModel by viewModels(this::requireActivity) { viewModelFactory }

    private val queryResultsAdapter = QueryResultsAdapter {
        viewModel.setSelectedIndexer(it.indexer)

        findNavController().navigate(R.id.action_queryFragment_to_indexerQueryResultsFragment)
    }

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
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = queryResultsAdapter
        }
    }

    private fun subscribeToViewComponents() {
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
    }

    private fun subscribeToLiveData() {
        viewModel.liveDataQueryResults.observe(
            viewLifecycleOwner,
            this::onIndexerQueryResultsChange
        )

        viewModel.liveDataQueryCompleted.observe(viewLifecycleOwner) {
            onQueryCompleted()
        }

        viewModel.liveDataQueryState.observe(viewLifecycleOwner, ::onQueryStateChange)
    }

    private fun onQueryStateChange(queryState: QueryState?) {
        linearLayoutQueryHint.isVisible = when (queryState) {
            null -> true
            else -> false
        }

        progressBar.isVisible = when (queryState) {
            QueryState.Pending -> true
            else -> false
        }
    }

    private fun onQueryCompleted() {
        progressBar.isVisible = false
    }

    private fun onIndexerQueryResultsChange(queryResults: List<IndexerQueryResult>) {
        if (queryResults.count() == 0) {
            queryResultsAdapter.configure(queryResults)
            return
        }

        val aggregateIndexerQueryResult = createAggregateIndexerQueryResult(queryResults)

        queryResultsAdapter.configure(listOf(aggregateIndexerQueryResult) + queryResults)
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
        }
    }


}

