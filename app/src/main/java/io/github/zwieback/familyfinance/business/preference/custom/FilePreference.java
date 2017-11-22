package io.github.zwieback.familyfinance.business.preference.custom;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.preference.Preference;
import android.util.AttributeSet;

import com.nononsenseapps.filepicker.FilePickerActivity;
import com.nononsenseapps.filepicker.Utils;

import java.io.File;
import java.util.List;

import io.github.zwieback.familyfinance.core.preference.custom.ActivityResultPreference;

import static io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.FILE_CODE;

public class FilePreference extends ActivityResultPreference {

    @SuppressWarnings("unused")
    public FilePreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressWarnings("unused")
    public FilePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("unused")
    public FilePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressWarnings("unused")
    public FilePreference(Context context) {
        super(context);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        callChangeListener(getFilePath());
    }

    @Override
    protected int getRequestCode() {
        return FILE_CODE;
    }

    @Override
    protected Intent getRequestIntent() {
        return new Intent(getContext(), FilePickerActivity.class)
                .putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, true)
                .putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR)
                .putExtra(FilePickerActivity.EXTRA_START_PATH,
                        Environment.getExternalStorageDirectory().getPath());
    }

    @Override
    public void onSuccessResult(@NonNull Intent resultIntent) {
        List<Uri> files = Utils.getSelectedFilesFromResult(resultIntent);
        if (!files.isEmpty()) {
            File file = Utils.getFileForUri(files.iterator().next());
            setFilePath(file.getAbsolutePath());
            callChangeListener(file.getAbsolutePath());
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (newValue instanceof String) {
            String backupPath = (String) newValue;
            setSummary(backupPath);
            return true;
        }
        return false;
    }

    private String getFilePath() {
        return backupPrefs.getBackupPath();
    }

    private void setFilePath(String absolutePath) {
        backupPrefs.setBackupPath(absolutePath);
    }
}
