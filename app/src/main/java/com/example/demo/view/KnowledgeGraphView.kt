package com.example.demo.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.demo.R
import com.example.demo.model.ConceptNode

class KnowledgeGraphView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val density = context.resources.displayMetrics.density
    private val scaledDensity = context.resources.displayMetrics.scaledDensity

    var nodes: List<ConceptNode> = listOf(
        ConceptNode("矩阵运算", 85, 0.3f, 0.2f),
        ConceptNode("行列式", 72, 0.7f, 0.2f),
        ConceptNode("特征值", 90, 0.5f, 0.5f),
        ConceptNode("线性变换", 60, 0.2f, 0.75f),
        ConceptNode("奇异值分解", -1, 0.8f, 0.75f)
    )
    set(value) {
        field = value
        invalidate()
    }

    private val nodePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val lockedPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.text_secondary) and 0x44FFFFFF
    }
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f * density
        color = ContextCompat.getColor(context, R.color.border_color)
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        color = ContextCompat.getColor(context, R.color.on_surface)
        textSize = 10f * scaledDensity
    }
    private val legendPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.LEFT
        color = ContextCompat.getColor(context, R.color.text_secondary)
        textSize = 9f * scaledDensity
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width.toFloat()
        val h = height.toFloat() - 28f * density
        val nodeRadius = 22f * density

        // Calculate positions
        val positions = nodes.map { node ->
            node.x * w to node.y * h
        }

        // Draw edges
        val edges = listOf(0 to 2, 1 to 2, 2 to 3, 3 to 4)
        for ((a, b) in edges) {
            canvas.drawLine(
                positions[a].first, positions[a].second,
                positions[b].first, positions[b].second,
                linePaint
            )
        }

        // Draw nodes
        for ((i, node) in nodes.withIndex()) {
            val (px, py) = positions[i]

            if (node.mastery < 0) {
                canvas.drawCircle(px, py, nodeRadius, lockedPaint)
                textPaint.color = ContextCompat.getColor(context, R.color.text_secondary)
            } else {
                val alpha = 128 + node.mastery * 127 / 100
                nodePaint.color = ContextCompat.getColor(context, R.color.primary) and
                        (alpha shl 24) or 0x00FFFFFF
                canvas.drawCircle(px, py, nodeRadius, nodePaint)
                textPaint.color = ContextCompat.getColor(context, R.color.white)
            }
            canvas.drawText(node.name, px, py + 4f * density, textPaint)
        }

        // Legend
        val ly = h + 20f * density
        legendPaint.color = ContextCompat.getColor(context, R.color.primary)
        canvas.drawCircle(14f * density, ly, 5f * density, legendPaint)
        legendPaint.color = ContextCompat.getColor(context, R.color.text_secondary)
        canvas.drawText("已掌握", 24f * density, ly + 3f * density, legendPaint)

        canvas.drawCircle(w / 2 + 20f * density, ly, 5f * density, lockedPaint)
        canvas.drawText("未解锁", w / 2 + 30f * density, ly + 3f * density, legendPaint)
    }
}
