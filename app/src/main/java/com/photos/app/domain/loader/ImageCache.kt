package com.photos.app.domain.loader

import android.graphics.Bitmap

interface ImageCache {
    fun get(key: String) : Bitmap
    fun put(key: String, value: Bitmap)
    fun clear()
}