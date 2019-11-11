package io.github.zwieback.familyfinance.business.article.lifecycle.creator.exception

import java.util.*

class NoArticleFoundException(name: String) :
    NoSuchElementException("No article was found with name '$name'") {

    companion object {
        private const val serialVersionUID = -4412352592461681914L
    }
}
