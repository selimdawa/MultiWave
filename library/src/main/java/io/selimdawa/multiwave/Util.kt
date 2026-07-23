@file:JvmName("MultiWaveUtils")

package io.selimdawa.multiwave

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes

@ColorInt
internal fun getColor(context: Context, @ColorRes colorId: Int): Int {
    return context.getColor(colorId)
}

internal fun dp2px(dpVal: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dpVal, Resources.getSystem().displayMetrics
    ).toInt()
}