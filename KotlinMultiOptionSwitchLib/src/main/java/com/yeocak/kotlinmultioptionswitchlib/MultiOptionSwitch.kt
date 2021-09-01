package com.yeocak.kotlinmultioptionswitchlib

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.res.ResourcesCompat
import kotlin.math.abs
import kotlin.properties.Delegates

class MultiOptionSwitch @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var optionChangedListener: ((optionIndex: Int) -> Unit)? = null

    fun setOptionChangedListener(f: (optionIndex: Int) -> Unit) {
        optionChangedListener = f
    }

    private var optionCount = 0
    private var selectedOption = 5

    private var currentX = 0f
        get() {
            return field.coerceAtLeast(minXPosition).coerceAtMost(maxXPosition)
        }

    // region Colors
    private val backGrayColor = ResourcesCompat.getColor(
        resources,
        R.color.backGray,
        null
    )
    private val lineColor = ResourcesCompat.getColor(
        resources,
        R.color.transparentWhite,
        null
    )
    private val selectColor = ResourcesCompat.getColor(
        resources,
        R.color.white,
        null
    )

    private val shadowColor = ResourcesCompat.getColor(
        resources,
        R.color.transparentBlack,
        null
    )
    // endregion

    private var componentWidth by Delegates.notNull<Float>()
    private var componentHeight by Delegates.notNull<Float>()

    // region Paints
    private val backgroundPaint = Paint().apply {
        color = backGrayColor
        isAntiAlias = true
    }

    private val linePaint = Paint().apply {
        color = lineColor
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private val selectPaint = Paint().apply {
        color = selectColor
        isAntiAlias = true
    }

    private val shadowPaint = Paint().apply {
        color = shadowColor
        isAntiAlias = true
    }
    //endregion

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MultiSwitch,
            0, 0
        ).apply {
            optionCount = getInt(R.styleable.MultiSwitch_option_count, 3)
            selectedOption = getInt(R.styleable.MultiSwitch_default_selected_option, 4)
            recycle()
        }
    }

    private var minXPosition = 0f
    private var maxXPosition = 0f
    private var selectRadius = 0f

    fun selectOption(optionIndex: Int){
        selectedOption = optionIndex - 1

        val destinationPosition = findXByOption(selectedOption)

        selectAnimator.apply {
            setFloatValues(currentX, destinationPosition)
            start()
        }

        optionChangedListener?.let {
            it(selectedOption + 1)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        componentWidth = (w - 10).toFloat()
        componentHeight = (h - 10).toFloat()

        selectRadius = componentHeight / 2f - 10
        minXPosition = selectRadius + 20
        maxXPosition = w - selectRadius - 20

        currentX = findXByOption()
    }

    override fun onDraw(canvas: Canvas?) {
        drawBackground(canvas)
        drawLines(canvas)
        drawSelect(canvas)
    }

    private val selectAnimator = ValueAnimator.ofFloat(0f, 0f).apply {
        duration = 100
        interpolator = LinearInterpolator()
        addUpdateListener { valueAnimator ->
            currentX = valueAnimator.animatedValue as Float
            invalidate()
        }
    }

    private fun findXByOption(option: Int = selectedOption): Float {
        val singlePartWidth = componentWidth / (optionCount - 1f) * option + 5

        if (option == 0) return minXPosition
        else if (option == optionCount - 1) return maxXPosition

        return singlePartWidth
    }

    private fun goNearestPointWithAnimation() {
        selectedOption = findNearestOption()

        val destinationPosition = findXByOption(selectedOption)

        selectAnimator.apply {
            setFloatValues(currentX, destinationPosition)
            start()
        }
    }

    private fun findNearestOption(currentPosition: Float = currentX): Int {
        var distance = Float.MAX_VALUE
        var whichOption = -1

        if (currentPosition - minXPosition < distance) {
            distance = currentPosition - minXPosition
            whichOption = 0
        }
        if (abs(currentPosition - maxXPosition) < distance) {
            distance = abs(currentPosition - maxXPosition)
            whichOption = optionCount - 1
        }
        for (a in 1..(optionCount - 2)) {
            if (abs(currentPosition - findXByOption(a)) < distance) {
                distance = abs(currentPosition - findXByOption(a))
                whichOption = a
            }
        }

        return whichOption
    }

    private fun drawBackground(canvas: Canvas?) {
        // Draw shadow of background
        canvas?.drawRoundRect(
            8f,
            12f,
            componentWidth + 8,
            componentHeight + 12,
            200f,
            200f,
            shadowPaint
        )

        // Draw background
        canvas?.drawRoundRect(
            5f,
            5f,
            componentWidth + 5,
            componentHeight + 5,
            200f,
            200f,
            backgroundPaint
        )
    }

    private fun drawLines(canvas: Canvas?) {
        val partWidth = componentWidth / (optionCount - 1f)
        for (a in 1..optionCount - 2) {
            canvas?.drawRect(
                partWidth * a + 3,
                30f,
                partWidth * a + 7,
                componentHeight - 30f,
                linePaint
            )
        }
    }

    private fun drawSelect(canvas: Canvas?) {
        // Draw shadow of select ball
        canvas?.drawCircle(
            currentX + 3,
            componentHeight / 2f + 12,
            selectRadius,
            shadowPaint
        )

        // Draw select ball
        canvas?.drawCircle(
            currentX,
            componentHeight / 2f + 5,
            selectRadius,
            selectPaint
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_MOVE) {
            currentX = event.x
            invalidate()
        } else if (event.action == MotionEvent.ACTION_UP) {
            goNearestPointWithAnimation()
            optionChangedListener?.let {
                it(selectedOption + 1)
            }
        }
        return true
    }

}