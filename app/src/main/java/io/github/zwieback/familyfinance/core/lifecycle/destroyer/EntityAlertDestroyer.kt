package io.github.zwieback.familyfinance.core.lifecycle.destroyer

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.model.IBaseEntity
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore

abstract class EntityAlertDestroyer<E : IBaseEntity>(
    context: Context,
    data: ReactiveEntityStore<Persistable>
) : EntityDestroyer<E>(context, data) {

    @get:StringRes
    protected abstract val alertResourceId: Int

    fun showAlert(@StringRes resId: Int) {
        AlertDialog.Builder(context)
            .setTitle(android.R.string.dialog_alert_title)
            .setMessage(resId)
            .setIconAttribute(android.R.attr.alertDialogIcon)
            .setPositiveButton(R.string.button_got_it) { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
