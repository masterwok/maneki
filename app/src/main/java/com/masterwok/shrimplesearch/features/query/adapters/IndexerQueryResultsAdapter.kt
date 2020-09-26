package com.masterwok.shrimplesearch.features.query.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.masterwok.shrimplesearch.R
import com.masterwok.shrimplesearch.common.contracts.Configurable
import com.masterwok.shrimplesearch.common.extensions.getCurrentLocale
import com.masterwok.shrimplesearch.common.extensions.getLocaleNumberFormat
import com.masterwok.shrimplesearch.common.extensions.onClicked
import com.masterwok.shrimplesearch.common.extensions.toHumanReadableByteCount
import com.masterwok.xamarininterface.models.QueryResultItem
import kotlinx.android.synthetic.main.view_indexer_query_result_item.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.DateFormat

class IndexerQueryResultsAdapter(
    private val onQueryResultItemClicked: (QueryResultItem) -> Unit
) : RecyclerView.Adapter<IndexerQueryResultsAdapter.ViewHolder>(),
    Configurable<List<QueryResultItem>> {

    private var configuredModel: List<QueryResultItem> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder = ViewHolder(
        LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.view_indexer_query_result_item, parent, false
            ), onQueryResultItemClicked
    )

    override fun getItemCount(): Int = configuredModel.count()

    override fun onBindViewHolder(
        holder: ViewHolder, position: Int
    ) = holder.configure(configuredModel[position])

    override fun configure(model: List<QueryResultItem>) {
        val diffCallback = QueryResultItemsDiffCallback(configuredModel, model)

        configuredModel = model

        DiffUtil.calculateDiff(diffCallback).also {
            it.dispatchUpdatesTo(this)
        }
    }

    class ViewHolder(
        itemView: View, private val onQueryResultItemClicked: (QueryResultItem) -> Unit
    ) : RecyclerView.ViewHolder(itemView), Configurable<QueryResultItem> {

        private val scope = CoroutineScope(Job() + Dispatchers.Main)

        @FlowPreview
        @ExperimentalCoroutinesApi
        override fun configure(model: QueryResultItem) {
            val context = itemView.context
            val statInfo = model.statInfo
            val stringNotAvailable = context.getString(R.string.stat_info_not_available)
            val numberFormat = context.getLocaleNumberFormat()
            val dateFormat = DateFormat.getDateInstance(
                DateFormat.SHORT, context.getCurrentLocale()
            )

            itemView.textViewTitle.text = model.title

            val seeders = statInfo.seeders
            val peers = statInfo.peers

            val leechers = if (seeders != null && peers != null) {
                peers - seeders
            } else {
                null
            }

            itemView.textViewSeeders.text = seeders
                ?.let(numberFormat::format)
                ?: stringNotAvailable

            itemView.textViewLeechers.text = leechers
                ?.let(numberFormat::format)
                ?: stringNotAvailable

            itemView.textViewFileCount.text = statInfo
                .files
                ?.let(numberFormat::format)
                ?: stringNotAvailable

            itemView.textViewPublishedOn.text = statInfo
                .publishedOn
                ?.let(dateFormat::format)
                ?: stringNotAvailable

            itemView.textViewSize.text = statInfo
                .size
                ?.toHumanReadableByteCount(true)
                ?: stringNotAvailable

            itemView.imageViewMagnet.isVisible = model.linkInfo.magnetUri != null

            itemView.setOnClickListener { onQueryResultItemClicked(model) }

            itemView
                .onClicked()
                .debounce(BUTTON_DEBOUNCE_MS)
                .onEach { onQueryResultItemClicked(model) }
                .launchIn(scope)
        }

        private companion object {
            private const val BUTTON_DEBOUNCE_MS = 250L
        }
    }
}

private class QueryResultItemsDiffCallback(
    val old: List<QueryResultItem>,
    val new: List<QueryResultItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = old.count()

    override fun getNewListSize(): Int = new.count()

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean = old[oldItemPosition] == new[newItemPosition]

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean = old[oldItemPosition] == new[newItemPosition]

}
