package io.github.zwieback.familyfinance.business.account.adapter

import android.content.Context
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.account.adapter.calculator.NonOptimizedAccountBalanceCalculator
import io.github.zwieback.familyfinance.business.account.filter.AccountFilter
import io.github.zwieback.familyfinance.business.account.listener.OnAccountClickListener
import io.github.zwieback.familyfinance.business.account.query.AccountQueryBuilder
import io.github.zwieback.familyfinance.core.adapter.BindingHolder
import io.github.zwieback.familyfinance.core.adapter.EntityFolderAdapter
import io.github.zwieback.familyfinance.core.adapter.EntityProvider
import io.github.zwieback.familyfinance.core.model.AccountView
import io.github.zwieback.familyfinance.databinding.ItemAccountBinding
import io.reactivex.functions.Consumer
import io.requery.Persistable
import io.requery.query.Result
import io.requery.reactivex.ReactiveEntityStore
import java.math.BigDecimal

class AccountAdapter(
    context: Context,
    clickListener: OnAccountClickListener,
    data: ReactiveEntityStore<Persistable>,
    filter: AccountFilter
) : EntityFolderAdapter<AccountView, AccountFilter, ItemAccountBinding, OnAccountClickListener>(
    AccountView.`$TYPE`,
    context,
    clickListener,
    data,
    filter
) {

    override fun createProvider(context: Context): EntityProvider<AccountView> {
        return AccountViewProvider(context)
    }

    override fun inflate(inflater: LayoutInflater): ItemAccountBinding {
        return ItemAccountBinding.inflate(inflater)
    }

    override fun extractEntity(binding: ItemAccountBinding): AccountView {
        return binding.account as AccountView
    }

    override fun performQuery(): Result<AccountView> {
        return AccountQueryBuilder.create(data)
            .withParentId(parentId)
            .withOwnerId(filter.getOwnerId())
            .withOnlyActive(filter.isOnlyActive)
            .build()
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun onBindViewHolder(
        account: AccountView?,
        holder: BindingHolder<ItemAccountBinding>,
        position: Int
    ) {
        holder.binding.account = account
        provider.setupIcon(holder.binding.icon.icon, account!!)
        calculateAndShowBalance(account, holder)
    }

    private fun calculateAndShowBalance(
        account: AccountView,
        holder: BindingHolder<ItemAccountBinding>
    ) {
        if (account.isFolder) {
            return
        }
        val calculator = NonOptimizedAccountBalanceCalculator(data, account)
        calculator.calculateBalance(showBalance(holder))
    }

    private fun showBalance(holder: BindingHolder<ItemAccountBinding>): Consumer<BigDecimal> {
        return Consumer { balance ->
            holder.binding.balanceValue = balance
            if (balance.signum() < 0) {
                changeBalanceColorToNegative(holder)
            }
        }
    }

    private fun changeBalanceColorToNegative(holder: BindingHolder<ItemAccountBinding>) {
        val negativeBalanceColor = ContextCompat.getColor(context, R.color.colorNegativeBalance)
        holder.binding.balance.setTextColor(negativeBalanceColor)
    }
}
