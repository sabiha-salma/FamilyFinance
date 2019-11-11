package io.github.zwieback.familyfinance.business.account.fragment

import io.github.zwieback.familyfinance.business.account.adapter.AccountAdapter
import io.github.zwieback.familyfinance.business.account.filter.AccountFilter
import io.github.zwieback.familyfinance.business.account.filter.AccountFilter.Companion.ACCOUNT_FILTER
import io.github.zwieback.familyfinance.business.account.listener.OnAccountClickListener
import io.github.zwieback.familyfinance.core.fragment.EntityFolderFragment
import io.github.zwieback.familyfinance.core.model.AccountView
import io.github.zwieback.familyfinance.databinding.ItemAccountBinding

class AccountFragment :
    EntityFolderFragment<
            AccountView,
            AccountFilter,
            ItemAccountBinding,
            OnAccountClickListener,
            AccountAdapter
            >() {

    override fun createEntityAdapter(): AccountAdapter {
        val filter = extractFilter(ACCOUNT_FILTER)
        return AccountAdapter(requireContext(), clickListener, data, filter)
    }

    companion object {
        fun newInstance(filter: AccountFilter) = AccountFragment().apply {
            arguments = createArguments(ACCOUNT_FILTER, filter)
        }
    }
}
