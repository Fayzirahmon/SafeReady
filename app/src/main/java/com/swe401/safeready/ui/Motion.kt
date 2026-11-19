package com.swe401.safeready.ui

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce

/**
 * Small motion and haptics helpers that give the app a fluid, iOS-like feel:
 * physics-based springs, count-up numbers, staggered entrances and haptic feedback.
 * Every animation is skipped when the user has turned animations off in system settings.
 */
object Motion {

    /** False when "Remove animations" / animator duration scale 0 is set (like iOS Reduce Motion). */
    fun enabled(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) ValueAnimator.areAnimatorsEnabled() else true

    private fun spring(view: View, property: DynamicAnimation.ViewProperty, damping: Float, stiffness: Float) =
        SpringAnimation(view, property).setSpring(
            SpringForce().setDampingRatio(damping).setStiffness(stiffness)
        )

    /** Cards and buttons shrink slightly when pressed and spring back when released. */
    @SuppressLint("ClickableViewAccessibility")
    fun springPress(view: View, pressedScale: Float = 0.96f) {
        if (!enabled()) return
        val scaleX = spring(view, DynamicAnimation.SCALE_X, SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY, SpringForce.STIFFNESS_MEDIUM)
        val scaleY = spring(view, DynamicAnimation.SCALE_Y, SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY, SpringForce.STIFFNESS_MEDIUM)
        view.setOnTouchListener { _, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    scaleX.animateToFinalPosition(pressedScale)
                    scaleY.animateToFinalPosition(pressedScale)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    scaleX.animateToFinalPosition(1f)
                    scaleY.animateToFinalPosition(1f)
                }
            }
            false // let the normal click listener run
        }
    }

    /** Grows a view in from small with a bouncy spring (badges, emojis, results). */
    fun popIn(view: View, delayMs: Long = 0) {
        if (!enabled()) return
        view.scaleX = 0.4f
        view.scaleY = 0.4f
        view.alpha = 0f
        view.postDelayed({
            view.animate().alpha(1f).setDuration(200).start()
            spring(view, DynamicAnimation.SCALE_X, SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY, SpringForce.STIFFNESS_LOW)
                .animateToFinalPosition(1f)
            spring(view, DynamicAnimation.SCALE_Y, SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY, SpringForce.STIFFNESS_LOW)
                .animateToFinalPosition(1f)
        }, delayMs)
    }

    /** A quick bounce used to celebrate a correct answer. */
    fun pulse(view: View) {
        if (!enabled()) return
        view.scaleX = 1.06f
        view.scaleY = 1.06f
        spring(view, DynamicAnimation.SCALE_X, SpringForce.DAMPING_RATIO_HIGH_BOUNCY, SpringForce.STIFFNESS_MEDIUM)
            .animateToFinalPosition(1f)
        spring(view, DynamicAnimation.SCALE_Y, SpringForce.DAMPING_RATIO_HIGH_BOUNCY, SpringForce.STIFFNESS_MEDIUM)
            .animateToFinalPosition(1f)
    }

    /** Side-to-side shake for a wrong answer, like a rejected passcode on iOS. */
    fun shake(view: View) {
        if (!enabled()) return
        ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0f, 18f, -18f, 12f, -12f, 6f, -6f, 0f)
            .setDuration(420)
            .start()
    }

    /** Fades and slides views up one after another. */
    fun staggerIn(views: List<View>, startDelayMs: Long = 0, stepMs: Long = 60) {
        if (!enabled()) return
        views.forEachIndexed { i, v ->
            v.alpha = 0f
            v.translationY = 40f
            v.animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(startDelayMs + i * stepMs)
                .setDuration(380)
                .setInterpolator(DecelerateInterpolator(2f))
                .start()
        }
    }

    /** Slides new content in from the right (used between quiz questions). */
    fun slideInFromRight(view: View) {
        if (!enabled()) return
        view.alpha = 0f
        view.translationX = view.width.coerceAtLeast(200) * 0.3f
        view.animate().alpha(1f).translationX(0f).setDuration(320)
            .setInterpolator(DecelerateInterpolator(2f)).start()
    }

    /** Counts a number up from 0, e.g. "0/100" to "72/100". */
    fun countUp(text: TextView, target: Int, prefix: String = "", suffix: String = "") {
        if (!enabled() || target <= 0) {
            text.text = "$prefix$target$suffix"
            return
        }
        ValueAnimator.ofInt(0, target).apply {
            duration = 900
            interpolator = DecelerateInterpolator(1.6f)
            addUpdateListener { text.text = "$prefix${it.animatedValue}$suffix" }
            start()
        }
    }

    // ---------- Haptics (respect the user's touch-feedback setting automatically) ----------

    fun hapticConfirm(view: View) {
        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) HapticFeedbackConstants.CONFIRM
        else HapticFeedbackConstants.VIRTUAL_KEY
        view.performHapticFeedback(type)
    }

    fun hapticReject(view: View) {
        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) HapticFeedbackConstants.REJECT
        else HapticFeedbackConstants.LONG_PRESS
        view.performHapticFeedback(type)
    }

    fun hapticTick(view: View) {
        view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
    }
}
