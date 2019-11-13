package io.github.zwieback.familyfinance.business.article.activity

import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.model.type.ArticleType

class IncomeArticleEditActivity : ArticleEditActivity() {

    override val titleStringId: Int
        get() = R.string.income_article_activity_edit_title

    override val articleType: ArticleType
        get() = ArticleType.INCOME_ARTICLE
}
