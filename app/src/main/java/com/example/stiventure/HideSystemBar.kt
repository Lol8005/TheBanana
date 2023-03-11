package com.example.stiventure

import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowInsets
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class HideSystemBar {

    private lateinit var window: Window

    private val onWindowInsetApplied: (view: View, window: WindowInsets) -> WindowInsets = { view, windowInsets ->

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        windowInsets
    }

    fun hide(_window: Window){

        window = _window
        window.decorView.setOnApplyWindowInsetsListener(onWindowInsetApplied)

        WindowCompat.getInsetsController(window, window.decorView).hide(WindowInsetsCompat.Type.statusBars())
    }
}