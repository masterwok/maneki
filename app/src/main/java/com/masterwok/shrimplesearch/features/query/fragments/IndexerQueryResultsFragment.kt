package com.masterwok.shrimplesearch.features.query.fragments

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog

import com.masterwok.shrimplesearch.R
import com.masterwok.shrimplesearch.common.constants.AnalyticEvent
import com.masterwok.shrimplesearch.common.data.services.contracts.AnalyticService
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
import javax.inject.Inject


class IndexerQueryResultsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var analyticService: AnalyticService

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
            analyticService.logException(exception, "No activity found to open query result item.")
            presentNoTorrentClientFoundDialog()
        }
    }

    private fun presentNoTorrentClientFoundDialog() = context.notNull { context ->
        MaterialDialog(context).show {
            title(res = R.string.dialog_header_whoops)
            message(res = R.string.dialog_no_torrent_client_found)
            positiveButton {
                title(res = R.string.button_ok)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        AppInjector
            .queryComponent
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.fragment_indexer_query_results, container, false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        subscribeToLiveData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_sort, menu)
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

        viewModel.liveDataQueryState.observe(viewLifecycleOwner, ::onQueryStateChange)
    }

    private fun subscribeToLiveData() {
        viewModel.liveDataSelectedIndexerQueryResultItem.observe(
            viewLifecycleOwner, ::configure
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

        linearLayoutNoResultsHint.isVisible = queryResultItems.count() == 0
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
