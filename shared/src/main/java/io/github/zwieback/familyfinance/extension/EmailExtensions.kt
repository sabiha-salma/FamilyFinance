package io.github.zwieback.familyfinance.extension

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import io.github.zwieback.familyfinance.core.R

private const val MAIL_TO_SCHEME = "mailto:"

fun Context.sendEmail(sendTo: String) {
    val title = this.resources.getString(R.string.send_email)
    val intent = Intent(Intent.ACTION_SENDTO, Uri.parse(MAIL_TO_SCHEME + sendTo))
    try {
        if (intent.resolveActivity(this.packageManager) != null) {
            this.startActivity(intent)
        } else {
            this.startActivity(Intent.createChooser(intent, title))
        }
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, R.string.no_email_clients_installed, Toast.LENGTH_SHORT).show()
    }
}
