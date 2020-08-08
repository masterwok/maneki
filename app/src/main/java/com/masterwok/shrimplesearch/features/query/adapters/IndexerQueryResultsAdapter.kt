package com.masterwok.shrimplesearch.features.query.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.masterwok.shrimplesearch.R
import com.masterwok.shrimplesearch.common.contracts.Configurable
import com.masterwok.shrimplesearch.common.extensions.getLocaleNumberFormat
import com.masterwok.xamarininterface.models.QueryResultItem

class IndexerQueryResultsAdapter(
    private val onQueryResultClicked: (QueryResultItem) -> Unit
) : RecyclerView.Adapter<IndexerQueryResultsAdapter.ViewHolder>()
    , Configurable<List<QueryResultItem>> {

    private var configuredModel: List<QueryResultItem> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup
        , viewType: Int
    ): ViewHolder = ViewHolder(
        LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.view_indexer_query_result_item
                , parent
                , false
            )
        , onQueryResultClicked
    )

    override fun getItemCount(): Int = configuredModel.count()

    override fun onBindViewHolder(
        holder: ViewHolder
        , position: Int
    ) = holder.configure(configuredModel[position])

    override fun configure(model: List<QueryResultItem>) {
        configuredModel = model

        notifyDataSetChanged()
    }

    class ViewHolder(
        itemView: View
        , private val onQueryResultItemClicked: (QueryResultItem) -> Unit
    ) : RecyclerView.ViewHolder(itemView)
        , Configurable<QueryResultItem> {

        override fun configure(model: QueryResultItem) {
            val numberFormat = itemView.context.getLocaleNumberFormat()

//            itemView.textViewIndexerName.text = model.indexer.displayName
//            itemView.textViewMagnetCount.text = numberFormat.format(model.magnetCount)
//            itemView.textViewLinkCount.text = numberFormat.format(model.linkCount)

//            itemView.setOnClickListener { onQueryResultClicked(model) }
        }
    }
}
