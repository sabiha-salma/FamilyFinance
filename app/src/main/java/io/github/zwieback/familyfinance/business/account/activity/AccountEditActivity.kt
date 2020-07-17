package io.github.zwieback.familyfinance.business.account.activity

import android.app.Activity
import android.content.Intent
import com.johnpetitto.validator.ValidatingTextInputLayout
import com.mikepenz.iconics.view.IconicsImageView
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.account.activity.helper.AccountTypeHelper
import io.github.zwieback.familyfinance.business.account.adapter.AccountProvider
import io.github.zwieback.familyfinance.business.currency.activity.CurrencyActivity
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.ACCOUNT_CODE
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.CURRENCY_CODE
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.PERSON_CODE
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_ACCOUNT_ID
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_CURRENCY_ID
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_PERSON_ID
import io.github.zwieback.familyfinance.business.person.activity.PersonActivity
import io.github.zwieback.familyfinance.core.activity.EntityActivity
import io.github.zwieback.familyfinance.core.activity.EntityFolderActivity
import io.github.zwieback.familyfinance.core.activity.EntityFolderEditActivity
import io.github.zwieback.familyfinance.core.adapter.EntityProvider
import io.github.zwieback.familyfinance.core.model.Account
import io.github.zwieback.familyfinance.core.model.Currency
import io.github.zwieback.familyfinance.core.model.Person
import io.github.zwieback.familyfinance.core.model.type.AccountType
import io.github.zwieback.familyfinance.databinding.ActivityEditAccountBinding
import io.github.zwieback.familyfinance.util.NumberUtils.nonNullId
import io.github.zwieback.familyfinance.util.NumberUtils.stringToBigDecimal
import io.github.zwieback.familyfinance.util.NumberUtils.stringToInt
import io.reactivex.functions.Consumer
import org.threeten.bp.LocalDateTime
import java.math.BigDecimal

class AccountEditActivity : EntityFolderEditActivity<Account, ActivityEditAccountBinding>() {

    override val titleStringId: Int
        get() = R.string.account_activity_edit_title

    override val bindingLayoutId: Int
        get() = R.layout.activity_edit_account

    override val extraInputId: String
        get() = INPUT_ACCOUNT_ID

    override val extraOutputId: String
        get() = OUTPUT_ACCOUNT_ID

    override val entityClass: Class<Account>
        get() = Account::class.java

    override val layoutsForValidation: List<ValidatingTextInputLayout>
        get() {
            val layouts = mutableListOf(
                binding.parentLayout,
                binding.nameLayout,
                binding.orderCodeLayout
            )
            if (!entity.isFolder) {
                layouts.add(binding.currencyLayout)
                layouts.add(binding.ownerLayout)
                layouts.add(binding.initialBalanceLayout)
                if (!binding.number.text?.toString().isNullOrEmpty()) {
                    layouts.add(binding.numberLayout)
                }
                if (!(binding.cardNumber.text?.toString().isNullOrEmpty())) {
                    layouts.add(binding.cardNumberLayout)
                }
            }
            return layouts.toList()
        }

    override val iconView: IconicsImageView
        get() = binding.icon

    override val parentLayout: ValidatingTextInputLayout
        get() = binding.parentLayout

