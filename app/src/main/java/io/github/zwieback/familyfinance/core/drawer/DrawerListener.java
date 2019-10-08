package io.github.zwieback.familyfinance.core.drawer;

import android.app.Activity;
import android.view.View;
import androidx.annotation.NonNull;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialize.util.KeyboardUtil;

public class DrawerListener implements Drawer.OnDrawerListener {

    private final Activity activity;

    public DrawerListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {
        KeyboardUtil.hideKeyboard(activity);
    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
        // stub
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        // stub
    }
}
