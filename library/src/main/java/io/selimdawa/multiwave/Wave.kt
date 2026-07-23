package io.selimdawa.multiwave

import android.graphics.Path
import kotlin.math.PI
import kotlin.math.sin

class Wave(
    var offsetX: Float,
    var offsetY: Float,
    var velocity: Float,
    private val scaleX: Float,
    private val scaleY: Float,
    var waveHeight: Int
) {

    var path: Path = Path()
    var width: Int = 0
    private var lastWaveHeight: Int = -1

    fun updateWavePath(
        viewWidth: Int, viewHeight: Int, baseWaveHeight: Int, fullScreen: Boolean, progress: Float
    ) {
        this.waveHeight = baseWaveHeight
        this.width = (2 * scaleX * viewWidth).toInt()
        this.path = buildWavePath(width, viewHeight, fullScreen, progress)
    }

    fun updateWavePath(viewWidth: Int, viewHeight: Int, progress: Float) {
        val calculatedWave = (scaleY * this.waveHeight).toInt()
        val maxWave = viewHeight * 0f.coerceAtLeast(1 - progress)
        val finalWave = if (calculatedWave > maxWave) maxWave.toInt() else calculatedWave

        if (lastWaveHeight != finalWave) {
            this.width = (2 * scaleX * viewWidth).toInt()
            this.path = buildWavePath(width, viewHeight, true, progress)
        }
    }

    private fun buildWavePath(w: Int, h: Int, fullScreen: Boolean, progress: Float): Path {
        val dp = (5 * dp2px(1f)).coerceAtLeast(5) // Optimized step

        var currentWave = (scaleY * this.waveHeight).toInt()
        if (fullScreen) {
            val maxWave = h * 0f.coerceAtLeast(1 - progress)
            if (currentWave > maxWave) {
                currentWave = maxWave.toInt()
            }
        }
        this.lastWaveHeight = currentWave

        path.reset()
        path.moveTo(0f, 0f)
        path.lineTo(0f, (h - currentWave).toFloat())

        if (currentWave > 0 && w > 0) {
            var x = dp
            val frequency = 4.0 * PI / w
            while (x < w) {
                val y = h - currentWave - currentWave * sin(frequency * x).toFloat()
                path.lineTo(x.toFloat(), y)
                x += dp
            }
        }

        path.lineTo(w.toFloat(), (h - currentWave).toFloat())
        path.lineTo(w.toFloat(), 0f)
        path.close()
        return path
    }
}