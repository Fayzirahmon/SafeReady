package com.swe401.safeready.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.swe401.safeready.R
import com.swe401.safeready.data.ProgressStore

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private val openNext = Runnable { openNextScreen() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Logo springs in, then the title and tagline slide up one after another
        Motion.popIn(findViewById(R.id.splashLogo), 100)
        val content = findViewById<android.view.ViewGroup>(R.id.splashContent)
        Motion.staggerIn((1 until content.childCount).map { content.getChildAt(it) }, 350, 120)

        handler.postDelayed(openNext, SPLASH_DELAY_MS)
    }

    private fun openNextScreen() {
        // Explicit intent: first-time users create a profile, others go to the main screen
        val store = ProgressStore(this)
        val intent = if (store.hasProfile()) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, ProfileActivity::class.java).putExtra(ProfileActivity.EXTRA_FIRST_RUN, true)
        }
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish() // so Back does not return to the splash screen
    }

    override fun onDestroy() {
        handler.removeCallbacks(openNext)
        super.onDestroy()
    }

    companion object {
        private const val SPLASH_DELAY_MS = 2000L
    }
}
