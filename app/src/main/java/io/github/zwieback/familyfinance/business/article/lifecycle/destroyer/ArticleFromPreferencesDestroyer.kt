package io.github.zwieback.familyfinance.business.article.lifecycle.destroyer

import android.content.Context
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityFromPreferencesDestroyer
import io.github.zwieback.familyfinance.core.model.Article
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

internal class ArticleFromPreferencesDestroyer(
    context: Context,
    data: ReactiveEntityStore<Persistable>
) : EntityFromPreferencesDestroyer<Article>(context, data) {

    override val alertResourceId: Int
        get() = R.string.preferences_contains_article

    override fun next(): EntityDestroyer<Article>? {
        return ArticleForceDestroyer(context, data)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun preferencesContainsEntity(article: Article): Boolean {
        val articleIds = runBlocking(Dispatchers.IO) {
            listOf(
                databasePrefs.incomesArticleId,
                databasePrefs.expensesArticleId,
                databasePrefs.transferArticleId
            )
        }
        return article.id in articleIds
    }
}
