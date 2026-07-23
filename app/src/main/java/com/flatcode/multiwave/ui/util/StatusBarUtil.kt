package com.flatcode.multiwave.ui.util

import android.graphics.Color
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

fun ComponentActivity.applyEdgeToEdge(isDark: Boolean = false) {
    enableEdgeToEdge(
        statusBarStyle = if (isDark) SystemBarStyle.dark(Color.TRANSPARENT) else SystemBarStyle.light(
            Color.TRANSPARENT, Color.TRANSPARENT
        ),
        navigationBarStyle = if (isDark) SystemBarStyle.dark(Color.TRANSPARENT) else SystemBarStyle.light(
            Color.TRANSPARENT, Color.TRANSPARENT
        )
    )
}

fun View.applyNavigationBarPadding() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val navBar = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
        v.setPadding(v.paddingLeft, v.paddingTop, v.paddingRight, navBar.bottom)
        insets
    }
}

fun View.applySystemBarsPadding() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
        insets
    }
}