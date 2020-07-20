package io.github.zwieback.familyfinance.business.sms.handler

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity
import io.github.zwieback.familyfinance.business.template.activity.helper.TemplateQualifier
import io.github.zwieback.familyfinance.constant.StringConstants.QUESTION
import io.github.zwieback.familyfinance.core.model.TemplateView
import io.github.zwieback.familyfinance.core.model.type.TemplateType
import io.github.zwieback.familyfinance.util.DateUtils
import io.github.zwieback.familyfinance.util.NumberUtils
import org.threeten.bp.LocalDate
import java.math.BigDecimal

class SmsNotificationBuilder private constructor() {

    private lateinit var context: Context
    private lateinit var templateQualifier: TemplateQualifier
    private lateinit var channelId: String

    private lateinit var template: TemplateView
    private lateinit var operationDate: LocalDate
    private var operationValue: BigDecimal? = null
    private var requestCode: Int = 0

    fun withContext(context: Context): SmsNotificationBuilder {
        return apply { this.context = context }
    }

    fun withTemplateQualifier(templateQualifier: TemplateQualifier): SmsNotificationBuilder {
        return apply { this.templateQualifier = templateQualifier }
    }

    fun withChannelId(channelId: String): SmsNotificationBuilder {
        return apply { this.channelId = channelId }
    }

    fun withRequestCode(requestCode: Int): SmsNotificationBuilder {
        return apply { this.requestCode = requestCode }
    }

    fun withTemplate(template: TemplateView): SmsNotificationBuilder {
        return apply { this.template = template }
    }

    fun withOperationDate(operationDate: LocalDate): SmsNotificationBuilder {
        return apply { this.operationDate = operationDate }
    }

    fun withOperationValue(operationValue: BigDecimal?): SmsNotificationBuilder {
        return apply { this.operationValue = operationValue }
    }

    fun build(): Notification {
        val pendingIntent = buildPendingIntent(template, operationDate, operationValue)

        val type = context.getString(determineOperationTypeRes(template))
        val date = DateUtils.localDateToString(operationDate, DateUtils.BANK_DATE_FORMATTER)
        val value = NumberUtils.bigDecimalToString(operationValue, QUESTION)
        val contentText =
            context.getString(R.string.sms_received_content, type, value, template.name, date)
        val contentTitle = context.getString(R.string.sms_received)

        // don't include the default values (sound, vibration, light),
        // because they are included in the input SMS
        val silentNotification = 0
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(contentText))
            .setContentIntent(pendingIntent)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setDefaults(silentNotification)
            .setAutoCancel(true)

        if (mayToAddOperationImmediately(template, operationDate, operationValue)) {
            val addOperationImmediatelyPendingIntent =
                buildAddOperationImmediatelyPendingIntent(template, operationDate, operationValue)

            notificationBuilder.addAction(
                NotificationCompat.Action.Builder(
                    0,
                    context.getString(R.string.action_add_immediately),
                    addOperationImmediatelyPendingIntent
                )
                    .setAllowGeneratedReplies(false)
                    .setShowsUserInterface(false)
                    .build()
            )
        }

        return notificationBuilder.build()
    }

    /**
     * @see [Solution from here](https://stackoverflow.com/a/31429210/8035065)
     */
    private fun buildPendingIntent(
        template: TemplateView,
        operationDate: LocalDate,
        operationValue: BigDecimal?
    ): PendingIntent? {
        val operationHelper = templateQualifier.determineHelper(template)
        val notificationIntent = operationHelper.getIntentToAdd(
            template.articleId, template.accountId, template.transferAccountId,
            template.ownerId, template.currencyId, template.exchangeRateId,
            operationDate, operationValue, template.description, template.url
        )
        return TaskStackBuilder.create(context)
            .addParentStack(DashboardActivity::class.java)
            .addNextIntent(notificationIntent)
            .getPendingIntent(requestCode, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun mayToAddOperationImmediately(
        template: TemplateView,
        operationDate: LocalDate,
        operationValue: BigDecimal?
    ): Boolean {
        val operationHelper = templateQualifier.determineHelper(template)
        return operationHelper.validToAddImmediately(
            template.articleId, template.accountId, template.transferAccountId,
            template.ownerId, template.currencyId, template.exchangeRateId,
            operationDate, operationValue, template.description, template.url
        )
    }

    private fun buildAddOperationImmediatelyPendingIntent(
        template: TemplateView,
        operationDate: LocalDate,
        operationValue: BigDecimal?
    ): PendingIntent? {
        val operationHelper = templateQualifier.determineHelper(template)
        val addImmediatelyIntent = operationHelper.getIntentToAddImmediately(
            template.articleId, template.accountId, template.transferAccountId,
            template.ownerId, template.currencyId, template.exchangeRateId,
            operationDate, operationValue, template.description, template.url
        )
        return PendingIntent.getService(
            context,
            ADD_OPERATION_IMMEDIATELY_REQUEST_CODE,
            addImmediatelyIntent,
            ADD_OPERATION_IMMEDIATELY_FLAGS
        )
    }

    @StringRes
    private fun determineOperationTypeRes(template: TemplateView): Int {
        return when (template.type) {
            TemplateType.EXPENSE_OPERATION -> R.string.expense_operation_type
            TemplateType.INCOME_OPERATION -> R.string.income_operation_type
            TemplateType.TRANSFER_OPERATION -> R.string.transfer_operation_type
        }
    }

    companion object {
        private const val ADD_OPERATION_IMMEDIATELY_REQUEST_CODE = 9872
        private const val ADD_OPERATION_IMMEDIATELY_FLAGS =
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_ONE_SHOT

        fun create(): SmsNotificationBuilder {
            return SmsNotificationBuilder()
        }
    }
}
