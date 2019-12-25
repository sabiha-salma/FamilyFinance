package io.github.zwieback.familyfinance.business.preference.custom.database

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.res.TypedArrayUtils
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.INCOME_ARTICLE_CODE

class IncomesArticlePreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = TypedArrayUtils.getAttr(
        context,
        androidx.preference.R.attr.preferenceStyle,
        android.R.attr.preferenceStyle
    ),
    defStyleRes: Int = 0
) : ArticlePreference(context, attrs, defStyleAttr, defStyleRes) {

    override val requestCode: Int
        get() = INCOME_ARTICLE_CODE

    override val preferenceTitleRes: Int
        get() = R.string.incomes_article_id_preference_title

    override suspend fun getSavedEntityId(): Int {
        return databasePrefs.incomesArticleId
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override suspend fun saveEntityId(incomesArticleId: Int) {
        databasePrefs.incomesArticleId = incomesArticleId
    }
}
