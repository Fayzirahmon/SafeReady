package com.swe401.safeready.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.MaterialFadeThrough
import com.swe401.safeready.R
import com.swe401.safeready.data.ProgressStore
import com.swe401.safeready.ui.fragments.AwardsFragment
import com.swe401.safeready.ui.fragments.CategoriesFragment
import com.swe401.safeready.ui.fragments.HomeFragment
import com.swe401.safeready.ui.fragments.ProgressFragment

/** Main screen: app bar with options menu + bottom navigation hosting four fragments. */
class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById<MaterialToolbar>(R.id.toolbar))

        bottomNav = findViewById(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener { item ->
            Motion.hapticTick(bottomNav)
            showSection(item.itemId)
            true
        }

        if (savedInstanceState == null) {
            // Home is already the selected tab, so show its fragment directly
            showSection(R.id.nav_home)
        } else {
            updateTitle(bottomNav.selectedItemId)
        }
    }

    /** Result screen's "Back to home" reuses this activity, so jump to the Home tab. */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.getBooleanExtra(EXTRA_OPEN_HOME, false) && bottomNav.selectedItemId != R.id.nav_home) {
            selectTab(R.id.nav_home)
        }
    }

    /** Called by fragments (e.g. Home quick actions) to switch tabs. */
    fun selectTab(@IdRes itemId: Int) {
        bottomNav.selectedItemId = itemId
    }

    private fun showSection(@IdRes itemId: Int) {
        val fragment: Fragment = when (itemId) {
            R.id.nav_categories -> CategoriesFragment()
            R.id.nav_progress -> ProgressFragment()
            R.id.nav_awards -> AwardsFragment()
            else -> HomeFragment()
        }
        // Material "fade through": old tab fades out, new tab fades and scales in
        fragment.enterTransition = MaterialFadeThrough()
        fragment.exitTransition = MaterialFadeThrough()
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.fragmentContainer, fragment)
            .commit()
        updateTitle(itemId)
    }

    private fun updateTitle(@IdRes itemId: Int) {
        supportActionBar?.title = when (itemId) {
            R.id.nav_categories -> getString(R.string.nav_categories)
            R.id.nav_progress -> getString(R.string.nav_progress)
            R.id.nav_awards -> getString(R.string.nav_awards)
            else -> getString(R.string.app_name)
        }
    }

    // ---------- App bar options menu ----------

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_emergency -> {
                ImplicitIntents.dialEmergency(this)
                true
            }
            R.id.action_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                true
            }
            R.id.action_share -> {
                ImplicitIntents.shareText(this, getString(R.string.share_app_text))
                true
            }
            R.id.action_reset -> {
                confirmReset()
                true
            }
            R.id.action_about -> {
                MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.about_title)
                    .setMessage(R.string.about_message)
                    .setPositiveButton(R.string.ok, null)
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun confirmReset() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.reset_title)
            .setMessage(R.string.reset_message)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.reset) { _, _ ->
                ProgressStore(this).resetProgress()
                Toast.makeText(this, R.string.progress_reset_done, Toast.LENGTH_SHORT).show()
                showSection(bottomNav.selectedItemId) // refresh the visible fragment
            }
            .show()
    }

    companion object {
        const val EXTRA_OPEN_HOME = "extra_open_home"
    }
}
