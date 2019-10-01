package com.photos.app.domain.loader

import android.graphics.Bitmap
import android.util.LruCache

class ImageCacheImpl : ImageCache {

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

    override fun get(key: String) = memoryCache[key]

    override fun put(key: String, value: Bitmap) {
        memoryCache.put(key, value)
    }

    override fun clear() {
        memoryCache.evictAll()
    }
}
