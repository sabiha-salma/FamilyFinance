package io.github.zwieback.familyfinance.business.article.lifecycle.creator

import android.content.Context
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.model.Article
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore

class ArticleFoldersCreator(
    context: Context,
    data: ReactiveEntityStore<Persistable>
) : ArticleCreator(context, data) {

    override fun buildEntities(): Iterable<Article> {
        return sortedSetOf(this).apply {
            addAll(createIncomes())
            addAll(createExpenses())
        }
    }

    private fun createIncomes(): Set<Article> {
        val root = findRoot(R.string.article_incomes)
        return setOf(
            createIncomeFolder(root, R.string.article_bank),
            createIncomeFolder(root, R.string.article_gifts),
            createIncomeFolder(root, R.string.article_work)
        )
    }

    private fun createExpenses(): Set<Article> {
        val root = findRoot(R.string.article_expenses)
        return setOf(
            createExpenseFolder(root, R.string.article_auto),
            createExpenseFolder(root, R.string.article_home),
            createExpenseFolder(root, R.string.article_clothes),
            createExpenseFolder(root, R.string.article_products),
            createExpenseFolder(root, R.string.article_transport),
            createExpenseFolder(root, R.string.article_education),
            createExpenseFolder(root, R.string.article_entertainment),
            createExpenseFolder(root, R.string.article_beauty_and_health)
        )
    }
}
