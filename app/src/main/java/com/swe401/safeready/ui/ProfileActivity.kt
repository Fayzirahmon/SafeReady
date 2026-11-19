package com.swe401.safeready.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.swe401.safeready.R
import com.swe401.safeready.data.ProgressStore

class ProfileActivity : AppCompatActivity() {

    private lateinit var store: ProgressStore
    private lateinit var avatarPreview: TextView
    private var selectedAvatar = ProgressStore.DEFAULT_AVATAR
    private var firstRun = false

    private val avatars = listOf("🧑‍🎓", "👩‍🎓", "👨‍🎓", "🧑‍🚒", "👩‍🚒", "🦸", "🦸‍♀️", "🐯")
    private val forms = listOf("Form 1", "Form 2", "Form 3", "Form 4", "Form 5", "Form 6")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        store = ProgressStore(this)
        firstRun = intent.getBooleanExtra(EXTRA_FIRST_RUN, false)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title =
            getString(if (firstRun) R.string.profile_title_new else R.string.profile_title_edit)
        supportActionBar?.setDisplayHomeAsUpEnabled(!firstRun)

        avatarPreview = findViewById(R.id.avatarPreview)
        val nameLayout = findViewById<TextInputLayout>(R.id.nameLayout)
        val schoolLayout = findViewById<TextInputLayout>(R.id.schoolLayout)
        val nameInput = findViewById<TextInputEditText>(R.id.nameInput)
        val schoolInput = findViewById<TextInputEditText>(R.id.schoolInput)
        val formSpinner = findViewById<Spinner>(R.id.formSpinner)

        // Class level dropdown
        formSpinner.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, forms)

        // Fill in saved values when editing
        nameInput.setText(store.name)
        schoolInput.setText(store.school)
        formSpinner.setSelection(forms.indexOf(store.form).coerceAtLeast(0))
        selectedAvatar = store.avatar
        setupAvatarChips()

        findViewById<Button>(R.id.saveButton).setOnClickListener {
            val name = nameInput.text?.toString()?.trim().orEmpty()
            val school = schoolInput.text?.toString()?.trim().orEmpty()
            nameLayout.error = if (name.isEmpty()) getString(R.string.error_name) else null
            schoolLayout.error = if (school.isEmpty()) getString(R.string.error_school) else null
            if (name.isEmpty() || school.isEmpty()) return@setOnClickListener

            store.name = name
            store.school = school
            store.form = formSpinner.selectedItem.toString()
            store.avatar = selectedAvatar
            Toast.makeText(this, R.string.profile_saved, Toast.LENGTH_SHORT).show()

            if (firstRun) {
                // Explicit intent to the main screen after first-time setup
                startActivity(Intent(this, MainActivity::class.java))
            }
            finish()
        }
    }

    private fun setupAvatarChips() {
        val group = findViewById<ChipGroup>(R.id.avatarGroup)
        avatarPreview.text = selectedAvatar
        for (emoji in avatars) {
            val chip = Chip(this).apply {
                text = emoji
                textSize = 22f
                isCheckable = true
                isCheckedIconVisible = false
                id = android.view.View.generateViewId()
                isChecked = emoji == selectedAvatar
            }
            chip.setOnCheckedChangeListener { _, checked ->
                if (checked) {
                    selectedAvatar = emoji
                    avatarPreview.text = emoji
                }
            }
            group.addView(chip)
        }
        if (group.checkedChipId == android.view.View.NO_ID) {
            (group.getChildAt(0) as Chip).isChecked = true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object {
        const val EXTRA_FIRST_RUN = "extra_first_run"
    }
}
