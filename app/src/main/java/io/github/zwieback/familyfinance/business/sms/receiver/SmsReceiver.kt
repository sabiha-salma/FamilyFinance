package io.github.zwieback.familyfinance.business.sms.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import io.github.zwieback.familyfinance.app.FamilyFinanceApplication
import io.github.zwieback.familyfinance.business.sms.handler.SmsHandler
import io.github.zwieback.familyfinance.business.sms.model.dto.SmsDto
import io.github.zwieback.familyfinance.business.sms.parser.SmsParser

class SmsReceiver : BroadcastReceiver() {

    private val smsParser: SmsParser = SmsParser()

    override fun onReceive(context: Context, intent: Intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION == intent.action) {
            val smsDto = extractSmsDto(intent) ?: return
            val application = extractApplication(context)
            application?.let {
                val handler = SmsHandler(context, application.data)
                handler.handleSms(smsDto)
            }
        }
    }

    /**
     * "pdus" is an Object[] of byte[]s containing the PDUs that make up
     * the message.
     *
     * See comments for the [Telephony.Sms.Intents.SMS_RECEIVED_ACTION]
     */
    private fun extractSmsDto(intent: Intent): SmsDto? {
        val bundle = intent.extras ?: return null
        val pdus = bundle.get("pdus") as Array<*>?
        return if (pdus.isNullOrEmpty()) {
            null
        } else {
            smsParser.parseSms(pdus, intent)
        }
    }

    private fun extractApplication(context: Context): FamilyFinanceApplication? {
        return context.applicationContext as? FamilyFinanceApplication
    }
}
