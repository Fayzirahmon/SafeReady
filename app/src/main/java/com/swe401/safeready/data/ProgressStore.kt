package com.swe401.safeready.data

import android.content.Context
import android.content.SharedPreferences

/**
 * Saves the profile and learning progress on the device using SharedPreferences,
 * and calculates the preparedness score and badges from it.
 */
class ProgressStore(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // ---------- Profile ----------

    var name: String
        get() = prefs.getString(KEY_NAME, "") ?: ""
        set(value) = prefs.edit().putString(KEY_NAME, value).apply()

    var school: String
        get() = prefs.getString(KEY_SCHOOL, "") ?: ""
        set(value) = prefs.edit().putString(KEY_SCHOOL, value).apply()

    var form: String
        get() = prefs.getString(KEY_FORM, "") ?: ""
        set(value) = prefs.edit().putString(KEY_FORM, value).apply()

    var avatar: String
        get() = prefs.getString(KEY_AVATAR, DEFAULT_AVATAR) ?: DEFAULT_AVATAR
        set(value) = prefs.edit().putString(KEY_AVATAR, value).apply()

    fun hasProfile(): Boolean = name.isNotBlank()

    // ---------- Exercise scores ----------

    /** Best exercise score for a category as a percentage, or -1 if never attempted. */
    fun bestScore(categoryId: String): Int = prefs.getInt(KEY_SCORE + categoryId, -1)

    /** Saves a new score and keeps only the best one. Returns true if it is a new best. */
    fun saveScore(categoryId: String, percent: Int): Boolean {
        if (percent > bestScore(categoryId)) {
            prefs.edit().putInt(KEY_SCORE + categoryId, percent).apply()
            return true
        }
        return false
    }

    // ---------- Lessons ----------

    fun isLessonComplete(categoryId: String): Boolean =
        prefs.getBoolean(KEY_LESSON + categoryId, false)

    fun setLessonComplete(categoryId: String) {
        prefs.edit().putBoolean(KEY_LESSON + categoryId, true).apply()
    }

    // ---------- Go-Bag checklist ----------

    fun checkedKitItems(): Set<String> =
        prefs.getStringSet(KEY_KIT, emptySet())?.toSet() ?: emptySet()

    fun setKitItemChecked(itemId: String, checked: Boolean) {
        val items = checkedKitItems().toMutableSet()
        if (checked) items.add(itemId) else items.remove(itemId)
        prefs.edit().putStringSet(KEY_KIT, items).apply()
    }

    // ---------- Score analysis ----------

    /** Average of the best exercise scores (unattempted counts as 0). */
    fun quizComponent(): Int =
        ScoreCalculator.quizComponent(ContentRepository.categories.map { bestScore(it.id) })

    /** Percentage of lessons marked as learned. */
    fun lessonComponent(): Int {
        val categories = ContentRepository.categories
        return ScoreCalculator.lessonComponent(
            categories.count { isLessonComplete(it.id) }, categories.size
        )
    }

    /** Percentage of Go-Bag items packed. */
    fun kitComponent(): Int =
        ScoreCalculator.kitComponent(checkedKitItems(), ContentRepository.kitItems.map { it.id })

    /** Preparedness score out of 100: 60% exercises, 20% lessons, 20% Go-Bag. */
    fun preparednessScore(): Int =
        ScoreCalculator.preparednessScore(quizComponent(), lessonComponent(), kitComponent())

    // ---------- Badges ----------

    fun badges(): List<Badge> {
        val categories = ContentRepository.categories
        val list = mutableListOf<Badge>()

        list.add(
            Badge(
                "first_lesson", "📘", "First Steps",
                "Learn your first lesson",
                categories.any { isLessonComplete(it.id) }
            )
        )
        for (category in categories) {
            list.add(
                Badge(
                    "master_" + category.id, category.emoji, category.badgeTitle,
                    "Score 80% or more in the ${category.name} exercise",
                    bestScore(category.id) >= MASTERY_SCORE
                )
            )
        }
        list.add(
            Badge(
                "perfect", "💯", "Perfect Score",
                "Get 100% in any exercise",
                categories.any { bestScore(it.id) == 100 }
            )
        )
        list.add(
            Badge(
                "bookworm", "📚", "Knowledge Seeker",
                "Learn every lesson",
                lessonComponent() == 100
            )
        )
        list.add(
            Badge(
                "kit", "🎒", "Go-Bag Ready",
                "Pack every item on the checklist",
                kitComponent() == 100
            )
        )
        list.add(
            Badge(
                CHAMPION_ID, "🏆", "Safety Champion",
                "Reach a preparedness score of $CHAMPION_SCORE",
                isSafetyChampion()
            )
        )
        return list
    }

    fun unlockedBadgeIds(): Set<String> = badges().filter { it.unlocked }.map { it.id }.toSet()

    fun isSafetyChampion(): Boolean = preparednessScore() >= CHAMPION_SCORE

    /** Clears learning progress but keeps the profile. */
    fun resetProgress() {
        val editor = prefs.edit()
        for (category in ContentRepository.categories) {
            editor.remove(KEY_SCORE + category.id)
            editor.remove(KEY_LESSON + category.id)
        }
        editor.remove(KEY_KIT)
        editor.apply()
    }

    companion object {
        private const val PREFS_NAME = "safeready_prefs"
        private const val KEY_NAME = "profile_name"
        private const val KEY_SCHOOL = "profile_school"
        private const val KEY_FORM = "profile_form"
        private const val KEY_AVATAR = "profile_avatar"
        private const val KEY_SCORE = "score_"
        private const val KEY_LESSON = "lesson_"
        private const val KEY_KIT = "kit_items"

        const val DEFAULT_AVATAR = "🧑‍🎓"
        const val MASTERY_SCORE = 80
        const val CHAMPION_SCORE = 80
        const val CHAMPION_ID = "champion"

        /** Readiness level name for a score, used on the Progress screen. */
        fun levelFor(score: Int): String = when {
            score < 0 -> "Not started"
            score < 40 -> "Beginner"
            score < 60 -> "Aware"
            score < 80 -> "Prepared"
            else -> "Expert"
        }
    }
}
