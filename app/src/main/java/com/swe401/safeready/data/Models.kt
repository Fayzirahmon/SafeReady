package com.swe401.safeready.data

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

/** One disaster topic with its lesson content and safety exercise. */
data class DisasterCategory(
    val id: String,
    val name: String,
    val emoji: String,
    @ColorRes val colorRes: Int,
    @DrawableRes val illustrationRes: Int,
    val summary: String,
    val before: List<String>,
    val during: List<String>,
    val after: List<String>,
    val videoSearch: String,
    val guideUrl: String,
    val badgeTitle: String,
    val questions: List<Question>
)

/** A scenario-based multiple choice question. */
data class Question(
    val text: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

/** An item for the emergency Go-Bag checklist. */
data class KitItem(
    val id: String,
    val name: String,
    val note: String
)

/** An achievement badge shown on the Awards screen. */
data class Badge(
    val id: String,
    val emoji: String,
    val title: String,
    val description: String,
    val unlocked: Boolean
)
