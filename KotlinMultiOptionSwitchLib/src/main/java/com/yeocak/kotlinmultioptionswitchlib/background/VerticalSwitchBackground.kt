package com.yeocak.kotlinmultioptionswitchlib.background

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

class VerticalSwitchBackground : SwitchBackground {
    override val coordinates = RectF(0f, 0f, 0f, 0f)

    override var backgroundVisible = true
    override var shadowVisible = true

    override val isHorizontal = false

    override fun draw(
        canvas: Canvas?,
        backgroundPaint: Paint,
        linesPaint: Paint,
        shadowPaint: Paint,
        selectCoordinates: ArrayList<Float>
    ) {
        if (!backgroundVisible) return

        if (shadowVisible) drawBackgroundShadow(canvas, shadowPaint)

        canvas?.drawRoundRect(
            coordinates.left,
            coordinates.top,
            coordinates.right,
            coordinates.bottom,
            500f,
            500f,
            backgroundPaint
        )

        selectorLines(canvas, linesPaint, selectCoordinates)
    }

    private fun selectorLines(
        canvas: Canvas?,
        linesPaint: Paint,
        selectCoordinates: ArrayList<Float>
    ) {
        for (a in 1 until selectCoordinates.size - 1) {
            canvas?.drawRect(
                coordinates.left + 30f,
                selectCoordinates[a] + coordinates.top - 3f,
                coordinates.right - 30f,
                selectCoordinates[a] + coordinates.top + 3f,
                linesPaint
            )
        }
    }

    private fun drawBackgroundShadow(canvas: Canvas?, shadowPaint: Paint) {
        canvas?.drawRoundRect(
            coordinates.left + 3,
            coordinates.top + 7,
            coordinates.right + 3,
            coordinates.bottom + 7,
            500f,
            500f,
            shadowPaint
        )
    }

}