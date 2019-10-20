package io.github.zwieback.familyfinance.core.model

import io.requery.Column
import io.requery.Superclass

@Superclass
interface IBaseEntityFolder : IBaseEntity {

    @get:Column(name = "is_folder", nullable = false)
    val isFolder: Boolean
}
