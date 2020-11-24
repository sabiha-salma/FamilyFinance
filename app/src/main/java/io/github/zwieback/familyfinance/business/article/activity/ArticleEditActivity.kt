package io.github.zwieback.familyfinance.business.article.activity

import android.app.Activity
import android.content.Intent
import com.johnpetitto.validator.ValidatingTextInputLayout
import com.mikepenz.iconics.view.IconicsImageView
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.article.adapter.ArticleProvider
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.ARTICLE_CODE
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_ARTICLE_ID
import io.github.zwieback.familyfinance.core.activity.EntityActivity
import io.github.zwieback.familyfinance.core.activity.EntityFolderActivity
import io.github.zwieback.familyfinance.core.activity.EntityFolderEditActivity
import io.github.zwieback.familyfinance.core.adapter.EntityProvider
import io.github.zwieback.familyfinance.core.model.Article
import io.github.zwieback.familyfinance.core.model.type.ArticleType
import io.github.zwieback.familyfinance.databinding.ActivityEditArticleBinding
import io.github.zwieback.familyfinance.extension.isNotEmptyId
import io.github.zwieback.familyfinance.extension.toBigDecimalOrNull
import io.github.zwieback.familyfinance.extension.transliterate
import org.threeten.bp.LocalDateTime

abstract class ArticleEditActivity :
    EntityFolderEditActivity<Article, ActivityEditArticleBinding>() {

    protected abstract val articleType: ArticleType

    override val bindingLayoutId: Int
        get() = R.layout.activity_edit_article

    override val extraInputId: String
        get() = INPUT_ARTICLE_ID

    override val extraOutputId: String
        get() = OUTPUT_ARTICLE_ID

    override val entityClass: Class<Article>
        get() = Article::class.java

    override val layoutsForValidation: List<ValidatingTextInputLayout>
        get() {
            val layouts = mutableListOf(binding.parentLayout, binding.nameLayout)
            if (!entity.isFolder && !binding.defaultValue.text?.toString().isNullOrEmpty()) {
                layouts.add(binding.defaultValueLayout)
            }
            return layouts
        }

    override val iconView: IconicsImageView
        get() = binding.icon

    override val parentLayout: ValidatingTextInputLayout
        get() = binding.parentLayout

    override fun createProvider(): EntityProvider<Article> {
        return ArticleProvider(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultIntent)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            ARTICLE_CODE -> resultIntent?.let {
                val parentId = extractOutputId(resultIntent, RESULT_ARTICLE_ID)
                loadParent(parentId)
            }
        }
    }

    private fun onParentClick() {
        val intent = Intent(this, AllArticleActivity::class.java)
            .putExtra(EntityActivity.INPUT_REGULAR_SELECTABLE, false)
            .putExtra(EntityFolderActivity.INPUT_PROHIBITED_FOLDER_ID, entity.id)
        startActivityForResult(intent, ARTICLE_CODE)
    }

    private fun onParentRemoved() {
        entity.setParent(null)
        binding.parentLayout.error = null
    }

    private fun loadParent(parentId: Int) {
        if (parentId.isNotEmptyId()) {
            loadEntity(Article::class.java, parentId) { foundArticle ->
                entity.setParent(foundArticle)
                binding.parentLayout.error = null
            }
        }
    }

    override fun createEntity() {
        val parentId = extractInputId(INPUT_PARENT_ID)
        val folder = extractInputBoolean(INPUT_IS_FOLDER)
        val article = Article()
            .setCreateDate(LocalDateTime.now())
            .setType(articleType)
            .setFolder(folder)
        bind(article)
        loadParent(parentId)
        disableLayout(binding.parentLayout, R.string.hint_parent_disabled)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun bind(article: Article) {
        entity = article
        binding.article = article
        provider.setupIcon(binding.icon.icon, article)
        super.bind(article)
    }

    override fun setupBindings() {
        binding.icon.setOnClickListener { onSelectIconClick() }
        binding.parent.setOnClickListener { onParentClick() }
        binding.parent.setOnClearTextListener { onParentRemoved() }
        binding.parentLayout.setValidator { isParentValid(it) }
        if (!entity.isFolder) {
            return
        }
        disableLayout(binding.defaultValueLayout, R.string.hint_default_value_disabled)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun updateEntityProperties(article: Article) {
        article.setLastChangeDate(LocalDateTime.now())
        article.setName(binding.name.text?.toString())
        article.setNameAscii(article.name.transliterate())
        if (!entity.isFolder) {
            article.setDefaultValue(binding.defaultValue.text?.toString()?.toBigDecimalOrNull())
        }
    }

    private fun isParentValid(input: String): Boolean {
        return isParentValid(input, entity.parent as Article?, Article.`$TYPE`.name)
    }

    override fun isParentInsideItself(parentId: Int, newParentId: Int): Boolean {
        return isParentInsideItself(
            newParentId,
            Article.ID,
            Article.PARENT_ID.eq(parentId).and(Article.FOLDER.eq(true))
        )
    }

    companion object {
        const val INPUT_ARTICLE_ID = "articleId"
        const val OUTPUT_ARTICLE_ID = "resultArticleId"
    }
}
