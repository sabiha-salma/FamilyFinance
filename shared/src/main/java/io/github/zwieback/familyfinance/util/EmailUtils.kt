package io.github.zwieback.familyfinance.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import io.github.zwieback.familyfinance.core.R

object EmailUtils {

    private const val MAIL_TO_SCHEME = "mailto:"

    @JvmStatic
    fun sendEmail(context: Context, sendTo: String) {
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse(MAIL_TO_SCHEME + sendTo))
        val title = context.resources.getString(R.string.send_email)
        try {
            context.startActivity(Intent.createChooser(intent, title))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, R.string.no_email_clients_installed, Toast.LENGTH_SHORT).show()
        }
    }
}
