package io.github.zwieback.familyfinance.business.sms.parser

import android.content.Intent
import android.os.Build
import android.telephony.SmsMessage
import io.github.zwieback.familyfinance.business.sms.model.dto.SmsDto

class SmsParser {

    fun parseSms(pdus: Array<*>, intent: Intent): SmsDto {
        val messages = pdus.map { pdu -> createSmsMessageFromPdu(pdu as ByteArray, intent) }
        val sender = messages.first().originatingAddress ?: error("Can't extract sender from SMS")
        val body = messages.joinToString(separator = "") { message -> message.messageBody }
        return SmsDto(sender, body)
    }

    /**
     * `format` param of the [SmsMessage.createFromPdu] method is equals
     * [android.provider.Telephony.CellBroadcasts.MESSAGE_FORMAT] constant
     *
     * @return one message in array, because multipart message due to SMS max char
     */
    private fun createSmsMessageFromPdu(pdu: ByteArray, intent: Intent): SmsMessage {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val format = intent.getStringExtra("format")
            SmsMessage.createFromPdu(pdu, format)
        } else {
            SmsMessage.createFromPdu(pdu)
        }
    }
}
