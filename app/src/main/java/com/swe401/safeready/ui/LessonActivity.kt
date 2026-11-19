package com.swe401.safeready.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.swe401.safeready.R
import com.swe401.safeready.adapters.TipAdapter
import com.swe401.safeready.adapters.TipAdapter.TipItem
import com.swe401.safeready.data.ContentRepository
import com.swe401.safeready.data.DisasterCategory
import com.swe401.safeready.data.ProgressStore

/** Lesson for one disaster: illustration, video, official guide and Before/During/After tips. */
class LessonActivity : AppCompatActivity() {

    private lateinit var store: ProgressStore
    private lateinit var category: DisasterCategory
    private lateinit var completeButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)

        val found = ContentRepository.getCategory(intent.getStringExtra(EXTRA_CATEGORY_ID))
        if (found == null) {
            finish()
            return
        }
        category = found
        store = ProgressStore(this)

        setSupportActionBar(findViewById<MaterialToolbar>(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Large title that collapses into the app bar, tinted with the disaster's colour
        val color = ContextCompat.getColor(this, category.colorRes)
        val collapsing = findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbar)
        collapsing.title = "${category.emoji} ${category.name}"
        collapsing.setContentScrimColor(color)
        findViewById<android.view.View>(R.id.appBar).setBackgroundColor(color)

        val image = findViewById<ImageView>(R.id.lessonImage)
        image.setImageResource(category.illustrationRes)
        image.contentDescription = getString(R.string.cd_illustration, category.name)
        findViewById<TextView>(R.id.lessonSummary).text = category.summary

        // Implicit intents: video and official guide open in other apps
        findViewById<MaterialButton>(R.id.videoButton).setOnClickListener {
            ImplicitIntents.searchVideo(this, category.videoSearch)
        }
        findViewById<MaterialButton>(R.id.guideButton).setOnClickListener {
            ImplicitIntents.openUrl(this, category.guideUrl)
        }

        val tipList = findViewById<RecyclerView>(R.id.tipList)
        tipList.layoutManager = LinearLayoutManager(this)
        tipList.adapter = TipAdapter(buildTipItems(), color)

        completeButton = findViewById(R.id.completeButton)
        completeButton.setOnClickListener {
            store.setLessonComplete(category.id)
            updateCompleteButton()
            Motion.hapticConfirm(it)
            Motion.pulse(it)
        }
        updateCompleteButton()

        // Explicit intent to the safety exercise
        findViewById<MaterialButton>(R.id.quizButton).setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
                .putExtra(QuizActivity.EXTRA_CATEGORY_ID, category.id)
            startActivity(intent)
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (!::category.isInitialized) return
        listOf(R.id.videoButton, R.id.guideButton, R.id.completeButton, R.id.quizButton)
            .forEach { Motion.springPress(findViewById(it)) }
        if (savedInstanceState == null) {
            Motion.staggerIn(listOf(findViewById(R.id.lessonSummary), findViewById(R.id.videoButton), findViewById(R.id.guideButton)), 150)
        }
    }

    private fun buildTipItems(): List<TipItem> {
        val items = mutableListOf<TipItem>()
        items.add(TipItem.Header(getString(R.string.before)))
        category.before.forEachIndexed { i, tip -> items.add(TipItem.Tip(i + 1, tip)) }
        items.add(TipItem.Header(getString(R.string.during)))
        category.during.forEachIndexed { i, tip -> items.add(TipItem.Tip(i + 1, tip)) }
        items.add(TipItem.Header(getString(R.string.after)))
        category.after.forEachIndexed { i, tip -> items.add(TipItem.Tip(i + 1, tip)) }
        return items
    }

    private fun updateCompleteButton() {
        val done = store.isLessonComplete(category.id)
        completeButton.isEnabled = !done
        completeButton.text = getString(if (done) R.string.lesson_completed else R.string.lesson_mark_complete)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object {
        const val EXTRA_CATEGORY_ID = "extra_category_id"
    }
}
