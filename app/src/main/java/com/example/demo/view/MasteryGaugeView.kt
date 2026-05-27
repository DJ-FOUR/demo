package com.example.demo.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.demo.R

class MasteryGaugeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val density = context.resources.displayMetrics.density
    private val scaledDensity = context.resources.displayMetrics.scaledDensity

    var progress: Float = 68f
        set(value) {
            field = value.coerceIn(0f, 100f)
            invalidate()
        }

    private val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 14f * density
        strokeCap = Paint.Cap.ROUND
        color = ContextCompat.getColor(context, R.color.chip_background)
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 14f * density
        strokeCap = Paint.Cap.ROUND
        color = ContextCompat.getColor(context, R.color.primary)
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        color = ContextCompat.getColor(context, R.color.on_surface)
        textSize = 36f * scaledDensity
        isFakeBoldText = true
    }

    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        color = ContextCompat.getColor(context, R.color.text_secondary)
        textSize = 12f * scaledDensity
    }

    private val oval = RectF()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val strokeOffset = trackPaint.strokeWidth / 2
        oval.set(
            strokeOffset,
            strokeOffset,
            width - strokeOffset,
            height - strokeOffset
        )

        canvas.drawArc(oval, 135f, 270f, false, trackPaint)
        canvas.drawArc(oval, 135f, 270f * progress / 100f, false, progressPaint)

        val cx = width / 2f
        val cy = height / 2f
        canvas.drawText("${progress.toInt()}%", cx, cy - 4f * density, textPaint)
        canvas.drawText("今日掌握深度", cx, cy + 18f * density, labelPaint)
    }
}
