package io.github.zwieback.familyfinance.business.operation.dialog

import android.app.Activity
import android.content.Intent
import androidx.databinding.ViewDataBinding
import io.github.zwieback.familyfinance.business.article.activity.ArticleActivity
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.ARTICLE_CODE
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_ARTICLE_ID
import io.github.zwieback.familyfinance.business.operation.filter.OperationFilter
import io.github.zwieback.familyfinance.widget.listener.OnClearTextListener

abstract class OperationWithArticleFilterDialog<F, B, AA> :
    OperationFilterDialog<F, B>()
        where F : OperationFilter,
              B : ViewDataBinding,
              AA : ArticleActivity<*, *> {

    protected abstract val articleActivityClass: Class<AA>

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultIntent)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            ARTICLE_CODE -> resultIntent?.let {
                val articleId = extractId(resultIntent, RESULT_ARTICLE_ID)
                loadArticle(articleId)
            }
        }
    }

    override fun bind(filter: F) {
        articleEdit.setOnClickListener { onArticleClick() }
        articleEdit.setOnClearTextListener(object : OnClearTextListener {
            override fun onTextCleared() {
                onArticleRemoved()
            }
        })

        loadArticle(filter.getArticleId())

        super.bind(filter)
    }

    private fun onArticleClick() {
        val intent = Intent(context, articleActivityClass)
        startActivityForResult(intent, ARTICLE_CODE)
    }

    private fun onArticleRemoved() {
        filter.setArticleId(null)
    }
}
