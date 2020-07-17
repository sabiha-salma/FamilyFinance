package io.github.zwieback.familyfinance.core.model.converter

import io.requery.Converter
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.LocalDateTime
import java.sql.Timestamp

class LocalDateTimeConverter : Converter<LocalDateTime, Timestamp> {

    override fun getMappedType(): Class<LocalDateTime> {
        return LocalDateTime::class.java
    }

    override fun getPersistedType(): Class<Timestamp> {
        return Timestamp::class.java
    }

    override fun getPersistedSize(): Int? {
        return null
    }

    override fun convertToPersisted(value: LocalDateTime?): Timestamp? {
        return if (value == null) null else DateTimeUtils.toSqlTimestamp(value)
    }

    override fun convertToMapped(
        type: Class<out LocalDateTime>,
        value: Timestamp?
    ): LocalDateTime? {
        return if (value == null) null else DateTimeUtils.toLocalDateTime(value)
    }
}
