package com.masterwok.shrimplesearch.features.query.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.masterwok.shrimplesearch.R
import com.masterwok.shrimplesearch.common.contracts.Configurable
import com.masterwok.xamarininterface.models.IndexerQueryResult
import kotlinx.android.synthetic.main.view_indexer_query_result_item.view.*

class QueryResultsAdapter : RecyclerView.Adapter<QueryResultsAdapter.ViewHolder>()
    , Configurable<List<IndexerQueryResult>> {

    private var configuredModel: List<IndexerQueryResult> = emptyList()

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
    )

    override fun getItemCount(): Int = configuredModel.count()

    override fun onBindViewHolder(
        holder: ViewHolder
        , position: Int
    ) = holder.configure(configuredModel[position])

    override fun configure(model: List<IndexerQueryResult>) {
        configuredModel = model

        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        , Configurable<IndexerQueryResult> {

        override fun configure(model: IndexerQueryResult) {
            itemView.textViewIndexerName.text = model
                .indexer
                .displayName

            itemView.textViewMagnetCount.text = model
                .items
                .count { it.linkInfo.magnetUri != null }
                .toString()

            itemView.textViewLinkCount.text = model
                .items
                .count { it.linkInfo.magnetUri == null }
                .toString()
        }
    }
}
