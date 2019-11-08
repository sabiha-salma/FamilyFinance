package io.github.zwieback.familyfinance.business.article.activity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import com.annimon.stream.Objects;
import com.jakewharton.rxbinding3.appcompat.RxSearchView;

import java.util.concurrent.TimeUnit;

import io.github.zwieback.familyfinance.R;
import io.github.zwieback.familyfinance.business.article.filter.ArticleFilter;
import io.github.zwieback.familyfinance.business.article.lifecycle.destroyer.ArticleAsParentDestroyer;
import io.github.zwieback.familyfinance.business.article.lifecycle.destroyer.ArticleFromExpenseOperationsDestroyer;
import io.github.zwieback.familyfinance.business.article.listener.OnArticleClickListener;
import io.github.zwieback.familyfinance.core.activity.EntityFolderActivity;
import io.github.zwieback.familyfinance.core.fragment.EntityFolderFragment;
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer;
import io.github.zwieback.familyfinance.core.model.Article;
import io.github.zwieback.familyfinance.core.model.ArticleView;
import io.github.zwieback.familyfinance.databinding.ItemArticleBinding;

import static io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.RESULT_ARTICLE_ID;
import static io.github.zwieback.familyfinance.util.NumberUtils.UI_DEBOUNCE_TIMEOUT;

public abstract class ArticleActivity<
        FRAGMENT extends EntityFolderFragment<ArticleView, ArticleFilter, ItemArticleBinding, OnArticleClickListener, ?>,
        FILTER extends ArticleFilter>
        extends EntityFolderActivity<ArticleView, Article, ArticleFilter, FRAGMENT>
        implements OnArticleClickListener {

    private MenuItem searchItem;

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_entity_search, menu);
        searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        setupSearchItem(searchItem, menu);
        setupSearchView(searchView);
        return super.onCreateOptionsMenu(menu);
    }

    private void setupSearchItem(@Nullable MenuItem searchItem, @NonNull Menu menu) {
        if (searchItem == null) {
            return;
        }
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                showMenuGroupOfAddEntries(menu, false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                showMenuGroupOfAddEntries(menu, true);
                // need invalidate options menu, because of platform bug:
                // see similar example: https://github.com/jakewharton/actionbarsherlock/issues/467
                invalidateOptionsMenu();
                return true;
            }
        });
    }

    private void setupSearchView(@NonNull SearchView searchView) {
        addDisposable(
                RxSearchView.queryTextChanges(searchView)
                        .debounce(UI_DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS)
                        .subscribe(searchName -> {
                            getFilter().setSearchName(searchName.toString());
                            applyFilter(getFilter());
                        })
        );
    }

    @Nullable
    protected Integer getInitialParentId() {
        return getParentFolderId() != null ? getParentFolderId() : getDefaultParentId();
    }

    @Nullable
    abstract Integer getDefaultParentId();

    @NonNull
    @Override
    protected String getResultName() {
        return RESULT_ARTICLE_ID;
    }

    @Override
    protected boolean isFirstFrame() {
        return Objects.equals(getFilter().getParentId(), getInitialParentId());
    }

    @NonNull
    @Override
    protected String getFragmentTag() {
        return String.format("%s_%s", getLocalClassName(), getFilter().getParentId());
    }

    @Override
    public void onFolderClick(@NonNull View view, @NonNull ArticleView article) {
        closeSearchView();
        super.onFolderClick(view, article);
    }

    @Override
    protected void addEntity(int parentId, boolean isFolder) {
        closeSearchView();
        super.addEntity(parentId, isFolder);
    }

    @Override
    protected void editEntity(@NonNull ArticleView entity) {
        closeSearchView();
        super.editEntity(entity);
    }

    @NonNull
    @Override
    protected Class<Article> getClassOfRegularEntity() {
        return Article.class;
    }

    @NonNull
    @Override
    protected EntityDestroyer<Article> createDestroyer(ArticleView article) {
        if (article.isFolder()) {
            return new ArticleAsParentDestroyer(this, getData());
        }
        return new ArticleFromExpenseOperationsDestroyer(this, getData());
    }

    private void closeSearchView() {
        searchItem.collapseActionView();
    }

    private static void showMenuGroupOfAddEntries(Menu menu, boolean shown) {
        menu.setGroupEnabled(R.id.action_add_entries, shown);
        menu.setGroupVisible(R.id.action_add_entries, shown);
    }
}
