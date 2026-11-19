package com.swe401.safeready.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.swe401.safeready.R
import com.swe401.safeready.adapters.CategoryAdapter
import com.swe401.safeready.data.ContentRepository
import com.swe401.safeready.data.ProgressStore
import kotlin.math.roundToInt

/** Shows the exercise score, stars and any newly unlocked badges. */
class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val category = ContentRepository.getCategory(intent.getStringExtra(EXTRA_CATEGORY_ID))
        if (category == null) {
            finish()
            return
        }
        val correct = intent.getIntExtra(EXTRA_CORRECT, 0)
        val total = intent.getIntExtra(EXTRA_TOTAL, 1).coerceAtLeast(1)
        val percent = (correct * 100f / total).roundToInt()

        val store = ProgressStore(this)
        // Save once only (not again after a screen rotation) and find new badges
        val newBadges = if (savedInstanceState == null) {
            val before = store.unlockedBadgeIds()
            store.saveScore(category.id, percent)
            store.badges().filter { it.unlocked && it.id !in before }
        } else {
            emptyList()
        }

        val (emoji, title, message) = when {
            percent >= 80 -> Triple("🎉", "Excellent!", "You really know how to stay safe during a ${category.name.lowercase()}.")
            percent >= 60 -> Triple("👍", "Good job!", "You are well prepared. Review the tips you missed to become an expert.")
            percent >= 40 -> Triple("💪", "Keep going!", "Read the lesson again and retry to improve your score.")
            else -> Triple("📖", "Let's review", "Go back to the lesson, then try the exercise again.")
        }

        findViewById<TextView>(R.id.resultEmoji).text = emoji
        findViewById<TextView>(R.id.resultTitle).text = title
        findViewById<TextView>(R.id.resultCategory).text =
            "${category.emoji} ${category.name} exercise · $correct of $total correct"
        Motion.countUp(findViewById(R.id.resultScore), percent, suffix = "%")
        findViewById<TextView>(R.id.resultStars).text = CategoryAdapter.starsFor(percent)
        findViewById<TextView>(R.id.resultMessage).text =
            message + "\nBest score: ${store.bestScore(category.id)}% · Preparedness score: ${store.preparednessScore()}/100"

        val ring = findViewById<CircularProgressIndicator>(R.id.resultRing)
        ring.setIndicatorColor(ContextCompat.getColor(this, category.colorRes))
        ring.setProgressCompat(percent, true)

        // Celebrate: emoji springs in, stars pop after the score has counted up
        Motion.popIn(findViewById(R.id.resultEmoji), 100)
        Motion.popIn(findViewById(R.id.resultStars), 900)
        Motion.staggerIn(listOf(findViewById(R.id.retryButton), findViewById(R.id.shareButton), findViewById(R.id.homeButton)), 400)
        listOf(R.id.retryButton, R.id.shareButton, R.id.homeButton).forEach { Motion.springPress(findViewById(it)) }
        if (percent >= 80 && savedInstanceState == null) Motion.hapticConfirm(findViewById(R.id.resultEmoji))

        if (newBadges.isNotEmpty()) {
            findViewById<View>(R.id.newBadgeCard).visibility = View.VISIBLE
            Motion.popIn(findViewById(R.id.newBadgeCard), 1100)
            findViewById<TextView>(R.id.newBadgeText).text =
                newBadges.joinToString("\n") { "${it.emoji}  ${it.title}" }
        }

        findViewById<MaterialButton>(R.id.retryButton).setOnClickListener {
            startActivity(
                Intent(this, QuizActivity::class.java)
                    .putExtra(QuizActivity.EXTRA_CATEGORY_ID, category.id)
            )
            finish()
        }
        findViewById<MaterialButton>(R.id.shareButton).setOnClickListener {
            ImplicitIntents.shareText(
                this,
                "I scored $percent% in the ${category.name} safety exercise on SafeReady! " +
                        "${CategoryAdapter.starsFor(percent)} Do you know what to do in a ${category.name.lowercase()}?"
            )
        }
        findViewById<MaterialButton>(R.id.homeButton).setOnClickListener {
            // Return to the existing main screen instead of stacking a new one
            val intent = Intent(this, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .putExtra(MainActivity.EXTRA_OPEN_HOME, true)
            startActivity(intent)
            finish()
        }
    }

    companion object {
        const val EXTRA_CATEGORY_ID = "extra_category_id"
        const val EXTRA_CORRECT = "extra_correct"
        const val EXTRA_TOTAL = "extra_total"
    }
}
