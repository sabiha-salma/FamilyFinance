package io.github.zwieback.familyfinance.business.article.filter

import io.github.zwieback.familyfinance.core.filter.EntityFolderFilter

abstract class ArticleFilter : EntityFolderFilter() {

    abstract var searchName: String?
}