    override fun createProvider(): EntityProvider<Account> {
        return AccountProvider(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultIntent)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            ACCOUNT_CODE -> resultIntent?.let {
                val parentId = extractOutputId(resultIntent, RESULT_ACCOUNT_ID)
                loadParent(parentId)
            }
            CURRENCY_CODE -> resultIntent?.let {
                val currencyId = extractOutputId(resultIntent, RESULT_CURRENCY_ID)
                loadCurrency(currencyId)
            }
            PERSON_CODE -> resultIntent?.let {
                val ownerId = extractOutputId(resultIntent, RESULT_PERSON_ID)
                loadOwner(ownerId)
            }
        }
    }

    private fun onParentClick() {
        val intent = Intent(this, AccountActivity::class.java)
            .putExtra(EntityActivity.INPUT_REGULAR_SELECTABLE, false)
            .putExtra(EntityFolderActivity.INPUT_PROHIBITED_FOLDER_ID, entity.id)
        startActivityForResult(intent, ACCOUNT_CODE)
    }

    private fun onParentRemoved() {
        entity.setParent(null)
        binding.parentLayout.error = null
    }

    private fun onCurrencyClick() {
        val intent = Intent(this, CurrencyActivity::class.java)
        startActivityForResult(intent, CURRENCY_CODE)
    }

    private fun onOwnerClick() {
        val intent = Intent(this, PersonActivity::class.java)
        startActivityForResult(intent, PERSON_CODE)
    }

    private fun onActiveChange(isChecked: Boolean) {
        val accountCopy = entity.copy()
        accountCopy.setActive(isChecked)
        provider.setupIcon(binding.icon.icon, accountCopy)
    }

    private fun loadCurrency(currencyId: Int) {
        loadEntity(
            Currency::class.java,
            currencyId,
            Consumer { foundCurrency -> entity.setCurrency(foundCurrency) }
        )
    }

    private fun loadOwner(ownerId: Int) {
        loadEntity(
            Person::class.java,
            ownerId,
            Consumer { foundOwner -> entity.setOwner(foundOwner) }
        )
    }

    private fun loadParent(parentId: Int) {
        if (nonNullId(parentId)) {
            loadEntity(
                Account::class.java,
                parentId,
                Consumer { foundAccount ->
                    entity.setParent(foundAccount)
                    binding.parentLayout.error = null
                }
            )
        }
    }

    override fun createEntity() {
        val parentId = extractInputId(INPUT_PARENT_ID)
        val folder = extractInputBoolean(INPUT_IS_FOLDER)
        val account = Account()
            .setCreateDate(LocalDateTime.now())
            .setActive(true)
            .setFolder(folder)
            .setType(AccountType.UNDEFINED_ACCOUNT)
        if (!folder) {
            account.setInitialBalance(BigDecimal.ZERO)
            loadOwner(databasePrefs.personId)
            loadCurrency(databasePrefs.currencyId)
        }
        bind(account)
        loadParent(parentId)
        disableLayout(binding.parentLayout, R.string.hint_parent_disabled)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun bind(account: Account) {
        entity = account
        binding.account = account
        provider.setupIcon(binding.icon.icon, account)
        super.bind(account)
    }

    override fun setupBindings() {
        if (!entity.isFolder) {
            binding.currency.setOnClearTextListener { entity.setCurrency(null) }
            binding.owner.setOnClearTextListener { entity.setOwner(null) }
        } else {
            disableLayout(binding.ownerLayout, R.string.hint_owner_disabled)
            disableLayout(binding.currencyLayout, R.string.hint_currency_disabled)
            disableLayout(binding.initialBalanceLayout, R.string.hint_initial_balance_disabled)
            disableView(binding.accountType)
            disableLayout(binding.numberLayout, R.string.hint_account_number_disabled)
            disableLayout(binding.paymentSystemLayout, R.string.hint_payment_system_disabled)
            disableLayout(binding.cardNumberLayout, R.string.hint_card_number_disabled)
        }
        binding.icon.setOnClickListener { onSelectIconClick() }
        binding.parent.setOnClickListener { onParentClick() }
        binding.parent.setOnClearTextListener { onParentRemoved() }
        binding.currency.setOnClickListener { onCurrencyClick() }
        binding.owner.setOnClickListener { onOwnerClick() }
        binding.parentLayout.setValidator { isParentValid(it) }
        binding.accountType.setSelection(AccountTypeHelper.getAccountTypeIndex(entity))
        binding.active.setOnCheckedChangeListener { _, isChecked -> onActiveChange(isChecked) }
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun updateEntityProperties(account: Account) {
        account.setLastChangeDate(LocalDateTime.now())
        account.setActive(binding.active.isChecked)
        account.setName(binding.name.text?.toString())
        binding.orderCode.text?.toString()?.let { orderCode ->
            account.setOrderCode(stringToInt(orderCode))
        }
        if (!account.isFolder) {
            account.setInitialBalance(stringToBigDecimal(binding.initialBalance.text?.toString()))
            account.setType(AccountTypeHelper.getAccountType(binding.accountType))
            account.setNumber(binding.number.text?.toString())
            account.setPaymentSystem(binding.paymentSystem.text?.toString())
            account.setCardNumber(binding.cardNumber.text?.toString())
        }
    }

    private fun isParentValid(input: String): Boolean {
        return isParentValid(input, entity.parent as Account?, Account.`$TYPE`.name)
    }

    override fun isParentInsideItself(parentId: Int, newParentId: Int): Boolean {
        return isParentInsideItself(
            newParentId,
            Account.ID,
            Account.PARENT_ID.eq(parentId).and(Account.FOLDER.eq(true))
        )
    }

    companion object {
        const val INPUT_ACCOUNT_ID = "accountId"
        const val OUTPUT_ACCOUNT_ID = "resultAccountId"
    }
}
