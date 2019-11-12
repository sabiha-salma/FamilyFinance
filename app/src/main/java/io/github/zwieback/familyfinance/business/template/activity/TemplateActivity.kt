package io.github.zwieback.familyfinance.business.template.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_TEMPLATE_ID
import io.github.zwieback.familyfinance.business.template.activity.helper.TemplateQualifier
import io.github.zwieback.familyfinance.business.template.filter.TemplateFilter
import io.github.zwieback.familyfinance.business.template.filter.TemplateFilter.Companion.TEMPLATE_FILTER
import io.github.zwieback.familyfinance.business.template.fragment.TemplateFragment
import io.github.zwieback.familyfinance.business.template.lifecycle.destroyer.TemplateFromSmsPatternsDestroyer
import io.github.zwieback.familyfinance.business.template.listener.OnTemplateClickListener
import io.github.zwieback.familyfinance.core.activity.EntityActivity
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer
import io.github.zwieback.familyfinance.core.model.Template
import io.github.zwieback.familyfinance.core.model.TemplateView
import io.github.zwieback.familyfinance.core.model.type.TemplateType

class TemplateActivity :
    EntityActivity<TemplateView, Template, TemplateFilter, TemplateFragment>(),
    OnTemplateClickListener {

    private lateinit var templateQualifier: TemplateQualifier

    override val titleStringId: Int
        get() = R.string.template_activity_title

    override val filterName: String
        get() = TEMPLATE_FILTER

    override val resultName: String
        get() = RESULT_TEMPLATE_ID

    override val fragmentTag: String
        get() = localClassName

    override val classOfRegularEntity: Class<Template>
        get() = Template::class.java

    override fun collectMenuIds(): List<Int> {
        return if (!readOnly) {
            listOf(R.menu.menu_entity_template)
        } else {
            emptyList()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_expense_template -> {
                addExpenseTemplate()
                true
            }
            R.id.action_add_income_template -> {
                addIncomeTemplate()
                true
            }
            R.id.action_add_transfer_template -> {
                addTransferTemplate()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        templateQualifier = TemplateQualifier(this, data)
    }

    override fun createDefaultFilter(): TemplateFilter {
        return TemplateFilter()
    }

    override fun createFragment(): TemplateFragment {
        return TemplateFragment.newInstance(filter)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun onEntityClick(view: View, template: TemplateView) {
        super.onEntityClick(view, template)
        if (regularSelectable) {
            return
        }
        val intent = templateQualifier.determineHelper(template)
            .getIntentToAdd(
                template.articleId,
                template.accountId,
                template.transferAccountId,
                template.ownerId,
                template.currencyId,
                template.exchangeRateId,
                template.date,
                template.value,
                template.description,
                template.url
            )
        startActivity(intent)
    }

    private fun addExpenseTemplate() {
        addTemplate(TemplateType.EXPENSE_OPERATION)
    }

    private fun addIncomeTemplate() {
        addTemplate(TemplateType.INCOME_OPERATION)
    }

    private fun addTransferTemplate() {
        addTemplate(TemplateType.TRANSFER_OPERATION)
    }

    private fun addTemplate(type: TemplateType) {
        super.addEntity()
        val intent = Intent(this, TemplateEditActivity::class.java)
            .putExtra(TemplateEditActivity.INPUT_TEMPLATE_TYPE, type.name)
        startActivity(intent)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun editEntity(template: TemplateView) {
        super.editEntity(template)
        val intent = Intent(this, TemplateEditActivity::class.java)
            .putExtra(TemplateEditActivity.INPUT_TEMPLATE_ID, template.id)
        startActivity(intent)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun createDestroyer(template: TemplateView): EntityDestroyer<Template> {
        return TemplateFromSmsPatternsDestroyer(this, data)
    }
}
