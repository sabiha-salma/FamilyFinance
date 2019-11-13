package io.github.zwieback.familyfinance.business.article.adapter

import android.content.Context
import android.view.LayoutInflater
import io.github.zwieback.familyfinance.business.article.filter.ArticleFilter
import io.github.zwieback.familyfinance.business.article.listener.OnArticleClickListener
import io.github.zwieback.familyfinance.core.adapter.BindingHolder
import io.github.zwieback.familyfinance.core.adapter.EntityFolderAdapter
import io.github.zwieback.familyfinance.core.adapter.EntityProvider
import io.github.zwieback.familyfinance.core.model.ArticleView
import io.github.zwieback.familyfinance.databinding.ItemArticleBinding
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore

abstract class ArticleAdapter internal constructor(
    context: Context,
    clickListener: OnArticleClickListener,
    data: ReactiveEntityStore<Persistable>,
    filter: ArticleFilter
) : EntityFolderAdapter<ArticleView, ArticleFilter, ItemArticleBinding, OnArticleClickListener>(
    ArticleView.`$TYPE`,
    context,
    clickListener,
    data,
    filter
) {

    override fun createProvider(context: Context): EntityProvider<ArticleView> {
        return ArticleViewProvider(context)
    }

    override fun inflate(inflater: LayoutInflater): ItemArticleBinding {
        return ItemArticleBinding.inflate(inflater)
    }

    override fun extractEntity(binding: ItemArticleBinding): ArticleView {
        return binding.article as ArticleView
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun onBindViewHolder(
        article: ArticleView?,
        holder: BindingHolder<ItemArticleBinding>,
        position: Int
    ) {
        article?.let {
            holder.binding.article = article
            provider.setupIcon(holder.binding.icon.icon, article)
        }
    }
}
