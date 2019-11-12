package io.github.zwieback.familyfinance.business.iconics.fragment.item

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.State
import kotlin.math.ceil

/**
 * @author pa.gulko zTrap (28.10.2017)
 */
class SpaceItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: State
    ) {
        val space =
            ceil((8 * Resources.getSystem().displayMetrics.density).toDouble()).toInt() // 8 dp
        val position = parent.getChildAdapterPosition(view)
        if (position < 2) {
            outRect.top = space
        }
        if ((position + 1) % 2 == 0) {
            outRect.right = space
        }
        outRect.left = space
        outRect.bottom = space
    }
}
