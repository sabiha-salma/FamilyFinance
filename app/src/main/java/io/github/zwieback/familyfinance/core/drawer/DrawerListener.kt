package io.github.zwieback.familyfinance.core.drawer

import android.app.Activity
import android.view.View

import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialize.util.KeyboardUtil

class DrawerListener(private val activity: Activity) : Drawer.OnDrawerListener {

    override fun onDrawerOpened(drawerView: View) {
        KeyboardUtil.hideKeyboard(activity)
    }

    override fun onDrawerClosed(drawerView: View) {
        // stub
    }

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        // stub
    }
}
