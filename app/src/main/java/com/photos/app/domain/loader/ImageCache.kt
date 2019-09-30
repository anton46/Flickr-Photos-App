package com.photos.app.domain.loader

import android.graphics.Bitmap
import android.util.LruCache

class ImageCache {

    companion object {
        private var instance: ImageCache? = null
        fun getInstance(): ImageCache {
            if (instance == null) instance =
                ImageCache()
            return instance!!
        }
    }

    private val memoryCache: LruCache<String, Bitmap>

    init {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8

        memoryCache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                return bitmap.byteCount / 1024
            }
        }
    }

    fun get(key: String) = memoryCache[key]

    fun put(key: String, value: Bitmap) { memoryCache.put(key, value) }

    fun clear() {
        memoryCache.evictAll()
    }
}
