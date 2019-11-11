package io.github.zwieback.familyfinance.business.article.lifecycle.creator

import android.content.Context
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.model.Article
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore

class ArticleEntriesCreator(
    context: Context,
    data: ReactiveEntityStore<Persistable>
) : ArticleCreator(context, data) {

    override fun buildEntities(): Iterable<Article> {
        return sortedSetOf(this).apply {
            addAll(createIncomes())
            addAll(createExpenses())
            addAll(createServices())
        }
    }

    private fun createIncomes(): Set<Article> {
        val bank = findFolder(R.string.article_bank)
        val work = findFolder(R.string.article_work)
        return setOf(
            createIncomeEntry(bank, R.string.article_capitalization_of_interest),
            createIncomeEntry(work, R.string.article_salary),
            createIncomeEntry(work, R.string.article_bonus)
        )
    }

    private fun createExpenses(): Set<Article> {
        val auto = findFolder(R.string.article_auto)
        val home = findFolder(R.string.article_home)
        val clothes = findFolder(R.string.article_clothes)
        val transport = findFolder(R.string.article_transport)
        val education = findFolder(R.string.article_education)
        val entertainment = findFolder(R.string.article_entertainment)
        return setOf(
            createExpenseEntry(auto, R.string.article_fuel),
            createExpenseEntry(auto, R.string.article_maintenance),
            createExpenseEntry(home, R.string.article_rent),
            createExpenseEntry(clothes, R.string.article_jacket),
            createExpenseEntry(transport, R.string.article_taxi),
            createExpenseEntry(transport, R.string.article_metro),
            createExpenseEntry(education, R.string.article_foreign_language_courses),
            createExpenseEntry(entertainment, R.string.article_cinema),
            createExpenseEntry(entertainment, R.string.article_theater)
        )
    }

    private fun createServices(): Set<Article> {
        val root = findRoot(R.string.article_service)
        return setOf(
            createServiceEntry(root, R.string.article_transfer)
        )
    }
}
