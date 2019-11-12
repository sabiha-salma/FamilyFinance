package io.github.zwieback.familyfinance.business.template.lifecycle.destroyer

import android.content.Context
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityForceDestroyer
import io.github.zwieback.familyfinance.core.model.Template
import io.requery.Persistable
import io.requery.meta.QueryAttribute
import io.requery.reactivex.ReactiveEntityStore

internal class TemplateForceDestroyer(context: Context, data: ReactiveEntityStore<Persistable>) :
    EntityForceDestroyer<Template>(context, data) {

    override val entityClass: Class<Template>
        get() = Template::class.java

    override val idAttribute: QueryAttribute<Template, Int>
        get() = Template.ID
}
