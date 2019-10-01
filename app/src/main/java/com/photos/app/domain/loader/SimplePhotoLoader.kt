package com.photos.app.domain.loader

import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.annotation.VisibleForTesting
import com.photos.app.common.provideImageCache
import com.photos.app.common.provideLoadPhotosRepository
import com.photos.app.data.network.core.observable.Callback
import com.photos.app.data.network.core.observable.Observable
import com.photos.app.data.network.request.LoadPhotoRequestParams
import com.photos.app.data.network.response.NetworkLoadPhoto
import com.photos.app.domain.repository.LoadPhotosRepository

class SimplePhotoLoader(val imageCache: ImageCache) {

    companion object {
        val instance: SimplePhotoLoader by lazy {
            SimplePhotoLoader(provideImageCache()).attachRepository(provideLoadPhotosRepository())
        }

        const val TAG = "SimplePhotoLoader"
    }

    private lateinit var repository: LoadPhotosRepository
    private val requests = mutableMapOf<ImageView, Request>()

    @VisibleForTesting
    fun attachRepository(
        repository: LoadPhotosRepository
    ): SimplePhotoLoader {
        this.repository = repository
        return this
    }

    fun loadPhoto(
        url: String,
        imageView: ImageView,
        placeholder: View
    ) {
        imageView.tag = url
        loadFromCache(url) { bitmap ->
            if (bitmap != null) {
                Log.i(TAG, "Load bitmap from cache")
                if (imageView.tag == url) {
                    imageView.setImageBitmap(bitmap)
                    imageView.visibility = View.VISIBLE
                    placeholder.visibility = View.GONE
                }
            } else {
                Log.i(TAG, "Load bitmap from network")
                defer(imageView)
                val params = LoadPhotoRequestParams(url)
                val loadPhotoRequest = repository.loadPhoto(params)
                val requestData = RequestData(imageView, placeholder, loadPhotoRequest)
                val request = Request(requestData).apply { call() }
                requests[imageView] = request
            }
        }
    }

    private fun defer(target: ImageView) {
        if (requests.containsKey(target))
            requests[target]?.cancelRequest()
    }

    private fun loadFromCache(url: String, result: (Bitmap?) -> Unit) {
        result(imageCache.get(url))
    }

    fun clear() {
        for (request in requests.values) {
            request.cancelRequest()
        }
        requests.clear()
        imageCache.clear()
    }

    inner class Request(private val requestData: RequestData) {

        fun call() {
            requestData.observable.observe(true, object : Callback<NetworkLoadPhoto> {
                override fun onSuccess(result: NetworkLoadPhoto) {
                    imageCache.put(result.url, result.bitmap)
                    bindPhotoToView(
                        result.url,
                        result.bitmap,
                        requestData.target,
                        requestData.placeholder
                    )
                }

                override fun onError() {
                    requestData.placeholder.visibility = View.VISIBLE
                }
            })
        }

        fun cancelRequest() {
            requestData.observable.clear()
        }

        private fun bindPhotoToView(
            url: String,
            bitmap: Bitmap,
            imageView: ImageView,
            placeholder: View
        ) {
            if (imageView.tag == url) {
                imageView.setImageBitmap(bitmap)
                imageView.visibility = View.VISIBLE
                placeholder.visibility = View.GONE
            }
        }
    }

    data class RequestData(
        val target: ImageView,
        val placeholder: View,
        val observable: Observable<NetworkLoadPhoto>
    )
}