package io.github.zwieback.familyfinance.business.article.activity

import android.content.Intent
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.article.activity.ArticleEditActivity.Companion.INPUT_ARTICLE_ID
import io.github.zwieback.familyfinance.business.article.filter.ExpenseArticleFilter
import io.github.zwieback.familyfinance.business.article.filter.ExpenseArticleFilter.Companion.EXPENSE_ARTICLE_FILTER
import io.github.zwieback.familyfinance.business.article.fragment.ExpenseArticleFragment
import io.github.zwieback.familyfinance.core.activity.EntityFolderEditActivity.Companion.INPUT_IS_FOLDER
import io.github.zwieback.familyfinance.core.activity.EntityFolderEditActivity.Companion.INPUT_PARENT_ID
import io.github.zwieback.familyfinance.core.model.ArticleView

class ExpenseArticleActivity : ArticleActivity<ExpenseArticleFragment, ExpenseArticleFilter>() {

    override val titleStringId: Int
        get() = R.string.expense_article_activity_title

    override val filterName: String
        get() = EXPENSE_ARTICLE_FILTER

    override val defaultParentId: Int?
        get() = databasePrefs.expensesArticleId

    override fun createDefaultFilter(): ExpenseArticleFilter {
        return ExpenseArticleFilter().apply {
            putParentId(initialParentId)
        }
    }

    override fun createFragment(): ExpenseArticleFragment {
        return ExpenseArticleFragment.newInstance(filter)
    }

    override fun addEntity(parentId: Int, isFolder: Boolean) {
        super.addEntity(parentId, isFolder)
        val intent = Intent(this, ExpenseArticleEditActivity::class.java)
            .putExtra(INPUT_PARENT_ID, parentId)
            .putExtra(INPUT_IS_FOLDER, isFolder)
        startActivity(intent)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun editEntity(article: ArticleView) {
        super.editEntity(article)
        val intent = Intent(this, ExpenseArticleEditActivity::class.java)
            .putExtra(INPUT_ARTICLE_ID, article.id)
        startActivity(intent)
    }
}
