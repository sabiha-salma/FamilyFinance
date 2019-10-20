package io.github.zwieback.familyfinance.core.model

import androidx.databinding.Bindable
import io.github.zwieback.familyfinance.core.model.restriction.PersonRestriction
import io.requery.*

@Entity(propertyNameStyle = PropertyNameStyle.FLUENT_BEAN)
@Table(name = "person")
interface IPerson : IBaseEntityFolder {

    @get:Bindable
    @get:Nullable
    @get:ForeignKey
    @get:OneToOne(mappedBy = "id", cascade = [CascadeAction.NONE])
    @get:Index("idx_parent_person_id")
    @get:Column(name = "parent_id")
    val parent: IPerson?

    @get:Bindable
    @get:Column(nullable = false, length = PersonRestriction.NAME_MAX_LENGTH)
    val name: String

    @get:Bindable
    @get:Column(name = "order_code", nullable = false)
    val orderCode: Int
}
