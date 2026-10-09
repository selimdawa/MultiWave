@file:JvmName("MultiWaveUtils")

package io.selimdawa.multiwave

import android.content.res.Resources
import android.util.TypedValue

internal fun dp2px(dpVal: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dpVal, Resources.getSystem().displayMetrics
    ).toInt()
}