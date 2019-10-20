package io.github.zwieback.familyfinance.core.model

import android.os.Parcelable
import androidx.databinding.Observable
import io.github.zwieback.familyfinance.core.model.restriction.BaseRestriction
import io.requery.*
import java.io.Serializable

@Superclass
interface IBaseEntity :
    Observable,
    Persistable,
    Parcelable,
    Serializable {

    @get:Key
    @get:Generated
    val id: Int

    @get:Column(name = "icon_name", length = BaseRestriction.ICON_NAME_MAX_LENGTH)
    val iconName: String?
}
