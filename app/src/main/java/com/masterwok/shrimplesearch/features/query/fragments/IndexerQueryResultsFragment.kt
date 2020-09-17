package com.masterwok.shrimplesearch.features.query.fragments

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.list.customListAdapter
import com.google.android.material.snackbar.Snackbar
import com.masterwok.shrimplesearch.R
import com.masterwok.shrimplesearch.common.constants.AnalyticEvent
import com.masterwok.shrimplesearch.common.data.models.UserSettings
import com.masterwok.shrimplesearch.common.data.services.contracts.AnalyticService
import com.masterwok.shrimplesearch.common.extensions.copyToClipboard
import com.masterwok.shrimplesearch.common.extensions.getColorByAttribute
import com.masterwok.shrimplesearch.common.extensions.showSnackbar
import com.masterwok.shrimplesearch.common.utils.DialogUtil
import com.masterwok.shrimplesearch.common.utils.notNull
import com.masterwok.shrimplesearch.di.AppInjector
import com.masterwok.shrimplesearch.features.query.adapters.IndexerQueryResultsAdapter
import com.masterwok.shrimplesearch.features.query.adapters.MaterialDialogIconListItemAdapter
import com.masterwok.shrimplesearch.features.query.components.SortComponent
import com.masterwok.shrimplesearch.features.query.constants.IndexerQueryResultSortBy
import com.masterwok.shrimplesearch.features.query.constants.OrderBy
import com.masterwok.shrimplesearch.features.query.constants.QueryState
import com.masterwok.shrimplesearch.features.query.viewmodels.QueryViewModel
import com.masterwok.xamarininterface.models.QueryResultItem
import kotlinx.android.synthetic.main.fragment_indexer_query_results.*
import kotlinx.android.synthetic.main.fragment_indexer_query_results.progressBar
import kotlinx.android.synthetic.main.fragment_indexer_query_results.recyclerView
import kotlinx.android.synthetic.main.fragment_query.*
import javax.inject.Inject


class IndexerQueryResultsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var analyticService: AnalyticService

    private lateinit var linearLayoutManager: LinearLayoutManager

    private val viewModel: QueryViewModel by viewModels(this::requireActivity) { viewModelFactory }

    private val queryResultsAdapter = IndexerQueryResultsAdapter { queryResultItem ->
        presentBottomSheet(queryResultItem)
    }

    private val userSettings: UserSettings get() = viewModel.getUserSettings()

    private var snackbarNewResults: Snackbar? = null

    private fun openQueryResultItem(queryResultItem: QueryResultItem) = activity.notNull {
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
        subscribeToViewComponents()
        subscribeToLiveData()
    }

    private fun subscribeToViewComponents() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    snackbarNewResults?.dismiss()
                    snackbarNewResults = null
                }
            }
        })
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
        linearLayoutManager = LinearLayoutManager(context)

        recyclerView.apply {
            layoutManager = linearLayoutManager
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
        queryResultsAdapter.configure(queryResultItems)

        linearLayoutNoResultsHint.isVisible = queryResultItems.count() == 0

        if (
            userSettings.isScrollToTopNotificationsEnabled
            && snackbarNewResults == null
            && linearLayoutManager.findFirstCompletelyVisibleItemPosition() > 0
        ) {
            presentNewResultsSnack()
        }
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

            recyclerView.scrollToPosition(0)
        }
    }

    private fun presentNewResultsSnack() = context.notNull { context ->
        snackbarNewResults = coordinatorLayoutIndexerQuery.showSnackbar(
            message = context.getString(R.string.snack_new_query_results),
            length = Snackbar.LENGTH_INDEFINITE,
            actionMessage = context.getString(R.string.snack_scroll_to_top),
            backgroundColor = context.getColorByAttribute(R.attr.color_snack_background),
            textColor = context.getColorByAttribute(R.attr.color_snack_text)
        ) {
            recyclerView.scrollToPosition(0)
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

    private fun shareQueryResultItem(queryResultItem: QueryResultItem) = activity.notNull {
        val uri = (queryResultItem.linkInfo.magnetUri ?: queryResultItem.linkInfo.link)
            ?: return@notNull

        ShareCompat
            .IntentBuilder
            .from(it)
            .setType("text/plain")
            .setText(uri.toString())
            .startChooser()

    }

    private fun copyQueryResultItem(queryResultItem: QueryResultItem) =
        context?.notNull { context ->
            val uri = (queryResultItem.linkInfo.magnetUri ?: queryResultItem.linkInfo.link)
                ?: return@notNull

            context.copyToClipboard(CLIPBOARD_LABEL, uri.toString())
        }

    private fun presentBottomSheet(queryResultItem: QueryResultItem) = context.notNull { context ->
        val hasMagnetUri = queryResultItem
            .linkInfo
            .magnetUri != null

        MaterialDialog(context, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            customListAdapter(MaterialDialogIconListItemAdapter().apply {
                val items = listOf(
                    MaterialDialogIconListItemAdapter.Item(
                        R.drawable.ic_baseline_share_24,
                        if (hasMagnetUri) R.string.share_magnet else R.string.share_link
                    ) {
                        shareQueryResultItem(queryResultItem)
                        dismiss()
                    },
                    MaterialDialogIconListItemAdapter.Item(
                        R.drawable.ic_content_copy_black_24dp,
                        if (hasMagnetUri) R.string.copy_magnet else R.string.copy_torrent
                    ) {
                        copyQueryResultItem(queryResultItem)
                        dismiss()
                    },
                    MaterialDialogIconListItemAdapter.Item(
                        R.drawable.ic_baseline_open_in_new_24,
                        if (hasMagnetUri) R.string.open_magnet else R.string.open_link
                    ) {
                        openQueryResultItem(queryResultItem)
                        dismiss()
                    }
                )

                configure(items)
            })
        }
    }

    companion object {
        private const val CLIPBOARD_LABEL = "clipboard.uri"
    }

}
