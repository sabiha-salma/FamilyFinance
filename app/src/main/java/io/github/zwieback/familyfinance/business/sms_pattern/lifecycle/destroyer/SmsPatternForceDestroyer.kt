package io.github.zwieback.familyfinance.business.sms_pattern.lifecycle.destroyer

import android.content.Context
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityForceDestroyer
import io.github.zwieback.familyfinance.core.model.SmsPattern
import io.requery.Persistable
import io.requery.meta.QueryAttribute
import io.requery.reactivex.ReactiveEntityStore

class SmsPatternForceDestroyer(context: Context, data: ReactiveEntityStore<Persistable>) :
    EntityForceDestroyer<SmsPattern>(context, data) {

    override val entityClass: Class<SmsPattern>
        get() = SmsPattern::class.java

    override val idAttribute: QueryAttribute<SmsPattern, Int>
        get() = SmsPattern.ID
}
