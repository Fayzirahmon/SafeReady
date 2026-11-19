package com.swe401.safeready.data

import kotlin.math.roundToInt

/**
 * Pure scoring rules for the preparedness score.
 * Kept free of Android classes so the rules stay separate from the screens.
 */
object ScoreCalculator {

    const val QUIZ_WEIGHT = 0.6f
    const val LESSON_WEIGHT = 0.2f
    const val KIT_WEIGHT = 0.2f

    /** Average of the best exercise scores. Unattempted (-1) counts as 0. */
    fun quizComponent(bestScores: List<Int>): Int {
        if (bestScores.isEmpty()) return 0
        val total = bestScores.sumOf { it.coerceIn(0, 100) }
        return (total.toFloat() / bestScores.size).roundToInt()
    }

    /** Percentage of lessons completed. */
    fun lessonComponent(completed: Int, totalLessons: Int): Int {
        if (totalLessons <= 0) return 0
        return (completed.coerceIn(0, totalLessons) * 100f / totalLessons).roundToInt()
    }

    /** Percentage of valid Go-Bag items packed. Unknown ids are ignored. */
    fun kitComponent(checkedIds: Set<String>, validIds: List<String>): Int {
        if (validIds.isEmpty()) return 0
        val done = checkedIds.count { it in validIds }
        return (done * 100f / validIds.size).roundToInt()
    }

    /** Preparedness score out of 100: 60% exercises, 20% lessons, 20% Go-Bag. */
    fun preparednessScore(quiz: Int, lesson: Int, kit: Int): Int =
        (quiz * QUIZ_WEIGHT + lesson * LESSON_WEIGHT + kit * KIT_WEIGHT).roundToInt()

    /** Exercise score as a whole percentage. */
    fun percent(correct: Int, total: Int): Int {
        if (total <= 0) return 0
        return (correct * 100f / total).roundToInt()
    }
}
