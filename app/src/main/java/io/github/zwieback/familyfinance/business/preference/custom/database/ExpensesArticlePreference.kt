package io.github.zwieback.familyfinance.business.preference.custom.database

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.res.TypedArrayUtils
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.EXPENSE_ARTICLE_CODE

class ExpensesArticlePreference @JvmOverloads constructor(
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
        get() = EXPENSE_ARTICLE_CODE

    override val preferenceTitleRes: Int
        get() = R.string.expenses_article_id_preference_title

    override suspend fun getSavedEntityId(): Int {
        return databasePrefs.expensesArticleId
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override suspend fun saveEntityId(expensesArticleId: Int) {
        databasePrefs.expensesArticleId = expensesArticleId
    }
}
