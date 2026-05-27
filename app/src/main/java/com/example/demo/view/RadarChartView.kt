package com.example.demo.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.demo.R
import kotlin.math.cos
import kotlin.math.sin

class RadarChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val density = context.resources.displayMetrics.density
    private val scaledDensity = context.resources.displayMetrics.scaledDensity

    data class Dimension(val name: String, val score: Float)

    var dimensions: List<Dimension> = listOf(
        Dimension("记忆力", 82f),
        Dimension("逻辑力", 91f),
        Dimension("计算力", 88f),
        Dimension("专注度", 78f),
        Dimension("创新力", 85f),
        Dimension("坚持度", 92f)
    )
    set(value) {
        field = value
        invalidate()
    }

    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 1f * density
        color = ContextCompat.getColor(context, R.color.border_color)
    }

    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.primary) and 0x33FFFFFF
    }

    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f * density
        color = ContextCompat.getColor(context, R.color.primary)
    }

    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        color = ContextCompat.getColor(context, R.color.text_secondary)
        textSize = 11f * scaledDensity
    }

    private val scorePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        color = ContextCompat.getColor(context, R.color.primary)
        textSize = 24f * scaledDensity
        isFakeBoldText = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val cx = width / 2f
        val cy = height / 2f - 10f * density
        val radius = minOf(cx, cy) - 40f * density
        val n = dimensions.size
        if (n < 3) return

        val angleStep = (2.0 * Math.PI / n).toFloat()
        val startAngle = (-Math.PI / 2).toFloat()

        // Draw grid (3 levels)
        for (level in 1..3) {
            val r = radius * level / 3f
            val path = Path()
            for (i in 0 until n) {
                val angle = startAngle + i * angleStep
                val x = cx + r * cos(angle.toDouble()).toFloat()
                val y = cy + r * sin(angle.toDouble()).toFloat()
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()
            canvas.drawPath(path, gridPaint)
        }

        // Draw axis lines
        for (i in 0 until n) {
            val angle = startAngle + i * angleStep
            val x = cx + radius * cos(angle.toDouble()).toFloat()
            val y = cy + radius * sin(angle.toDouble()).toFloat()
            canvas.drawLine(cx, cy, x, y, gridPaint)
        }

        // Draw data fill
        val dataPath = Path()
        for (i in 0 until n) {
            val angle = startAngle + i * angleStep
            val r = radius * dimensions[i].score / 100f
            val x = cx + r * cos(angle.toDouble()).toFloat()
            val y = cy + r * sin(angle.toDouble()).toFloat()
            if (i == 0) dataPath.moveTo(x, y) else dataPath.lineTo(x, y)
        }
        dataPath.close()
        canvas.drawPath(dataPath, fillPaint)
        canvas.drawPath(dataPath, strokePaint)

        // Draw labels
        labelPaint.textSize = 11f * scaledDensity
        for (i in 0 until n) {
            val angle = startAngle + i * angleStep
            val labelR = radius + 24f * density
            val x = cx + labelR * cos(angle.toDouble()).toFloat()
            val y = cy + labelR * sin(angle.toDouble()).toFloat() + 4f * density
            canvas.drawText(dimensions[i].name, x, y, labelPaint)
        }

        // Center score
        val avgScore = dimensions.map { it.score }.average().toInt()
        canvas.drawText("${avgScore}分", cx, cy - 4f * density, scorePaint)
        labelPaint.textSize = 10f * scaledDensity
        canvas.drawText("全球前 5%", cx, cy + 16f * density, labelPaint)
    }
}
