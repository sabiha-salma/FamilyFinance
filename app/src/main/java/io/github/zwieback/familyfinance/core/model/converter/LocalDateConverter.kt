package io.github.zwieback.familyfinance.core.model.converter

import io.requery.Converter
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.LocalDate
import java.sql.Date

class LocalDateConverter : Converter<LocalDate, Date> {

    override fun getMappedType(): Class<LocalDate> {
        return LocalDate::class.java
    }

    override fun getPersistedType(): Class<Date> {
        return Date::class.java
    }

    override fun getPersistedSize(): Int? {
        return null
    }

    override fun convertToPersisted(value: LocalDate?): Date? {
        return if (value == null) null else DateTimeUtils.toSqlDate(value)
    }

    override fun convertToMapped(type: Class<out LocalDate>, value: Date?): LocalDate? {
        return if (value == null) null else DateTimeUtils.toLocalDate(value)
    }
}
