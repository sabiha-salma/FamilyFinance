package io.github.zwieback.familyfinance.business.iconics.fragment.item

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.iconics.view.IconicsImageView
import io.github.zwieback.familyfinance.R

class ItemViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val name: TextView = itemView.findViewById(R.id.name)
    val image: IconicsImageView = itemView.findViewById(R.id.icon)
}
