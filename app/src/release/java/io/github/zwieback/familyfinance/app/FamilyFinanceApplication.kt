package io.github.zwieback.familyfinance.app

import android.content.Context
import io.github.zwieback.familyfinance.BuildConfig
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.app.info.DeveloperInfo
import io.github.zwieback.familyfinance.app.lifecycle.creator.DatabaseTableCreator
import io.github.zwieback.familyfinance.core.model.Models
import org.acra.ACRA
import org.acra.ReportField.*
import org.acra.annotation.AcraCore
import org.acra.annotation.AcraDialog
import org.acra.annotation.AcraMailSender

@AcraCore(
    reportContent = [
        APP_VERSION_CODE,
        APP_VERSION_NAME,
        PHONE_MODEL,
        ANDROID_VERSION,
        BUILD,
        BRAND,
        PRODUCT,
        CUSTOM_DATA,
        STACK_TRACE,
        DISPLAY,
        USER_COMMENT,
        LOGCAT,
        SHARED_PREFERENCES
    ],
    additionalSharedPreferences = ["database_prefs", "backup_prefs"],
    buildConfigClass = BuildConfig::class,
    stopServicesOnCrash = true,
    resReportSendSuccessToast = R.string.crash_dialog_report_send_success_toast,
    resReportSendFailureToast = R.string.crash_dialog_report_send_failure_toast
)
@AcraMailSender(mailTo = DeveloperInfo.EMAIL)
@AcraDialog(
    resCommentPrompt = R.string.crash_dialog_comment_prompt,
    resText = R.string.crash_dialog_text,
    resTitle = R.string.crash_dialog_title,
    resTheme = R.style.AppTheme_Dialog
)
class FamilyFinanceApplication : AbstractApplication() {

    override fun onCreate() {
        super.onCreate()
        if (isNewApp()) {
            createDatabase()
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        // The following line triggers the initialization of ACRA
        ACRA.init(this)
    }

    private fun createDatabase() {
        DatabaseTableCreator(this, data).createTables()
    }

    private fun isNewApp(): Boolean {
        return databaseList().isEmpty()
    }
}
