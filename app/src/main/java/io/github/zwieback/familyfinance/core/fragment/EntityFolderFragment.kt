package io.github.zwieback.familyfinance.core.fragment

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import io.github.zwieback.familyfinance.core.adapter.EntityFolderAdapter
import io.github.zwieback.familyfinance.core.filter.EntityFolderFilter
import io.github.zwieback.familyfinance.core.listener.EntityFolderClickListener
import io.github.zwieback.familyfinance.core.model.IBaseEntityFolder
import io.github.zwieback.familyfinance.util.NumberUtils.ID_AS_NULL
import io.github.zwieback.familyfinance.util.NumberUtils.intToIntegerId
import io.github.zwieback.familyfinance.util.NumberUtils.integerToIntId

abstract class EntityFolderFragment<
        ENTITY : IBaseEntityFolder,
        FILTER : EntityFolderFilter,
        BINDING : ViewDataBinding,
        LISTENER : EntityFolderClickListener<ENTITY>,
        ADAPTER : EntityFolderAdapter<ENTITY, FILTER, BINDING, LISTENER>> :
    EntityFragment<ENTITY, FILTER, BINDING, LISTENER, ADAPTER>() {

    /**
     * Workaround to get parentId when activity recreated.
     */
    val parentId: Int?
        get() {
            val id = arguments?.getInt(PARENT_ID_ARG, ID_AS_NULL) ?: ID_AS_NULL
            return intToIntegerId(id)
        }

    companion object {
        private const val PARENT_ID_ARG = "parentId"

        /**
         * Workaround to get parentId when activity recreated.
         */
        fun <FILTER : EntityFolderFilter> createArguments(
            filterName: String,
            filter: FILTER
        ): Bundle {
            return Bundle().apply {
                putParcelable(filterName, filter)
                putInt(PARENT_ID_ARG, integerToIntId(filter.getParentId()))
            }
        }
    }
}
