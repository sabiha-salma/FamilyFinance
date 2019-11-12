package io.github.zwieback.familyfinance.business.person.adapter

import android.content.Context
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.adapter.EntityProvider
import io.github.zwieback.familyfinance.core.model.Person

class PersonProvider(context: Context) : EntityProvider<Person>(context) {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun provideDefaultIcon(person: Person): IIcon {
        return if (person.isFolder)
            CommunityMaterial.Icon.cmd_folder_account
        else
            CommunityMaterial.Icon.cmd_account_circle
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun provideDefaultIconColor(person: Person): Int {
        return R.color.colorPrimaryDark
    }
}
