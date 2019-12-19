package io.github.zwieback.familyfinance.business.iconics.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.mikepenz.fastadapter.listeners.OnBindViewHolderListener
import com.mikepenz.iconics.Iconics
import com.mikepenz.iconics.IconicsColor
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.IconicsSize
import com.mikepenz.iconics.utils.IconicsUtils
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.iconics.fragment.item.IconItem
import io.github.zwieback.familyfinance.business.iconics.fragment.item.ItemViewHolder
import io.github.zwieback.familyfinance.business.iconics.fragment.item.SpaceItemDecoration
import io.github.zwieback.familyfinance.business.iconics.listener.OnIconSelectListener
import java.util.*
import kotlin.collections.ArrayList

class IconicsFragment : Fragment() {

    private lateinit var iconSelectListener: OnIconSelectListener
    private lateinit var recyclerView: RecyclerView
    private lateinit var onScrollListener: RecyclerView.OnScrollListener
    private var icons: List<IconItem> = ArrayList()
    private var popup: PopupWindow? = null
    private var adapter: FastItemAdapter<IconItem>? = null
    private var searchSuggestion: String? = null

    private val popupIconSize: Int
        get() = context?.resources?.getDimension(R.dimen.popup_icon_size)?.toInt() ?: 144

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnIconSelectListener) {
            this.iconSelectListener = context
        } else {
            throw ClassCastException("$context must implement OnIconSelectListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_iconics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = createAdapter()
        onScrollListener = createOnScrollListener()

        recyclerView = view.findViewById(R.id.icon_recycler_view)
        recyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.addItemDecoration(SpaceItemDecoration())
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(onScrollListener)

        val fontName = requireArguments().getString(FONT_NAME) as String
        val foundFont = Iconics.getRegisteredFonts(context)
            .single { font -> font.fontName.equals(fontName, ignoreCase = true) }
        icons = foundFont.icons.map { icon -> IconItem(icon) }
        adapter?.set(icons)

        onSearch(searchSuggestion)
    }

    override fun onDetach() {
        recyclerView.removeOnScrollListener(onScrollListener)
        super.onDetach()
    }

    private fun createOnScrollListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    closePopup()
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        }
    }

    private fun createAdapter(): FastItemAdapter<IconItem> {
        val fastAdapter = FastItemAdapter<IconItem>()

        fastAdapter.onClickListener = { _, _, item, _ ->
            iconSelectListener.onIconSelected(item.icon)
            true
        }

        fastAdapter.onLongClickListener = { v, _, item, _ ->
            closePopup()
            val popupIconSize = popupIconSize
            val icon = IconicsDrawable(v.context)
                .icon(item.icon)
                .size(IconicsSize.dp(popupIconSize))
                .padding(IconicsSize.res(R.dimen.popup_icon_padding))
                .backgroundColor(IconicsColor.colorRes(R.color.popup_icon_background))
                .roundedCorners(IconicsSize.res(R.dimen.popup_icon_rounded_corners))

            val imageView = ImageView(v.context)
            imageView.setImageDrawable(icon)
            val size = IconicsUtils.convertDpToPx(v.context, popupIconSize)
            popup = PopupWindow(imageView, size, size)
            popup?.showAsDropDown(v)
            true
        }

        fastAdapter.onBindViewHolderListener = object : OnBindViewHolderListener {
            override fun onBindViewHolder(
                viewHolder: RecyclerView.ViewHolder,
                position: Int,
                payloads: MutableList<Any>
            ) {
                val holder = viewHolder as ItemViewHolder
                fastAdapter.getItem(position)?.let { item ->
                    // set the R.id.fastadapter_item tag of this item
                    // to the item object (can be used when retrieving the view)
                    viewHolder.itemView.setTag(R.id.fastadapter_item, item)
                    // as we overwrite the default listener
                    item.bindView(holder, payloads)
                }
            }

            override fun unBindViewHolder(
                viewHolder: RecyclerView.ViewHolder,
                position: Int
            ) {
                val item = viewHolder.itemView.getTag(R.id.fastadapter_item) as IconItem
                item.unbindView(viewHolder as ItemViewHolder)
                // remove set tag's
                viewHolder.itemView.setTag(R.id.fastadapter_item, null)
                viewHolder.itemView.setTag(R.id.fastadapter_item_adapter, null)
            }

            override fun onViewAttachedToWindow(
                viewHolder: RecyclerView.ViewHolder,
                position: Int
            ) {
                // stub
            }

            override fun onViewDetachedFromWindow(
                viewHolder: RecyclerView.ViewHolder,
                position: Int
            ) {
                // stub
            }

            override fun onFailedToRecycleView(
                viewHolder: RecyclerView.ViewHolder,
                position: Int
            ): Boolean {
                return false
            }
        }
        return fastAdapter
    }

    private fun closePopup() {
        if (popup?.isShowing == true) {
            popup?.dismiss()
        }
    }

    fun onSearch(searchName: String?) {
        searchSuggestion = searchName
        adapter?.let { adapter ->
            if (searchSuggestion.isNullOrEmpty()) {
                adapter.clear()
                adapter.setNewList(icons, false)
            } else {
                val searchInLowerCase = searchSuggestion?.toLowerCase(Locale.getDefault()).orEmpty()
                val filteredIcons = filterIcons(searchInLowerCase)
                adapter.setNewList(filteredIcons, false)
            }
        }
    }

    private fun filterIcons(searchInLowerCase: String): List<IconItem> {
        return icons.filter { icon ->
            icon.icon.toLowerCase(Locale.getDefault()).contains(searchInLowerCase)
        }
    }

    companion object {
        private const val FONT_NAME = "FONT_NAME"

        fun newInstance(fontName: String) = IconicsFragment().apply {
            arguments = Bundle().apply {
                putString(FONT_NAME, fontName)
            }
        }
    }
}
