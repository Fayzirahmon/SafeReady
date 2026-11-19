package com.swe401.safeready.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.core.content.ContextCompat
import com.swe401.safeready.R

/** A small custom-drawn bar chart (0-100) used on the Progress screen. */
class BarChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var labels: List<String> = emptyList()
    private var values: List<Int> = emptyList()
    private var colors: List<Int> = emptyList()

    private val density = resources.displayMetrics.density
    private val barPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.divider)
        strokeWidth = density
    }
    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.text_secondary)
        textSize = 11 * resources.displayMetrics.scaledDensity
        textAlign = Paint.Align.CENTER
    }
    private val valuePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.text_primary)
        textSize = 12 * resources.displayMetrics.scaledDensity
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }
    private val rect = RectF()
    private var growth = 1f // 0..1, animates the bars growing up

    fun setData(labels: List<String>, values: List<Int>, colors: List<Int>) {
        this.labels = labels
        this.values = values
        this.colors = colors
        contentDescription = labels.zip(values).joinToString(", ") { "${it.first} ${it.second} percent" }
        if (Motion.enabled()) {
            // Bars grow from the baseline with a slight overshoot
            ValueAnimator.ofFloat(0f, 1f).apply {
                duration = 700
                interpolator = OvershootInterpolator(1.2f)
                addUpdateListener {
                    growth = it.animatedValue as Float
                    invalidate()
                }
                start()
            }
        } else {
            growth = 1f
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (values.isEmpty()) return

        val left = paddingLeft.toFloat()
        val right = (width - paddingRight).toFloat()
        val top = paddingTop + valuePaint.textSize + 4 * density
        val bottom = height - paddingBottom - labelPaint.textSize - 8 * density
        val chartHeight = bottom - top

        // Horizontal grid lines at 0, 50 and 100
        for (step in 0..2) {
            val y = bottom - chartHeight * step / 2f
            canvas.drawLine(left, y, right, y, gridPaint)
        }

        val slot = (right - left) / values.size
        val barWidth = slot * 0.55f
        values.forEachIndexed { i, value ->
            val centerX = left + slot * i + slot / 2
            val barTop = bottom - chartHeight * value.coerceIn(0, 100) / 100f * growth
            rect.set(centerX - barWidth / 2, barTop, centerX + barWidth / 2, bottom)
            barPaint.color = colors.getOrElse(i) { Color.GRAY }
            canvas.drawRoundRect(rect, 6 * density, 6 * density, barPaint)
            canvas.drawText("${(value * growth.coerceAtMost(1f)).toInt()}", centerX, barTop - 4 * density, valuePaint)
            canvas.drawText(labels.getOrElse(i) { "" }, centerX, height - paddingBottom - 2 * density, labelPaint)
        }
    }
}
