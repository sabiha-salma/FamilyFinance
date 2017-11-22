package io.github.zwieback.familyfinance.app.lifecycle.destroyer;

import android.content.Context;
import android.util.Log;

public class DatabaseDestroyer {

    private static final String TAG = "DatabaseDestroyer";

    private final Context context;

    public DatabaseDestroyer(Context context) {
        this.context = context;
    }

    public void deleteDatabases() {
        for (String databaseName : context.databaseList()) {
            Log.d(TAG, "The '" + databaseName + "' database was deleted");
            context.deleteDatabase(databaseName);
        }
    }
}
