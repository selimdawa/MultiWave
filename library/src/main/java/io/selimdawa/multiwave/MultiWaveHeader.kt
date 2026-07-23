package io.selimdawa.multiwave

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.withSave
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * MultiWaveHeader
 * Created by Selim Dawa on 2026.
 */
class MultiWaveHeader @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private var wavePath: Path? = null
    private val wavePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val drawMatrix = Matrix()
    private val waves: MutableList<Wave> = mutableListOf()
    private var cornerRadius: Float = 0f
    private var curProgress: Float = 0f
    private var lastTime: Long = 0
    private var reboundAnimator: ValueAnimator? = null
    private val clipRect = RectF()
    private var needUpdateGradient = true

    var waveHeight: Int = 0
        set(value) {
            field = value
            updateWavePath()
            invalidate()
        }

    var startColor: Int = 0
        set(value) {
            field = value
            needUpdateGradient = true
            updateGradientColors()
        }

    var closeColor: Int = 0
        set(value) {
            field = value
            needUpdateGradient = true
            updateGradientColors()
        }

    var colorAlpha: Float = 0f
        set(value) {
            field = value.coerceIn(0f, 1f)
            needUpdateGradient = true
            updateGradientColors()
        }

    private var resolvedStartColor: Int = 0
    private var resolvedCloseColor: Int = 0

    var gradientAngle: Int = 0
        set(value) {
            field = value
            needUpdateGradient = true
            invalidate()
        }

    var isRunning: Boolean = false
        set(value) {
            field = value
            if (value) {
                lastTime = System.currentTimeMillis()
                invalidate()
            }
        }

    var enableFullScreen: Boolean = false
        set(value) {
            field = value
            updateWavePath(width, height)
            invalidate()
        }

    var velocity: Float = 1f
        set(value) {
            field = value.coerceAtLeast(0f)
        }

    var progress: Float = 0f
        set(value) {
            val safeValue = value.coerceIn(0f, 1f)
            field = safeValue
            needUpdateGradient = true
            if (!isRunning) {
                updateProgress(safeValue)
            } else {
                animProgress(safeValue, DecelerateInterpolator(), 300)
            }
        }

    var shape: ShapeType = ShapeType.RoundRect
        set(value) {
            field = value
            updateShapePath()
            invalidate()
        }

    init {
        context.withStyledAttributes(attrs, R.styleable.MultiWaveHeader) {
            waveHeight =
                getDimensionPixelOffset(R.styleable.MultiWaveHeader_mwhWaveHeight, dp2px(50f))
            startColor = getColor(R.styleable.MultiWaveHeader_mwhStartColor, -0xfa9330)
            closeColor = getColor(R.styleable.MultiWaveHeader_mwhCloseColor, -0xce5002)
            colorAlpha = getFloat(R.styleable.MultiWaveHeader_mwhColorAlpha, 0.45f)
            velocity = getFloat(R.styleable.MultiWaveHeader_mwhVelocity, 1f)
            gradientAngle = getInt(R.styleable.MultiWaveHeader_mwhGradientAngle, 45)
            isRunning = getBoolean(R.styleable.MultiWaveHeader_mwhIsRunning, true)
            enableFullScreen = getBoolean(R.styleable.MultiWaveHeader_mwhEnableFullScreen, false)
            cornerRadius = getDimensionPixelOffset(
                R.styleable.MultiWaveHeader_mwhCornerRadius, dp2px(25f)
            ).toFloat()
            shape = ShapeType.entries[getInt(R.styleable.MultiWaveHeader_mwhShape, shape.ordinal)]
            progress = getFloat(R.styleable.MultiWaveHeader_mwhProgress, 0.35f)
            curProgress = progress

            val wavesTag = getString(R.styleable.MultiWaveHeader_mwhWaves)
            if (wavesTag != null) {
                tag = wavesTag
            } else if (tag == null) {
                tag =
                    "70,25,1.4,1.4,-26\n100,5,1.4,1.2,15\n420,0,1.15,1,-10\n520,10,1.7,1.5,20\n220,0,1,1,-15"
            }
        }
        updateGradientColors()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (isRunning) {
            lastTime = System.currentTimeMillis()
            invalidate()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        reboundAnimator?.cancel()
    }

    private fun updateGradientColors() {
        resolvedStartColor = ColorUtils.setAlphaComponent(startColor, (colorAlpha * 255).toInt())
        resolvedCloseColor = ColorUtils.setAlphaComponent(closeColor, (colorAlpha * 255).toInt())
        if (width > 0 && height > 0) {
            updateLinearGradient(width, height)
            invalidate()
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (waves.isEmpty()) {
            updateWavePath()
            updateWavePath(r - l, b - t)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateShapePath()
        updateWavePath(w, h)
        needUpdateGradient = true
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (waves.isEmpty()) return

        if (needUpdateGradient) {
            updateLinearGradient(width, height)
            needUpdateGradient = false
        }

        if (wavePath != null) {
            canvas.withSave {
                clipPath(wavePath!!)
                drawWavesInternal(canvas)
            }
        } else {
            drawWavesInternal(canvas)
        }

        if (isRunning) {
            postInvalidateOnAnimation()
        }
    }

    private fun drawWavesInternal(canvas: Canvas) {
        val h = height
        val thisTime = System.currentTimeMillis()
        var deltaTime = if (lastTime > 0) (thisTime - lastTime) / 1000f else 0f
        if (deltaTime > 0.1f) deltaTime = 0.1f

        for (wave in waves) {
            drawMatrix.reset()
            if (isRunning && lastTime > 0 && wave.velocity != 0f) {
                var offsetX = (wave.offsetX - (wave.velocity * velocity * deltaTime))
                if (wave.width > 0) {
                    val waveHalfWidth = wave.width / 2f
                    offsetX %= waveHalfWidth
                    if (offsetX < 0) offsetX += waveHalfWidth
                }
                wave.offsetX = offsetX
            }

            drawMatrix.setTranslate(wave.offsetX, (1 - curProgress) * h)

            canvas.withSave {
                translate(-wave.offsetX, -wave.offsetY - (1 - curProgress) * h)
                wavePaint.shader?.setLocalMatrix(drawMatrix)
                drawPath(wave.path, wavePaint)
            }
        }
        lastTime = thisTime
    }

    private fun updateLinearGradient(width: Int, height: Int) {
        if (width <= 0 || height <= 0) return
        val w = width.toFloat()
        val h = height * curProgress
        val r = sqrt(w * w + h * h) / 2f
        val angleRad = (PI * gradientAngle / 180f).toFloat()
        val y = r * sin(angleRad)
        val x = r * cos(angleRad)
        wavePaint.shader = LinearGradient(
            w / 2f - x,
            h / 2f - y,
            w / 2f + x,
            h / 2f + y,
            resolvedStartColor,
            resolvedCloseColor,
            Shader.TileMode.CLAMP
        )
    }

    private fun updateShapePath() {
        val w = width
        val h = height
        if (w > 0 && h > 0 && shape != ShapeType.Rect) {
            wavePath = Path()
            clipRect.set(0f, 0f, w.toFloat(), h.toFloat())
            when (shape) {
                ShapeType.RoundRect -> wavePath?.addRoundRect(
                    clipRect, cornerRadius, cornerRadius, Path.Direction.CW
                )

                ShapeType.Oval -> wavePath?.addOval(clipRect, Path.Direction.CW)
                else -> {}
            }
        } else {
            wavePath = null
        }
    }

    private fun updateWavePath() {
        waves.clear()
        val tagStr = tag as? String ?: return
        var wavesData = tagStr.split("\\s+".toRegex()).toTypedArray()

        if ("-1" == tagStr) {
            wavesData =
                "70,25,1.4,1.4,-26\n100,5,1.4,1.2,15\n420,0,1.15,1,-10\n520,10,1.7,1.5,20\n220,0,1,1,-15".split(
                        "\\s+".toRegex()
                    ).toTypedArray()
        } else if ("-2" == tagStr) {
            wavesData = "0,0,1,0.5,90\n90,0,1,0.5,90".split("\\s+".toRegex()).toTypedArray()
        }

        for (data in wavesData) {
            val args = data.split("\\s*,\\s*".toRegex()).toTypedArray()
            if (args.size == 5) {
                waves.add(
                    Wave(
                        dp2px(args[0].toFloat()).toFloat(),
                        dp2px(args[1].toFloat()).toFloat(),
                        dp2px(args[4].toFloat()).toFloat(),
                        args[2].toFloat(),
                        args[3].toFloat(),
                        waveHeight / 2
                    )
                )
            }
        }
    }

    private fun updateWavePath(w: Int, h: Int) {
        for (wave in waves) {
            wave.updateWavePath(w, h, waveHeight / 2, enableFullScreen, curProgress)
        }
    }

    private fun animProgress(prog: Float, interpolator: Interpolator, duration: Int) {
        if (curProgress != prog) {
            reboundAnimator?.cancel()
            reboundAnimator = ValueAnimator.ofFloat(curProgress, prog).apply {
                this.duration = duration.toLong()
                this.interpolator = interpolator
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        reboundAnimator = null
                    }
                })
                addUpdateListener { animation ->
                    updateProgress(animation.animatedValue as Float)
                }
                start()
            }
        }
    }

    private fun updateProgress(prog: Float) {
        curProgress = prog
        needUpdateGradient = true
        if (enableFullScreen) {
            updateWavePath(width, height)
        }
        if (!isRunning) {
            invalidate()
        }
    }

    fun setWaves(wavesStr: String) {
        tag = wavesStr
        updateWavePath()
        updateWavePath(width, height)
        invalidate()
    }

    fun setProgress(prog: Float, interpolator: Interpolator, duration: Int) {
        this.progress = prog
        animProgress(prog, interpolator, duration)
    }

    fun start() {
        isRunning = true
    }

    fun stop() {
        isRunning = false
    }
}