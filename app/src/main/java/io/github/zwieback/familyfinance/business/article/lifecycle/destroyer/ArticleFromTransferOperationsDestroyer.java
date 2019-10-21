package io.github.zwieback.familyfinance.business.article.lifecycle.destroyer;

import android.content.Context;

import io.github.zwieback.familyfinance.R;
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer;
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityFromDestroyer;
import io.github.zwieback.familyfinance.core.model.Article;
import io.github.zwieback.familyfinance.core.model.Operation;
import io.requery.Persistable;
import io.requery.query.Condition;
import io.requery.reactivex.ReactiveEntityStore;

import static io.github.zwieback.familyfinance.core.model.type.OperationType.TRANSFER_INCOME_OPERATION;

class ArticleFromTransferOperationsDestroyer extends EntityFromDestroyer<Article, Operation> {

    ArticleFromTransferOperationsDestroyer(Context context, ReactiveEntityStore<Persistable> data) {
        super(context, data);
    }

    @Override
    protected EntityDestroyer<Article> next() {
        return new ArticleFromPreferencesDestroyer(getContext(), getData());
    }

    @Override
    protected Class<Operation> getFromClass() {
        return Operation.class;
    }

    @Override
    protected Condition<?, ?> getWhereCondition(Article article) {
        return Operation.TYPE.in(TRANSFER_INCOME_OPERATION, TRANSFER_INCOME_OPERATION)
                .and(Operation.ARTICLE_ID.eq(article.getId()));
    }

    @Override
    protected int getAlertResourceId() {
        return R.string.transfer_operations_with_article_exists;
    }
}
