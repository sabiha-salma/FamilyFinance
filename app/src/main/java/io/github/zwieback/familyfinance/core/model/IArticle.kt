package io.github.zwieback.familyfinance.core.model

import androidx.databinding.Bindable
import io.github.zwieback.familyfinance.core.model.constant.ArticleRestriction
import io.github.zwieback.familyfinance.core.model.constant.Tables
import io.github.zwieback.familyfinance.core.model.converter.BigDecimalToWorthConverter
import io.github.zwieback.familyfinance.core.model.type.ArticleType
import io.requery.*
import java.math.BigDecimal

@Entity(propertyNameStyle = PropertyNameStyle.FLUENT_BEAN)
@Table(name = Tables.ARTICLE)
interface IArticle : IBaseEntityFolder {

    @get:Bindable
    @get:Nullable
    @get:ForeignKey
    @get:OneToOne(mappedBy = "id", cascade = [CascadeAction.NONE])
    @get:Index("idx_parent_article_id")
    @get:Column(name = "parent_id")
    val parent: IArticle?

    @get:Column(name = "_type", nullable = false, length = ArticleRestriction.TYPE_MAX_LENGTH)
    val type: ArticleType

    @get:Bindable
    @get:Column(nullable = false, length = ArticleRestriction.NAME_MAX_LENGTH)
    val name: String

    /**
     * Workaround for constraints of SQLite
     *
     * See [SQLite upper() alike function for international characters](https://stackoverflow.com/a/16299031/8035065)
     */
    @get:Column(
        name = "name_ascii",
        nullable = false,
        length = ArticleRestriction.NAME_ASCII_MAX_LENGTH
    )
    val nameAscii: String

    @get:Bindable
    @get:Nullable
    @get:Column(name = "default_value")
    @get:Convert(BigDecimalToWorthConverter::class)
    val defaultValue: BigDecimal?
}
