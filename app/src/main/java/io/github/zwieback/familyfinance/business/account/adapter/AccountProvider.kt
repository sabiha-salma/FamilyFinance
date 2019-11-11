package io.github.zwieback.familyfinance.business.account.adapter

import android.content.Context

import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial

import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.adapter.EntityProvider
import io.github.zwieback.familyfinance.core.model.Account

class AccountProvider(context: Context) : EntityProvider<Account>(context) {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun provideDefaultIcon(account: Account): IIcon {
        return if (account.isFolder) {
            if (account.isActive) {
                CommunityMaterial.Icon.cmd_folder
            } else {
                CommunityMaterial.Icon.cmd_folder_remove
            }
        } else {
            CommunityMaterial.Icon2.cmd_wallet
        }
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun provideDefaultIconColor(account: Account): Int {
        return R.color.colorPrimaryDark
    }
}
