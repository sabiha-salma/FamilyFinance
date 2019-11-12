package io.github.zwieback.familyfinance.business.sms.handler

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

class SmsNotificationSender private constructor() {

    private lateinit var context: Context
    private lateinit var channelId: String
    private lateinit var channelName: String
    private lateinit var notification: Notification
    private var notificationId: Int = 0

    fun withContext(context: Context): SmsNotificationSender {
        return apply { this.context = context }
    }

    fun withChannelId(channelId: String): SmsNotificationSender {
        return apply { this.channelId = channelId }
    }

    fun withChannelName(channelName: String): SmsNotificationSender {
        return apply { this.channelName = channelName }
    }

    fun withNotificationId(notificationId: Int): SmsNotificationSender {
        return apply { this.notificationId = notificationId }
    }

    fun withNotification(notification: Notification): SmsNotificationSender {
        return apply { this.notification = notification }
    }

    fun send() {
        (context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)
            ?.let { notificationManager ->
                addWorkaroundForAndroidO(notificationManager)
                notificationManager.notify(notificationId, notification)
            }
    }

    /**
     * @see [Workaround from here](https://stackoverflow.com/a/47135605/8035065)
     * and [here](https://stackoverflow.com/a/45774186/8035065)
     */
    private fun addWorkaroundForAndroidO(notificationManager: NotificationManager) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // don't change the importance, because a higher value makes noise
            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        fun create(): SmsNotificationSender {
            return SmsNotificationSender()
        }
    }
}
