package io.github.zwieback.familyfinance.extension

import android.content.Intent
import org.threeten.bp.LocalDate

fun Intent.getLocalDateExtra(name: String): LocalDate {
    return (this.getSerializableExtra(name) as? LocalDate) ?: LocalDate.now()
}
