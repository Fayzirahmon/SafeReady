package com.swe401.safeready.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.swe401.safeready.R

/**
 * Implicit intents: Android chooses which installed app (dialler, browser,
 * YouTube, WhatsApp...) handles the action.
 */
object ImplicitIntents {

    const val EMERGENCY_NUMBER = "999"

    /** Opens the dialler with the emergency number filled in (no call permission needed). */
    fun dialEmergency(context: Context) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$EMERGENCY_NUMBER"))
        start(context, intent)
    }

    /** Opens a web page in the browser (or YouTube app for YouTube links). */
    fun openUrl(context: Context, url: String) {
        start(context, Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    /** Opens YouTube search results for an educational video topic. */
    fun searchVideo(context: Context, query: String) {
        openUrl(context, "https://www.youtube.com/results?search_query=" + Uri.encode(query))
    }

    /** Lets the user share text through any app that accepts it. */
    fun shareText(context: Context, text: String) {
        val send = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name))
            putExtra(Intent.EXTRA_TEXT, text)
        }
        start(context, Intent.createChooser(send, context.getString(R.string.share_chooser)))
    }

    private fun start(context: Context, intent: Intent) {
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, R.string.no_app_found, Toast.LENGTH_SHORT).show()
        }
    }
}
