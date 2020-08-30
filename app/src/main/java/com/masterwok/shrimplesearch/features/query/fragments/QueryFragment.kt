package com.masterwok.shrimplesearch.features.query.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.masterwok.shrimplesearch.R
import com.masterwok.shrimplesearch.common.extensions.hideSoftKeyboard
import com.masterwok.shrimplesearch.common.utils.notNull
import com.masterwok.shrimplesearch.di.AppInjector
import com.masterwok.shrimplesearch.features.query.adapters.QueryResultsAdapter
import com.masterwok.shrimplesearch.features.query.components.SortByComponent
import com.masterwok.shrimplesearch.features.query.enums.QueryState
import com.masterwok.shrimplesearch.features.query.viewmodels.QueryViewModel
import com.masterwok.xamarininterface.enums.IndexerQueryState
import com.masterwok.xamarininterface.enums.IndexerType
import com.masterwok.xamarininterface.models.Indexer
import com.masterwok.xamarininterface.models.IndexerQueryResult
import com.masterwok.xamarininterface.models.Query
import kotlinx.android.synthetic.main.fragment_query.*
import kotlinx.android.synthetic.main.include_toolbar_maneki.*
import kotlinx.android.synthetic.main.include_toolbar_query.*
import javax.inject.Inject
import javax.inject.Named


class QueryFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    @Named("aggregate_indexer_id")
    lateinit var aggregateIndexerId: String

    private val viewModel: QueryViewModel by viewModels(this::requireActivity) { viewModelFactory }

    private val queryResultsAdapter = QueryResultsAdapter {
        viewModel.setSelectedIndexer(it.indexer)

        findNavController().navigate(R.id.action_queryFragment_to_indexerQueryResultsFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(
        R.layout.fragment_query, container, false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initNavigation()
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

    private fun initToolbar() {
        toolbar.apply {
            inflateMenu(R.menu.menu_fragment_query)
            setOnMenuItemClickListener(this@QueryFragment::onOptionsItemSelected)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_item_sort) {
            presentSortDialog()

            return true
        }

        return false
    }

    private fun initNavigation() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        toolbar.setupWithNavController(navController, appBarConfiguration)
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
        viewModel.liveDataIndexerQueryResults.observe(
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
        val sortedQueryResults = sortQueryResults(queryResults)

        queryResultsAdapter.configure(listOf(aggregateIndexerQueryResult) + sortedQueryResults)
    }

    private fun sortQueryResults(
        queryResults: List<IndexerQueryResult>
    ): List<IndexerQueryResult> = queryResults.sortedByDescending { it.magnetCount }

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
        val sortPillLeechers = SortByComponent.Pill(R.string.component_sort_query_results_leechers)

        val sortPills = listOf(
            SortByComponent.Pill(R.string.component_sort_query_results_name),
            SortByComponent.Pill(R.string.component_sort_query_results_peers),
            sortPillLeechers,
            SortByComponent.Pill(R.string.component_sort_query_results_size),
            SortByComponent.Pill(R.string.component_sort_query_results_published_on)
        )

        val orderPillDescending =
            SortByComponent.Pill(R.string.component_sort_query_results_descending)

        val orderPills = listOf(
            SortByComponent.Pill(R.string.component_sort_query_results_ascending),
            orderPillDescending
        )

        val sortComponent = SortByComponent(context).apply {
            configure(
                SortByComponent.Model(
                    sortPills,
                    orderPills,
                    sortPillLeechers,
                    orderPillDescending
                )
            )
        }

        MaterialDialog(context).show {
            customView(view = sortComponent)
            cornerRadius(16f)
            positiveButton {
                title(res = R.string.button_done)
            }
            negativeButton {
                title(res = R.string.button_cancel)
            }
        }
    }

}

