package com.flatcode.multiwave.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.flatcode.multiwave.R
import kotlin.math.max
import kotlin.math.min

class ColorShadeSlider @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var shades: IntArray
    private var selectedIndex = -1
    private var onColorSelectedListener: ((Int) -> Unit)? = null
    private val rect = RectF()

    init {
        isClickable = true
        isFocusable = true
        val typedArray = resources.obtainTypedArray(R.array.shade_palette)
        shades = IntArray(typedArray.length())
        for (i in 0 until typedArray.length()) {
            shades[i] = typedArray.getColor(i, 0)
        }
        typedArray.recycle()
    }

    fun setOnColorSelectedListener(listener: (Int) -> Unit) {
        onColorSelectedListener = listener
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()
        if (shades.isEmpty()) return

        val itemWidth = w / shades.size
        val padding = 4f

        for (i in shades.indices) {
            paint.color = shades[i]
            val left = i * itemWidth + padding
            val right = (i + 1) * itemWidth - padding
            val top = padding
            val bottom = h - padding

            rect.set(left, top, right, bottom)
            canvas.drawRoundRect(rect, 8f, 8f, paint)

            if (i == selectedIndex) {
                paint.style = Paint.Style.STROKE
                paint.color = Color.WHITE
                paint.strokeWidth = 6f
                canvas.drawRoundRect(rect, 8f, 8f, paint)
                paint.style = Paint.Style.FILL
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (shades.isEmpty()) return false
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                val index = (event.x / (width / shades.size)).toInt()
                val safeIndex = max(0, min(shades.size - 1, index))
                if (safeIndex != selectedIndex) {
                    selectedIndex = safeIndex
                    onColorSelectedListener?.invoke(shades[selectedIndex])
                    invalidate()
                }
                if (event.action == MotionEvent.ACTION_DOWN) {
                    performClick()
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredHeight = (28 * resources.displayMetrics.density).toInt()
        val width = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(width, desiredHeight)
    }
}