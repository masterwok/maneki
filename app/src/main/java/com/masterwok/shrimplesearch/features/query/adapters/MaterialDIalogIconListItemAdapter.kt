package com.masterwok.shrimplesearch.features.query.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.masterwok.shrimplesearch.R
import com.masterwok.shrimplesearch.common.contracts.Configurable
import com.masterwok.shrimplesearch.common.utils.notNull
import kotlinx.android.synthetic.main.view_material_dialog_icon_item.view.*

class MaterialDialogIconListItemAdapter :
    RecyclerView.Adapter<MaterialDialogIconListItemAdapter.ViewHolder>(),
    Configurable<List<MaterialDialogIconListItemAdapter.Item>> {

    private lateinit var configuredModel: List<Item>

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder = ViewHolder(
        LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.view_material_dialog_icon_item, parent, false
            )
    )

    override fun getItemCount(): Int = configuredModel.count()

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) = holder.configure(configuredModel[position])

    override fun configure(model: List<Item>) {
        configuredModel = model

        notifyDataSetChanged()
    }

    class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView), Configurable<Item> {
        override fun configure(model: Item) = itemView.context.notNull { context ->
            itemView.imageView.setImageResource(model.drawable)
            itemView.textView.text = context.getString(model.label)
            itemView.setOnClickListener { model.onItemTap() }
        }
    }

    data class Item(
        @DrawableRes val drawable: Int,
        @StringRes val label: Int,
        val onItemTap: () -> Unit
    )

}