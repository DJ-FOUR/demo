package com.example.demo.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingCanvasView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val density = context.resources.displayMetrics.density
    private val scaledDensity = context.resources.displayMetrics.scaledDensity

    private val paths = mutableListOf<Pair<Path, Paint>>()
    private var currentPath = Path()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#0035C5")
        style = Paint.Style.STROKE
        strokeWidth = 4f * density
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#E2E1F0")
        style = Paint.Style.FILL
    }

    private val watermarkPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#E2E1F0")
        textSize = 12f * scaledDensity
        textAlign = Paint.Align.CENTER
    }

    fun clearCanvas() {
        paths.clear()
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                currentPath = Path()
                currentPath.moveTo(x, y)
                paths.add(currentPath to Paint(paint))
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                currentPath.lineTo(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                currentPath.lineTo(x, y)
                invalidate()
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val spacing = 20f * density
        var gx = spacing
        while (gx < width) {
            var gy = spacing
            while (gy < height) {
                canvas.drawCircle(gx, gy, 1f * density, dotPaint)
                gy += spacing
            }
            gx += spacing
        }

        canvas.drawText("手写草稿区域", width / 2f, height / 2f, watermarkPaint)

        for ((path, p) in paths) {
            canvas.drawPath(path, p)
        }
    }
}
