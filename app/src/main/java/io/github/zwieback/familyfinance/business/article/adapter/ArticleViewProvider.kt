package io.github.zwieback.familyfinance.business.article.adapter

import android.content.Context

import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial

import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.adapter.EntityProvider
import io.github.zwieback.familyfinance.core.model.ArticleView
import io.github.zwieback.familyfinance.core.model.type.ArticleType

class ArticleViewProvider(context: Context) : EntityProvider<ArticleView>(context) {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun provideDefaultIcon(article: ArticleView): IIcon {
        return if (article.isFolder) {
            CommunityMaterial.Icon.cmd_folder
        } else
            when (article.type) {
                ArticleType.EXPENSE_ARTICLE -> CommunityMaterial.Icon2.cmd_trending_down
                ArticleType.INCOME_ARTICLE -> CommunityMaterial.Icon2.cmd_trending_up
                else -> CommunityMaterial.Icon2.cmd_swap_horizontal
            }
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun provideDefaultIconColor(article: ArticleView): Int {
        return if (article.isFolder) {
            R.color.colorPrimaryDark
        } else
            when (article.type) {
                ArticleType.EXPENSE_ARTICLE -> R.color.colorExpense
                ArticleType.INCOME_ARTICLE -> R.color.colorIncome
                else -> R.color.colorPrimaryDark
            }
    }
}
