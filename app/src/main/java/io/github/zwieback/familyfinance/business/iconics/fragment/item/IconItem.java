package io.github.zwieback.familyfinance.business.iconics.fragment.item;

import android.graphics.Color;
import android.view.View;
import androidx.annotation.NonNull;

import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.iconics.IconicsColor;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.IconicsSize;

import java.util.List;

import io.github.zwieback.familyfinance.R;

public class IconItem extends AbstractItem<ItemViewHolder> {

    @NonNull
    private final String icon;

    public IconItem(@NonNull String icon) {
        this.icon = icon;
    }

    @Override
    public void bindView(@NonNull ItemViewHolder holder, @NonNull List<Object> payloads) {
        super.bindView(holder, payloads);

        holder.image.setIcon(new IconicsDrawable(holder.image.getContext(), icon));
        holder.name.setText(icon);

        IconicsDrawable imageIcon = holder.image.getIcon();
        if (imageIcon != null) {
            imageIcon.color(IconicsColor.colorInt(Color.BLACK));
            imageIcon.padding(IconicsSize.dp(0));
            imageIcon.contourWidth(IconicsSize.dp(0));
            imageIcon.contourColor(IconicsColor.colorInt(Color.TRANSPARENT));
            // as we want to respect the bounds of the original font in the icon list
            imageIcon.respectFontBounds(true);
        }
        holder.image.setBackgroundColor(Color.TRANSPARENT);
    }

    @NonNull
    @Override
    public ItemViewHolder getViewHolder(@NonNull View v) {
        return new ItemViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.item_row_icon;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.card_iconics;
    }

    @NonNull
    public String getIcon() {
        return icon;
    }
}
