package io.github.zwieback.familyfinance.business.preference.custom.database

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import androidx.core.content.res.TypedArrayUtils
import io.github.zwieback.familyfinance.business.article.activity.AllArticleActivity
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_ARTICLE_ID
import io.github.zwieback.familyfinance.core.model.Article
import io.github.zwieback.familyfinance.core.preference.custom.EntityActivityResultPreference

abstract class ArticlePreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = TypedArrayUtils.getAttr(
        context,
        androidx.preference.R.attr.preferenceStyle,
        android.R.attr.preferenceStyle
    ),
    defStyleRes: Int = 0
) : EntityActivityResultPreference<Article>(context, attrs, defStyleAttr, defStyleRes) {

    override val requestIntent: Intent
        get() = Intent(context, AllArticleActivity::class.java)

    override val resultName: String
        get() = RESULT_ARTICLE_ID

    override val entityClass: Class<Article>
        get() = Article::class.java

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun getEntityName(article: Article): String {
        return article.name
    }
}
