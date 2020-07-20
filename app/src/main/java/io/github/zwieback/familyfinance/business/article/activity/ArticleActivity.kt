package io.github.zwieback.familyfinance.business.article.activity

import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.article.filter.ArticleFilter
import io.github.zwieback.familyfinance.business.article.lifecycle.destroyer.ArticleAsParentDestroyer
import io.github.zwieback.familyfinance.business.article.lifecycle.destroyer.ArticleFromExpenseOperationsDestroyer
import io.github.zwieback.familyfinance.business.article.listener.OnArticleClickListener
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_ARTICLE_ID
import io.github.zwieback.familyfinance.constant.UiConstants.UI_DEBOUNCE_TIMEOUT
import io.github.zwieback.familyfinance.core.activity.EntityFolderActivity
import io.github.zwieback.familyfinance.core.fragment.EntityFolderFragment
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer
import io.github.zwieback.familyfinance.core.model.Article
import io.github.zwieback.familyfinance.core.model.ArticleView
import io.github.zwieback.familyfinance.databinding.ItemArticleBinding
import java.util.concurrent.TimeUnit

abstract class ArticleActivity<FRAGMENT, FILTER> :
    EntityFolderActivity<ArticleView, Article, ArticleFilter, FRAGMENT>(),
    OnArticleClickListener
        where FRAGMENT : EntityFolderFragment<ArticleView, ArticleFilter, ItemArticleBinding, OnArticleClickListener, *>,
              FILTER : ArticleFilter {

    private var searchItem: MenuItem? = null

    protected val initialParentId: Int?
        get() = parentFolderId ?: defaultParentId

    protected abstract val defaultParentId: Int?

    override val resultName: String
        get() = RESULT_ARTICLE_ID

    override val isFirstFrame: Boolean
        get() = filter.getParentId() == initialParentId

    override val fragmentTag: String
        get() = "${localClassName}_${filter.getParentId()}"

    override val classOfRegularEntity: Class<Article>
        get() = Article::class.java

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_entity_search, menu)
        searchItem = menu.findItem(R.id.action_search)
        (searchItem?.actionView as? SearchView?)?.let { searchView ->
            setupSearchItem(searchItem, menu)
            setupSearchView(searchView)
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupSearchItem(searchItem: MenuItem?, menu: Menu) {
        searchItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                showMenuGroupOfAddEntries(menu, false)
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                showMenuGroupOfAddEntries(menu, true)
                // need invalidate options menu, because of platform bug:
                // see similar example: https://github.com/jakewharton/actionbarsherlock/issues/467
                invalidateOptionsMenu()
                return true
            }
        })
    }

    private fun setupSearchView(searchView: SearchView) {
        addDisposable(
            searchView.queryTextChanges()
                .debounce(UI_DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe { searchName ->
                    filter.searchName = searchName.toString()
                    applyFilter(filter)
                }
        )
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun onFolderClick(view: View, article: ArticleView) {
        closeSearchView()
        super.onFolderClick(view, article)
    }

    override fun addEntity(parentId: Int, isFolder: Boolean) {
        closeSearchView()
        super.addEntity(parentId, isFolder)
    }

    override fun editEntity(entity: ArticleView) {
        closeSearchView()
        super.editEntity(entity)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun createDestroyer(article: ArticleView): EntityDestroyer<Article> {
        return if (article.isFolder) {
            ArticleAsParentDestroyer(this, data)
        } else {
            ArticleFromExpenseOperationsDestroyer(this, data)
        }
    }

    private fun closeSearchView() {
        searchItem?.collapseActionView()
    }

    private fun showMenuGroupOfAddEntries(menu: Menu, shown: Boolean) {
        menu.setGroupEnabled(R.id.action_add_entries, shown)
        menu.setGroupVisible(R.id.action_add_entries, shown)
    }
}
