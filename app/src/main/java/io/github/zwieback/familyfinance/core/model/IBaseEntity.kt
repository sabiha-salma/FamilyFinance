package io.github.zwieback.familyfinance.core.model

import android.os.Parcelable
import androidx.databinding.Bindable
import androidx.databinding.Observable
import io.github.zwieback.familyfinance.core.activity.EntityEditActivity
import io.github.zwieback.familyfinance.core.model.converter.LocalDateTimeConverter
import io.github.zwieback.familyfinance.core.model.restriction.BaseRestriction
import io.requery.*
import org.threeten.bp.LocalDateTime
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

    @get:Nullable
    @get:Column(name = "icon_name", length = BaseRestriction.ICON_NAME_MAX_LENGTH)
    val iconName: String?

    /**
     * Warning for workaround: for each inherited entity, the [createDate]
     * field must be filled in the [EntityEditActivity.createEntity]
     */
    @get:Bindable
    @get:Column(name = "create_date")
    @get:Convert(LocalDateTimeConverter::class)
    val createDate: LocalDateTime?

    /**
     * Warning for workaround: for each inherited entity, the [lastChangeDate]
     * field must be filled in the [EntityEditActivity.updateEntityProperties]
     */
    @get:Bindable
    @get:Column(name = "last_change_date")
    @get:Convert(LocalDateTimeConverter::class)
    val lastChangeDate: LocalDateTime?
}
