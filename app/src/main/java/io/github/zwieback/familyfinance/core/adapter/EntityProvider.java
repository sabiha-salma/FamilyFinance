package io.github.zwieback.familyfinance.core.adapter;

import android.content.Context;
import android.util.Log;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mikepenz.iconics.Iconics;
import com.mikepenz.iconics.IconicsColor;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.iconics.typeface.ITypeface;
import com.mikepenz.iconics.utils.IconicsExtensionsKt;

import io.github.zwieback.familyfinance.BuildConfig;
import io.github.zwieback.familyfinance.core.model.IBaseEntity;

import static io.github.zwieback.familyfinance.util.StringUtils.isTextNotEmpty;

public abstract class EntityProvider<E extends IBaseEntity> {

    private static final String TAG = "EntityProvider";

    protected final Context context;

    protected EntityProvider(Context context) {
        this.context = context;
    }

    @NonNull
    public abstract IIcon provideDefaultIcon(E entity);

    @ColorRes
    public abstract int provideDefaultIconColor(E entity);

    @ColorInt
    public int provideTextColor(E entity) {
        throw new UnsupportedOperationException();
    }

    public final void setupIcon(@Nullable IconicsDrawable drawable, E entity) {
        if (drawable == null) {
            return;
        }
        IIcon icon = provideIcon(entity);
        if (icon == null) {
            icon = provideDefaultIcon(entity);
        }
        drawable.icon(icon);
        drawable.color(IconicsColor.colorRes(provideDefaultIconColor(entity)));
    }

    /**
     * Returns the icon of the entity, if it is defined.
     *
     * @see com.mikepenz.iconics.IconicsDrawable#icon(String)
     */
    @Nullable
    private IIcon provideIcon(E entity) {
        String iconName = entity.getIconName();
        if (isTextNotEmpty(iconName)) {
            String fontName = IconicsExtensionsKt.getIconPrefix(iconName);
            ITypeface font = Iconics.findFont(fontName, context);
            if (font != null) {
                String clearedIconName = IconicsExtensionsKt.getClearedIconName(iconName);
                try {
                    return font.getIcon(clearedIconName);
                } catch (IllegalArgumentException e) {
                    if (BuildConfig.DEBUG) {
                        Log.w(TAG, "Icon " + iconName + " not found");
                    }
                    return null;
                }
            }
        }
        return null;
    }
}
