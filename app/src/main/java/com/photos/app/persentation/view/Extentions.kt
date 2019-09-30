package com.photos.app.persentation.view

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

fun Context.photoSize(grid: Int): Int {
    val metrics = DisplayMetrics()
    val windowManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    windowManager.defaultDisplay.getMetrics(metrics)
    return metrics.widthPixels / grid
}

inline fun <reified VM : ViewModel> AppCompatActivity.provideViewModel(
    crossinline provider: () -> VM
) = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
    override fun <T1 : ViewModel> create(aClass: Class<T1>) = provider() as T1
}).get(VM::class.java)
