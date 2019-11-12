package io.github.zwieback.familyfinance.business.iconics.fragment.item

import android.graphics.Color
import android.view.View
import com.mikepenz.fastadapter.items.AbstractItem
import com.mikepenz.iconics.IconicsColor
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.IconicsSize
import io.github.zwieback.familyfinance.R

class IconItem(val icon: String) : AbstractItem<ItemViewHolder>() {

    override val type: Int
        get() = R.id.item_row_icon

    override val layoutRes: Int
        get() = R.layout.card_iconics

    override fun bindView(holder: ItemViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)

        holder.image.icon = IconicsDrawable(holder.image.context, icon)
        holder.name.text = icon
        holder.image.icon?.apply {
            color(IconicsColor.colorInt(Color.BLACK))
            padding(IconicsSize.dp(0))
            contourWidth(IconicsSize.dp(0))
            contourColor(IconicsColor.colorInt(Color.TRANSPARENT))
            // as we want to respect the bounds of the original font in the icon list
            respectFontBounds(true)
        }
        holder.image.setBackgroundColor(Color.TRANSPARENT)
    }

    override fun getViewHolder(v: View): ItemViewHolder {
        return ItemViewHolder(v)
    }
}
