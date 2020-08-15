package com.masterwok.shrimplesearch.features.query.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.masterwok.shrimplesearch.R
import com.masterwok.shrimplesearch.common.contracts.Configurable
import com.masterwok.shrimplesearch.common.extensions.getCurrentLocale
import com.masterwok.shrimplesearch.common.extensions.getLocaleNumberFormat
import com.masterwok.shrimplesearch.common.extensions.toHumanReadableByteCount
import com.masterwok.xamarininterface.models.QueryResultItem
import kotlinx.android.synthetic.main.view_indexer_query_result_item.view.*
import java.text.DateFormat

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
            val context = itemView.context
            val statInfo = model.statInfo
            val stringNotAvailable = context.getString(R.string.stat_info_not_available)
            val numberFormat = context.getLocaleNumberFormat()
            val dateFormat = DateFormat.getDateInstance(
                DateFormat.SHORT
                , context.getCurrentLocale()
            )

            itemView.textViewTitle.text = model.title

            itemView.textViewSeeders.text = statInfo
                .seeders
                ?.let(numberFormat::format)
                ?: stringNotAvailable

            itemView.textViewLeechers.text = statInfo
                .peers
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

        }
    }
}
