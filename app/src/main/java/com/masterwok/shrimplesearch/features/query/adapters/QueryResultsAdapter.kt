package com.masterwok.shrimplesearch.features.query.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.masterwok.shrimplesearch.R
import com.masterwok.shrimplesearch.common.contracts.Configurable
import com.masterwok.shrimplesearch.common.extensions.getLocaleNumberFormat
import com.masterwok.xamarininterface.models.IndexerQueryResult
import kotlinx.android.synthetic.main.view_query_result_item.view.*

class QueryResultsAdapter(
    private val onQueryResultClicked: (IndexerQueryResult) -> Unit
) : RecyclerView.Adapter<QueryResultsAdapter.ViewHolder>(),
    Configurable<List<IndexerQueryResult>> {

    private var configuredModel: List<IndexerQueryResult> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder = ViewHolder(
        LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.view_query_result_item, parent, false
            ), onQueryResultClicked
    )

    override fun getItemCount(): Int = configuredModel.count()

    override fun onBindViewHolder(
        holder: ViewHolder, position: Int
    ) = holder.configure(configuredModel[position])

    override fun configure(model: List<IndexerQueryResult>) {
        val diffCallback = IndexerQueryResultsDiffCallback(configuredModel, model)

        configuredModel = model

        DiffUtil.calculateDiff(diffCallback).also {
            it.dispatchUpdatesTo(this)
        }
    }

    class ViewHolder(
        itemView: View, private val onQueryResultClicked: (IndexerQueryResult) -> Unit
    ) : RecyclerView.ViewHolder(itemView), Configurable<IndexerQueryResult> {

        override fun configure(model: IndexerQueryResult) {
            val numberFormat = itemView.context.getLocaleNumberFormat()

            itemView.textViewIndexerName.text = model.indexer.displayName
            itemView.textViewMagnetCount.text = numberFormat.format(model.magnetCount)
            itemView.textViewLinkCount.text = numberFormat.format(model.linkCount)

            itemView.setOnClickListener { onQueryResultClicked(model) }
        }
    }
}

private class IndexerQueryResultsDiffCallback(
    val old: List<IndexerQueryResult>,
    val new: List<IndexerQueryResult>
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
