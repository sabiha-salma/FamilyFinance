package io.github.zwieback.familyfinance.business.currency.activity

import com.johnpetitto.validator.ValidatingTextInputLayout
import com.mikepenz.iconics.view.IconicsImageView
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.currency.adapter.CurrencyProvider
import io.github.zwieback.familyfinance.core.activity.EntityEditActivity
import io.github.zwieback.familyfinance.core.adapter.EntityProvider
import io.github.zwieback.familyfinance.core.model.Currency
import io.github.zwieback.familyfinance.databinding.ActivityEditCurrencyBinding

class CurrencyEditActivity : EntityEditActivity<Currency, ActivityEditCurrencyBinding>() {

    override val titleStringId: Int
        get() = R.string.currency_activity_edit_title

    override val bindingLayoutId: Int
        get() = R.layout.activity_edit_currency

    override val extraInputId: String
        get() = INPUT_CURRENCY_ID

    override val extraOutputId: String
        get() = OUTPUT_CURRENCY_ID

    override val entityClass: Class<Currency>
        get() = Currency::class.java

    override val layoutsForValidation: List<ValidatingTextInputLayout>
        get() = listOf(binding.nameLayout, binding.descriptionLayout)

    override val iconView: IconicsImageView
        get() = binding.icon

    override fun createProvider(): EntityProvider<Currency> {
        return CurrencyProvider(this)
    }

    override fun createEntity() {
        bind(Currency())
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun bind(currency: Currency) {
        entity = currency
        binding.currency = currency
        provider.setupIcon(binding.icon.icon, currency)
        super.bind(currency)
    }

    override fun setupBindings() {
        binding.icon.setOnClickListener { onSelectIconClick() }
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun updateEntityProperties(currency: Currency) {
        currency.setName(binding.name.text?.toString())
        currency.setDescription(binding.description.text?.toString())
    }

    companion object {
        const val INPUT_CURRENCY_ID = "currencyId"
        const val OUTPUT_CURRENCY_ID = "resultCurrencyId"
    }
}
