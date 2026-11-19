package com.swe401.safeready.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.swe401.safeready.R
import com.swe401.safeready.data.ContentRepository
import com.swe401.safeready.data.DisasterCategory

/** Interactive safety exercise: scenario questions with instant feedback. */
class QuizActivity : AppCompatActivity() {

    private lateinit var category: DisasterCategory
    private lateinit var optionButtons: List<MaterialButton>
    private lateinit var questionText: TextView
    private lateinit var questionCounter: TextView
    private lateinit var progress: LinearProgressIndicator
    private lateinit var feedbackCard: MaterialCardView
    private lateinit var feedbackTitle: TextView
    private lateinit var feedbackText: TextView
    private lateinit var nextButton: MaterialButton

    private var index = 0
    private var correctCount = 0
    private var answered = false
    private var lastChoice = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val found = ContentRepository.getCategory(intent.getStringExtra(EXTRA_CATEGORY_ID))
        if (found == null) {
            finish()
            return
        }
        category = found

        setSupportActionBar(findViewById<MaterialToolbar>(R.id.toolbar))
        supportActionBar?.title = "${category.name} exercise"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        questionText = findViewById(R.id.questionText)
        questionCounter = findViewById(R.id.questionCounter)
        progress = findViewById(R.id.quizProgress)
        feedbackCard = findViewById(R.id.feedbackCard)
        feedbackTitle = findViewById(R.id.feedbackTitle)
        feedbackText = findViewById(R.id.feedbackText)
        nextButton = findViewById(R.id.nextButton)
        optionButtons = listOf(
            findViewById(R.id.option0),
            findViewById(R.id.option1),
            findViewById(R.id.option2),
            findViewById(R.id.option3)
        )

        progress.max = category.questions.size
        optionButtons.forEachIndexed { i, button ->
            Motion.springPress(button, 0.97f)
            button.setOnClickListener { onAnswer(i) }
        }
        Motion.springPress(nextButton)
        nextButton.setOnClickListener { nextQuestion() }

        // Keep progress across screen rotation
        if (savedInstanceState != null) {
            index = savedInstanceState.getInt(STATE_INDEX)
            correctCount = savedInstanceState.getInt(STATE_CORRECT)
        }
        showQuestion()
        val savedChoice = savedInstanceState?.getInt(STATE_CHOICE, -1) ?: -1
        if (savedChoice >= 0) onAnswer(savedChoice, countScore = false)

        // Ask before leaving in the middle of an exercise
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() = confirmLeave()
        })
    }

    private fun showQuestion() {
        val question = category.questions[index]
        answered = false
        questionCounter.text = "Question ${index + 1} of ${category.questions.size}"
        progress.setProgressCompat(index + 1, true)
        questionText.text = question.text
        // New question slides in from the right, options follow one by one
        Motion.slideInFromRight(questionText.parent as View)
        Motion.staggerIn(optionButtons, 80, 50)

        val normalStroke = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.divider))
        optionButtons.forEachIndexed { i, button ->
            button.text = "${'A' + i}.  ${question.options[i]}"
            button.isEnabled = true
            button.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.surface))
            button.strokeColor = normalStroke
        }
        feedbackCard.visibility = View.GONE
        nextButton.visibility = View.GONE
    }

    private fun onAnswer(choice: Int, countScore: Boolean = true) {
        if (answered) return
        answered = true
        lastChoice = choice
        val question = category.questions[index]
        val correct = choice == question.correctIndex
        if (correct && countScore) correctCount++

        // Colour the correct answer green and a wrong choice red
        val green = ContextCompat.getColor(this, R.color.success_container)
        val red = ContextCompat.getColor(this, R.color.error_container)
        optionButtons.forEachIndexed { i, button ->
            button.isEnabled = false
            when (i) {
                question.correctIndex -> {
                    button.backgroundTintList = ColorStateList.valueOf(green)
                    button.strokeColor = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.success))
                }
                choice -> {
                    button.backgroundTintList = ColorStateList.valueOf(red)
                    button.strokeColor = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.emergency))
                }
            }
        }

        feedbackCard.setCardBackgroundColor(if (correct) green else red)
        feedbackTitle.text = getString(if (correct) R.string.quiz_correct else R.string.quiz_wrong)
        feedbackTitle.setTextColor(
            ContextCompat.getColor(this, if (correct) R.color.success else R.color.emergency)
        )
        feedbackText.text = question.explanation
        feedbackCard.visibility = View.VISIBLE
        Motion.staggerIn(listOf(feedbackCard))

        if (countScore) {
            // Haptic + motion feedback: bounce for correct, shake for wrong
            if (correct) {
                Motion.hapticConfirm(optionButtons[choice])
                Motion.pulse(optionButtons[choice])
            } else {
                Motion.hapticReject(optionButtons[choice])
                Motion.shake(optionButtons[choice])
            }
        }

        val isLast = index == category.questions.size - 1
        nextButton.text = getString(if (isLast) R.string.quiz_finish else R.string.quiz_next)
        nextButton.visibility = View.VISIBLE
        Motion.staggerIn(listOf(nextButton), 120)
    }

    private fun nextQuestion() {
        if (index < category.questions.size - 1) {
            index++
            showQuestion()
        } else {
            // Explicit intent with the result data
            val intent = Intent(this, ResultActivity::class.java)
                .putExtra(ResultActivity.EXTRA_CATEGORY_ID, category.id)
                .putExtra(ResultActivity.EXTRA_CORRECT, correctCount)
                .putExtra(ResultActivity.EXTRA_TOTAL, category.questions.size)
            startActivity(intent)
            finish()
        }
    }

    private fun confirmLeave() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.quiz_leave_title)
            .setMessage(R.string.quiz_leave_message)
            .setNegativeButton(R.string.quiz_stay, null)
            .setPositiveButton(R.string.quiz_leave) { _, _ -> finish() }
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        confirmLeave()
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(STATE_INDEX, index)
        outState.putInt(STATE_CORRECT, correctCount)
        outState.putInt(STATE_CHOICE, if (answered) lastChoice else -1)
    }

    companion object {
        const val EXTRA_CATEGORY_ID = "extra_category_id"
        private const val STATE_INDEX = "state_index"
        private const val STATE_CORRECT = "state_correct"
        private const val STATE_CHOICE = "state_choice"
    }
}
