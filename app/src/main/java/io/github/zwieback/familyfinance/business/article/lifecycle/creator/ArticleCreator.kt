package io.github.zwieback.familyfinance.business.article.lifecycle.creator

import android.content.Context
import androidx.annotation.StringRes
import com.annimon.stream.ComparatorCompat
import com.annimon.stream.function.Function
import io.github.zwieback.familyfinance.core.lifecycle.creator.EntityCreator
import io.github.zwieback.familyfinance.core.model.Article
import io.github.zwieback.familyfinance.core.model.type.ArticleType
import io.github.zwieback.familyfinance.core.model.type.ArticleType.*
import io.github.zwieback.familyfinance.util.TransliterationUtils.transliterate
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore

abstract class ArticleCreator(
    context: Context,
    data: ReactiveEntityStore<Persistable>
) : EntityCreator<Article>(context, data) {

    /**
     * See [Gradle compile error when using ComparatorCompat](https://github.com/aNNiMON/Lightweight-Stream-API/issues/148)
     */
    override fun compare(left: Article, right: Article): Int {
        return ComparatorCompat.comparing(Function<Article, ArticleType> { it.type })
            .thenComparing(Function<Article, String> { it.name })
            .compare(left, right)
    }

    fun findRoot(@StringRes nameId: Int): Article {
        val name = getString(nameId)
        return data
            .select(Article::class.java)
            .where(Article.NAME.eq(name).and(Article.PARENT_ID.isNull))
            .get().first()
    }

    fun findFolder(@StringRes nameId: Int): Article {
        val name = getString(nameId)
        return data
            .select(Article::class.java)
            .where(Article.NAME.eq(name).and(Article.PARENT_ID.notNull()))
            .get().first()
    }

    fun createIncomeFolder(parent: Article?, @StringRes nameId: Int): Article {
        return createArticle(parent, INCOME_ARTICLE, nameId, true)
    }

    fun createIncomeEntry(parent: Article?, @StringRes nameId: Int): Article {
        return createArticle(parent, INCOME_ARTICLE, nameId, false)
    }

    fun createExpenseFolder(parent: Article?, @StringRes nameId: Int): Article {
        return createArticle(parent, EXPENSE_ARTICLE, nameId, true)
    }

    fun createExpenseEntry(parent: Article?, @StringRes nameId: Int): Article {
        return createArticle(parent, EXPENSE_ARTICLE, nameId, false)
    }

    fun createServiceFolder(parent: Article?, @StringRes nameId: Int): Article {
        return createArticle(parent, SERVICE_ARTICLE, nameId, true)
    }

    fun createServiceEntry(parent: Article?, @StringRes nameId: Int): Article {
        return createArticle(parent, SERVICE_ARTICLE, nameId, false)
    }

    private fun createArticle(
        parent: Article?,
        type: ArticleType, @StringRes nameId: Int,
        folder: Boolean
    ): Article {
        val name = getString(nameId)
        return Article()
            .setParent(parent)
            .setType(type)
            .setName(name)
            .setNameAscii(transliterate(name))
            .setFolder(folder)
    }
}
