package io.github.zwieback.familyfinance.business.sms.handler

import android.app.Notification
import android.content.Context
import io.github.zwieback.familyfinance.business.sms.common.SmsConst.SMS_NOTIFICATION_ID
import io.github.zwieback.familyfinance.business.sms.model.dto.SmsDto
import io.github.zwieback.familyfinance.business.sms_pattern.query.SmsPatternQueryBuilder
import io.github.zwieback.familyfinance.business.template.activity.helper.TemplateQualifier
import io.github.zwieback.familyfinance.core.model.SmsPatternView
import io.github.zwieback.familyfinance.core.model.TemplateView
import io.github.zwieback.familyfinance.util.DateUtils
import io.github.zwieback.familyfinance.util.NumberUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore
import org.threeten.bp.LocalDate
import java.math.BigDecimal
import java.util.regex.Pattern

class SmsHandler(
    private val context: Context,
    private val data: ReactiveEntityStore<Persistable>
) {
    private val templateQualifier: TemplateQualifier = TemplateQualifier(context, data)

    fun handleSms(smsDto: SmsDto) {
        val smsPatterns = SmsPatternQueryBuilder.create(data)
            .withSender(smsDto.sender)
            .withUseOrderByCommon()
            .build()
            .toList()

        smsPatterns
            .find { smsPattern -> doesPatternMatch(smsDto, smsPattern) }
            ?.let { smsPattern -> parseSmsAndGenerateNotification(smsDto, smsPattern) }
    }

    private fun doesPatternMatch(smsDto: SmsDto, smsPattern: SmsPatternView): Boolean {
        val pattern = Pattern.compile(smsPattern.regex, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(smsDto.body)
        return matcher.matches()
    }

    private fun parseSmsAndGenerateNotification(smsDto: SmsDto, smsPattern: SmsPatternView) {
        val pattern = Pattern.compile(smsPattern.regex, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(smsDto.body)
        if (!matcher.matches()) {
            return
        }
        val date = smsPattern.dateGroup?.let { dateGroup -> matcher.group(dateGroup) }
        val value = smsPattern.valueGroup?.let { valueGroup -> matcher.group(valueGroup) }
        val operationDate = DateUtils.bankDateToLocalDate(date)
        val operationValue = NumberUtils.bankNumberToBigDecimal(value)
        findTemplate(smsPattern, buildAndSendNotificationOnSuccess(operationDate, operationValue))
    }

    private fun findTemplate(
        smsPattern: SmsPatternView,
        onSuccess: Consumer<TemplateView>
    ) {
        data.findByKey(TemplateView::class.java, smsPattern.templateId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess)
    }

    private fun buildAndSendNotificationOnSuccess(
        operationDate: LocalDate,
        operationValue: BigDecimal?
    ): Consumer<TemplateView> {
        return Consumer { foundTemplate ->
            val notification = buildNotification(foundTemplate, operationDate, operationValue)
            sendNotification(notification)
        }
    }

    private fun buildNotification(
        template: TemplateView,
        operationDate: LocalDate,
        operationValue: BigDecimal?
    ): Notification {
        return SmsNotificationBuilder.create()
            .withContext(context)
            .withTemplateQualifier(templateQualifier)
            .withChannelId(CHANNEL_ID)
            .withRequestCode(REQUEST_CODE)
            .withTemplate(template)
            .withOperationDate(operationDate)
            .withOperationValue(operationValue)
            .build()
    }

    private fun sendNotification(notification: Notification) {
        SmsNotificationSender.create()
            .withContext(context)
            .withChannelId(CHANNEL_ID)
            .withChannelName(CHANNEL_NAME)
            .withNotificationId(SMS_NOTIFICATION_ID)
            .withNotification(notification)
            .send()
    }

    companion object {
        private const val CHANNEL_ID = "Family Finance Sms Handler Channel Id"
        private const val CHANNEL_NAME = "Family Finance Sms Handler Channel"
        private const val REQUEST_CODE = 4944
    }
}
