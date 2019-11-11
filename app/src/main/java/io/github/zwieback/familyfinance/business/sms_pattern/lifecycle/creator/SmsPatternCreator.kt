package io.github.zwieback.familyfinance.business.sms_pattern.lifecycle.creator

import android.content.Context
import io.github.zwieback.familyfinance.core.lifecycle.creator.EntityCreator
import io.github.zwieback.familyfinance.core.model.SmsPattern
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore

class SmsPatternCreator(
    context: Context,
    data: ReactiveEntityStore<Persistable>
) : EntityCreator<SmsPattern>(context, data) {

    override fun buildEntities(): Iterable<SmsPattern> {
        return sortedSetOf(this)
    }

    override fun compare(left: SmsPattern, right: SmsPattern): Int {
        return left.id.compareTo(right.id)
    }
}
