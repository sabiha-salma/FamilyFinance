package io.github.zwieback.familyfinance.business.article.adapter;

import android.content.Context;
import androidx.annotation.NonNull;

import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.typeface.IIcon;

import io.github.zwieback.familyfinance.R;
import io.github.zwieback.familyfinance.core.adapter.EntityProvider;
import io.github.zwieback.familyfinance.core.model.Article;

public class ArticleProvider extends EntityProvider<Article> {

    public ArticleProvider(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public IIcon provideDefaultIcon(Article article) {
        if (article.isFolder()) {
            return CommunityMaterial.Icon.cmd_folder;
        }
        switch (article.getType()) {
            case EXPENSE_ARTICLE:
                return CommunityMaterial.Icon.cmd_trending_down;
            case INCOME_ARTICLE:
                return CommunityMaterial.Icon.cmd_trending_up;
            default:
                return CommunityMaterial.Icon.cmd_swap_horizontal;
        }
    }

    @Override
    public int provideDefaultIconColor(Article article) {
        if (article.isFolder()) {
            return R.color.colorPrimaryDark;
        }
        switch (article.getType()) {
            case EXPENSE_ARTICLE:
                return R.color.colorExpense;
            case INCOME_ARTICLE:
                return R.color.colorIncome;
            default:
                return R.color.colorPrimaryDark;
        }
    }
}
