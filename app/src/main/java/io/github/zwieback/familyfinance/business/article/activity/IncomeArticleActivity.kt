package io.github.zwieback.familyfinance.business.article.activity

import android.content.Intent
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.article.activity.ArticleEditActivity.Companion.INPUT_ARTICLE_ID
import io.github.zwieback.familyfinance.business.article.filter.IncomeArticleFilter
import io.github.zwieback.familyfinance.business.article.filter.IncomeArticleFilter.Companion.INCOME_ARTICLE_FILTER
import io.github.zwieback.familyfinance.business.article.fragment.IncomeArticleFragment
import io.github.zwieback.familyfinance.core.activity.EntityFolderEditActivity.Companion.INPUT_IS_FOLDER
import io.github.zwieback.familyfinance.core.activity.EntityFolderEditActivity.Companion.INPUT_PARENT_ID
import io.github.zwieback.familyfinance.core.model.ArticleView

class IncomeArticleActivity : ArticleActivity<IncomeArticleFragment, IncomeArticleFilter>() {

    override val titleStringId: Int
        get() = R.string.income_article_activity_title

    override val filterName: String
        get() = INCOME_ARTICLE_FILTER

    override val defaultParentId: Int?
        get() = databasePrefs.incomesArticleId

    override fun createDefaultFilter(): IncomeArticleFilter {
        return IncomeArticleFilter().apply {
            setParentId(initialParentId)
        }
    }

    override fun createFragment(): IncomeArticleFragment {
        return IncomeArticleFragment.newInstance(filter)
    }

    override fun addEntity(parentId: Int, isFolder: Boolean) {
        super.addEntity(parentId, isFolder)
        val intent = Intent(this, IncomeArticleEditActivity::class.java)
            .putExtra(INPUT_PARENT_ID, parentId)
            .putExtra(INPUT_IS_FOLDER, isFolder)
        startActivity(intent)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun editEntity(article: ArticleView) {
        super.editEntity(article)
        val intent = Intent(this, IncomeArticleEditActivity::class.java)
            .putExtra(INPUT_ARTICLE_ID, article.id)
        startActivity(intent)
    }
}
