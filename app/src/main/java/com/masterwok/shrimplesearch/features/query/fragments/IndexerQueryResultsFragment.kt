package com.masterwok.shrimplesearch.features.query.fragments

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.masterwok.shrimplesearch.R
import com.masterwok.shrimplesearch.common.utils.DialogUtil
import com.masterwok.shrimplesearch.common.utils.notNull
import com.masterwok.shrimplesearch.di.AppInjector
import com.masterwok.shrimplesearch.features.query.adapters.IndexerQueryResultsAdapter
import com.masterwok.shrimplesearch.features.query.components.SortComponent
import com.masterwok.shrimplesearch.features.query.constants.IndexerQueryResultSortBy
import com.masterwok.shrimplesearch.features.query.constants.OrderBy
import com.masterwok.shrimplesearch.features.query.constants.QueryState
import com.masterwok.shrimplesearch.features.query.viewmodels.QueryViewModel
import com.masterwok.xamarininterface.models.QueryResultItem
import kotlinx.android.synthetic.main.fragment_indexer_query_results.*
import kotlinx.android.synthetic.main.include_toolbar_maneki.*
import javax.inject.Inject


class IndexerQueryResultsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: QueryViewModel by viewModels(this::requireActivity) { viewModelFactory }

    private val queryResultsAdapter = IndexerQueryResultsAdapter { queryResultItem ->
        openQueryResultItem(queryResultItem)
    }

    private fun openQueryResultItem(
        queryResultItem: QueryResultItem
    ) = activity.notNull {
        val linkInfo = queryResultItem.linkInfo
        val uri = linkInfo.magnetUri ?: linkInfo.link ?: return

        try {
            startActivity(Intent(Intent.ACTION_VIEW, uri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            })
        } catch (exception: ActivityNotFoundException) {
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        AppInjector
            .queryComponent
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.fragment_indexer_query_results
        , container
        , false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initNavigation()
        initRecyclerView()
        subscribeToLiveData()
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
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    private fun initToolbar() {
        toolbar.apply {
            inflateMenu(R.menu.menu_fragment_query)
            setOnMenuItemClickListener(this@IndexerQueryResultsFragment::onOptionsItemSelected)
        }
    }

    private fun initRecyclerView() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = queryResultsAdapter
        }

        viewModel.liveDataQueryState.observe(viewLifecycleOwner, ::onQueryStateChange)
    }

    private fun subscribeToLiveData() {
        viewModel.liveDataSelectedIndexerQueryResultItem.observe(
            viewLifecycleOwner
            , ::configure
        )
    }

    private fun onQueryStateChange(queryState: QueryState?) {
        progressBar.isVisible = when (queryState) {
            QueryState.Pending -> true
            else -> false
        }
    }

    private fun configure(queryResultItems: List<QueryResultItem>) {
        recyclerView.scrollToPosition(0)

        queryResultsAdapter.configure(queryResultItems)
    }

    private fun presentSortDialog() = context.notNull { context ->
        val sortBy = checkNotNull(viewModel.liveDataSortIndexerQueryResults.value).first
        val orderBy = checkNotNull(viewModel.liveDataSortIndexerQueryResults.value).second

        DialogUtil.presentSortDialog(
            context,
            SortComponent.Model(
                IndexerQueryResultSortBy
                    .values()
                    .map { SortComponent.Pill(it.id, it::getDisplayValue) },
                OrderBy
                    .values()
                    .map { SortComponent.Pill(it.id, it::getDisplayValue) },
                SortComponent.Pill(sortBy.id, sortBy::getDisplayValue),
                SortComponent.Pill(orderBy.id, orderBy::getDisplayValue)
            )
        ) { sortModel ->
            viewModel.setSortQueryResultItems(
                IndexerQueryResultSortBy.getByValue(sortModel.selectedSortPill.id),
                OrderBy.getByValue(sortModel.selectedOrderPill.id)
            )
        }
    }


}
