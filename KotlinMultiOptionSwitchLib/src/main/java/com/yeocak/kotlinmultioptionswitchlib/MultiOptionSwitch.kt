package com.yeocak.kotlinmultioptionswitchlib

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.yeocak.kotlinmultioptionswitchlib.background.HorizontalSwitchBackground
import com.yeocak.kotlinmultioptionswitchlib.background.SwitchBackground
import com.yeocak.kotlinmultioptionswitchlib.background.VerticalSwitchBackground

class MultiOptionSwitch @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var optionChangedListener: ((optionIndex: Int) -> Unit)? = null

    fun setOptionChangedListener(f: (optionIndex: Int) -> Unit) {
        optionChangedListener = f
    }

    private var optionCount = 3

    private val selectCoordinates = arrayListOf<Float>()

    // region Colors
    private val defaultBackgroundColor = ResourcesCompat.getColor(
        resources,
        R.color.backGray,
        null
    )
    private val defaultLineColor = ResourcesCompat.getColor(
        resources,
        R.color.transparentWhite,
        null
    )
    private val defaultSelectorColor = ResourcesCompat.getColor(
        resources,
        R.color.white,
        null
    )

    private val defaultShadowColor = ResourcesCompat.getColor(
        resources,
        R.color.transparentBlack,
        null
    )
    // endregion

    // region Paints
    private val backgroundPaint = Paint().apply {
        color = defaultBackgroundColor
        isAntiAlias = true
    }

    private val linePaint = Paint().apply {
        color = defaultLineColor
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private val selectorPaint = Paint().apply {
        color = defaultSelectorColor
        isAntiAlias = true
    }

    private val shadowPaint = Paint().apply {
        color = defaultShadowColor
        isAntiAlias = true
    }
    //endregion

    private var background: SwitchBackground
    private var selector = SelectorBall()

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MultiSwitch,
            0, 0
        ).apply {
            // Getting data from attrs.xml
            background = if (getInt(R.styleable.MultiSwitch_direction, 0) == 0) {
                HorizontalSwitchBackground()
            } else {
                VerticalSwitchBackground()
            }

            optionCount = getInt(R.styleable.MultiSwitch_option_count, 3)
            selector.selectedOption = getInt(R.styleable.MultiSwitch_default_selected_option, 1) - 1
            background.backgroundVisible =
                getBoolean(R.styleable.MultiSwitch_background_visible, true)

            val shadowVisible = getBoolean(R.styleable.MultiSwitch_shadow_visible, true)
            selector.shadowVisible = shadowVisible
            background.shadowVisible = shadowVisible

            val colorOfBackground =
                getColor(R.styleable.MultiSwitch_background_color, defaultBackgroundColor)
            val selectorColor =
                getColor(R.styleable.MultiSwitch_selector_color, defaultSelectorColor)

            // Setting Paint Data
            backgroundPaint.color = colorOfBackground
            selectorPaint.color = selectorColor

            recycle()
        }
    }

    fun selectOption(optionIndex: Int) {
        selector.goPositionWithAnimation(
            selectCoordinates[optionIndex - 1],
            background.isHorizontal
        ) {
            selector.selectedOption = optionIndex - 1
            invalidate()
        }

        optionChangedListener?.let {
            it(selector.selectedOption + 1)
        }
    }

    private fun findNearestOptionPosition(position: Float): Int {
        var distance = Float.MAX_VALUE
        var index = -1

        for (a in 0 until selectCoordinates.size) {
            val newDistance = kotlin.math.abs(position - selectCoordinates[a])
            if (newDistance < distance) {
                distance = newDistance
                index = a
            }
        }

        return index
    }

    private fun setSelectCoordinates(backgroundCoordinates: RectF, selectorRadius: Float) {
        if (background.isHorizontal) {
            val partWidth =
                (backgroundCoordinates.right - backgroundCoordinates.left) / (optionCount - 1)

            selectCoordinates.clear()
            selectCoordinates.add(backgroundCoordinates.left + selectorRadius + 10f)

            for (a in 1 until optionCount - 1) {
                selectCoordinates.add(backgroundCoordinates.left + partWidth * a)
            }

            selectCoordinates.add(backgroundCoordinates.right - selectorRadius - 10f)
        } else {
            val partHeight =
                (backgroundCoordinates.bottom - backgroundCoordinates.top) / (optionCount - 1)

            selectCoordinates.clear()
            selectCoordinates.add(backgroundCoordinates.top + selectorRadius + 10f)

            for (a in 1 until optionCount - 1) {
                selectCoordinates.add(backgroundCoordinates.top + partHeight * a)
            }

            selectCoordinates.add(backgroundCoordinates.bottom - selectorRadius - 10f)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val backgroundCoordinates = RectF(5f, 7f, w - 5f, h - 7f)
        background.coordinates.set(backgroundCoordinates)

        val selectorRadius: Float = if (background.isHorizontal) {
            (backgroundCoordinates.bottom - backgroundCoordinates.top) / 2 - 10
        } else {
            (backgroundCoordinates.right - backgroundCoordinates.left) / 2 - 10
        }

        setSelectCoordinates(backgroundCoordinates, selectorRadius)

        if (background.isHorizontal) {
            selector.positionX = selectCoordinates[selector.selectedOption]
            selector.positionY =
                (backgroundCoordinates.bottom - backgroundCoordinates.top) / 2 + backgroundCoordinates.top
        } else {
            selector.positionX =
                (backgroundCoordinates.right - backgroundCoordinates.left) / 2 + backgroundCoordinates.left
            selector.positionY =
                selectCoordinates[selector.selectedOption]
        }

        selector.radius = selectorRadius
    }

    override fun onDraw(canvas: Canvas?) {
        background.draw(canvas, backgroundPaint, linePaint, shadowPaint, selectCoordinates)
        selector.draw(canvas, selectorPaint, shadowPaint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (background.isHorizontal) {
            if (event.action == MotionEvent.ACTION_MOVE) {
                selector.positionX = event.x.coerceAtLeast(selectCoordinates.first())
                    .coerceAtMost(selectCoordinates.last())
                invalidate()
            } else if (event.action == MotionEvent.ACTION_UP) {
                val nearestOption = findNearestOptionPosition(selector.positionX)
                selector.goPositionWithAnimation(
                    selectCoordinates[nearestOption],
                    true
                ) {
                    selector.selectedOption = nearestOption
                    invalidate()
                }

                optionChangedListener?.let {
                    it(selector.selectedOption + 1)
                }
            }
        } else {
            if (event.action == MotionEvent.ACTION_MOVE) {
                selector.positionY = event.y.coerceAtLeast(selectCoordinates.first())
                    .coerceAtMost(selectCoordinates.last())
                invalidate()
            } else if (event.action == MotionEvent.ACTION_UP) {
                val nearestOption = findNearestOptionPosition(selector.positionY)
                selector.goPositionWithAnimation(
                    selectCoordinates[nearestOption],
                    false
                ) {
                    selector.selectedOption = nearestOption
                    invalidate()
                }

                optionChangedListener?.let {
                    it(selector.selectedOption + 1)
                }
            }
        }
        return true
    }

}