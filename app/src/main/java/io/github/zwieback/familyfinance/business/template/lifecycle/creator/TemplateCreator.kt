package io.github.zwieback.familyfinance.business.template.lifecycle.creator

import android.content.Context
import io.github.zwieback.familyfinance.core.lifecycle.creator.EntityCreator
import io.github.zwieback.familyfinance.core.model.Template
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore

class TemplateCreator(
    context: Context,
    data: ReactiveEntityStore<Persistable>
) : EntityCreator<Template>(context, data) {

    override fun buildEntities(): Iterable<Template> {
        return sortedSetOf(this)
    }

    override fun compare(left: Template, right: Template): Int {
        return left.id.compareTo(right.id)
    }
}
