package io.github.zwieback.familyfinance.app;

import android.os.StrictMode;
import android.support.annotation.NonNull;

import com.jakewharton.threetenabp.AndroidThreeTen;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import io.github.zwieback.familyfinance.R;
import io.github.zwieback.familyfinance.app.info.DeveloperInfo;
import io.github.zwieback.familyfinance.app.lifecycle.creator.DatabaseTableCreator;
import io.github.zwieback.familyfinance.core.model.Models;
import io.requery.android.sqlite.DatabaseProvider;
import io.requery.android.sqlitex.SqlitexDatabaseSource;

import static org.acra.ReportField.ANDROID_VERSION;
import static org.acra.ReportField.APP_VERSION_CODE;
import static org.acra.ReportField.APP_VERSION_NAME;
import static org.acra.ReportField.BRAND;
import static org.acra.ReportField.BUILD;
import static org.acra.ReportField.CUSTOM_DATA;
import static org.acra.ReportField.DISPLAY;
import static org.acra.ReportField.LOGCAT;
import static org.acra.ReportField.PHONE_MODEL;
import static org.acra.ReportField.PRODUCT;
import static org.acra.ReportField.SHARED_PREFERENCES;
import static org.acra.ReportField.STACK_TRACE;
import static org.acra.ReportField.USER_COMMENT;

@ReportsCrashes(
        mode = ReportingInteractionMode.DIALOG,
        mailTo = DeveloperInfo.EMAIL,
        resToastText = R.string.crash_toast_text,
        resDialogText = R.string.crash_dialog_text,
        resDialogTitle = R.string.crash_dialog_title,
        resDialogCommentPrompt = R.string.crash_dialog_comment_prompt,
        resDialogOkToast = R.string.crash_dialog_ok_toast,
        resDialogTheme = R.style.AppTheme_Dialog,
        additionalSharedPreferences = {"database_prefs", "backup_prefs"},
        customReportContent = {APP_VERSION_CODE, APP_VERSION_NAME, PHONE_MODEL, ANDROID_VERSION,
                BUILD, BRAND, PRODUCT, CUSTOM_DATA, STACK_TRACE, DISPLAY, USER_COMMENT, LOGCAT,
                SHARED_PREFERENCES}
)
public class FamilyFinanceApplication extends AbstractApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        StrictMode.enableDefaults();
        // The following line triggers the initialization of ACRA
        ACRA.init(this);
        if (isNewApp()) {
            createDatabase();
        }
    }

    @NonNull
    @Override
    protected DatabaseProvider buildDatabaseProvider() {
        // override onUpgrade to handle migrating to a new version
        DatabaseProvider source = new SqlitexDatabaseSource(this, Models.DEFAULT, DB_VERSION);
        createViews(source.getConfiguration());
        return source;
    }

    private void createDatabase() {
        new DatabaseTableCreator(this, getData()).createTables();
    }

    private boolean isNewApp() {
        return databaseList().length == 0;
    }
}
