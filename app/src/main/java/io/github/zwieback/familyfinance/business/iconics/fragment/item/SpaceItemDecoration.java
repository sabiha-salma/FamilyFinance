package io.github.zwieback.familyfinance.business.iconics.fragment.item;

import android.content.res.Resources;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

/**
 * @author pa.gulko zTrap (28.10.2017)
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(
            @NonNull Rect outRect,
            @NonNull View view,
            @NonNull RecyclerView parent,
            @NonNull State state
    ) {
        int space = (int) Math.ceil(8 * Resources.getSystem().getDisplayMetrics().density); // 8 dp
        int position = parent.getChildAdapterPosition(view);
        if (position < 2) {
            outRect.top = space;
        }
        if ((position + 1) % 2 == 0) {
            outRect.right = space;
        }
        outRect.left = space;
        outRect.bottom = space;
    }
}
