package io.github.zwieback.familyfinance.business.person.fragment

import io.github.zwieback.familyfinance.business.person.adapter.PersonAdapter
import io.github.zwieback.familyfinance.business.person.filter.PersonFilter
import io.github.zwieback.familyfinance.business.person.filter.PersonFilter.Companion.PERSON_FILTER
import io.github.zwieback.familyfinance.business.person.listener.OnPersonClickListener
import io.github.zwieback.familyfinance.core.fragment.EntityFolderFragment
import io.github.zwieback.familyfinance.core.model.PersonView
import io.github.zwieback.familyfinance.databinding.ItemPersonBinding

class PersonFragment :
    EntityFolderFragment<
            PersonView,
            PersonFilter,
            ItemPersonBinding,
            OnPersonClickListener,
            PersonAdapter
            >() {

    override fun createEntityAdapter(): PersonAdapter {
        val filter = extractFilter(PERSON_FILTER)
        return PersonAdapter(requireContext(), clickListener, data, filter)
    }

    companion object {
        fun newInstance(filter: PersonFilter) = PersonFragment().apply {
            arguments = createArguments(PERSON_FILTER, filter)
        }
    }
}
