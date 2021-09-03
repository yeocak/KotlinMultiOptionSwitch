package com.yeocak.kotlinmultioptionswitchlib

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.view.animation.LinearInterpolator

class SelectorBall {
    var positionX: Float = 0f
    var positionY: Float = 0f
    var radius: Float = 0f

    var shadowVisible = true
    var selectedOption = 0

    private val selectAnimator = ValueAnimator.ofFloat(0f, 0f).apply {
        duration = 130
        interpolator = LinearInterpolator()
    }

    fun draw(canvas: Canvas?, selectorPaint: Paint, shadowPaint: Paint) {
        if (shadowVisible) drawShadow(canvas, shadowPaint)

        canvas?.drawCircle(
            positionX,
            positionY,
            radius,
            selectorPaint
        )
    }

    private fun drawShadow(canvas: Canvas?, shadowPaint: Paint) {
        canvas?.drawCircle(
            positionX + 3,
            positionY + 7,
            radius,
            shadowPaint
        )
    }

    fun goPositionWithAnimation(
        destinationPosition: Float,
        isHorizontal: Boolean,
        invalidate: (() -> Unit)
    ) {
        if (isHorizontal) {
            selectAnimator.apply {
                addUpdateListener { valueAnimator ->
                    positionX = valueAnimator.animatedValue as Float
                    invalidate()
                }
                setFloatValues(positionX, destinationPosition)
                start()
            }
        } else {
            selectAnimator.apply {
                addUpdateListener { valueAnimator ->
                    positionY = valueAnimator.animatedValue as Float
                    invalidate()
                }
                setFloatValues(positionY, destinationPosition)
                start()
            }
        }

    }

}