package io.github.zwieback.familyfinance.business.template.lifecycle.destroyer

import android.content.Context
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityFromDestroyer
import io.github.zwieback.familyfinance.core.model.SmsPattern
import io.github.zwieback.familyfinance.core.model.Template
import io.requery.Persistable
import io.requery.query.Condition
import io.requery.reactivex.ReactiveEntityStore

class TemplateFromSmsPatternsDestroyer(context: Context, data: ReactiveEntityStore<Persistable>) :
    EntityFromDestroyer<Template, SmsPattern>(context, data) {

    override val fromClass: Class<SmsPattern>
        get() = SmsPattern::class.java

    override val alertResourceId: Int
        get() = R.string.sms_patterns_with_template_exists

    override fun next(): EntityDestroyer<Template>? {
        return TemplateForceDestroyer(context, data)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun getWhereCondition(template: Template): Condition<*, *> {
        return SmsPattern.TEMPLATE_ID.eq(template.id)
    }
}
