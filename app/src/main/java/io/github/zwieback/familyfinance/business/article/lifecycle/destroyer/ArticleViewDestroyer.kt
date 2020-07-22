package io.github.zwieback.familyfinance.business.article.lifecycle.destroyer

import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityViewDestroyer
import io.github.zwieback.familyfinance.core.model.constant.Views
import java.sql.Connection

class ArticleViewDestroyer(connection: Connection) : EntityViewDestroyer(connection) {

    override val viewName: String
        get() = Views.ARTICLE
}
