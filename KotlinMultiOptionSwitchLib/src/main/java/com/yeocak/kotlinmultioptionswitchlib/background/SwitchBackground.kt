package com.yeocak.kotlinmultioptionswitchlib.background

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

interface SwitchBackground {

    val coordinates: RectF

    var backgroundVisible: Boolean
    var shadowVisible: Boolean

    val isHorizontal: Boolean

    fun draw(
        canvas: Canvas?,
        backgroundPaint: Paint,
        linesPaint: Paint,
        shadowPaint: Paint,
        selectCoordinates: ArrayList<Float>
    )
}