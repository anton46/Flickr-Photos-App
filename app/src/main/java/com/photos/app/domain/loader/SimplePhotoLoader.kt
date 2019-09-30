package com.photos.app.domain.loader

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import com.photos.app.common.provideLoadPhotosRepository
import com.photos.app.data.network.core.observable.Callback
import com.photos.app.data.network.core.observable.Observable
import com.photos.app.data.network.request.LoadPhotoRequestParams
import com.photos.app.data.network.response.NetworkLoadPhoto
import com.photos.app.domain.repository.LoadPhotosRepository
import java.lang.ref.WeakReference

class SimplePhotoLoader {

    companion object {
        private var instance: SimplePhotoLoader? = null
        fun getInstance(): SimplePhotoLoader {
            if (instance == null) instance =
                SimplePhotoLoader()
                    .with(provideLoadPhotosRepository())
            return instance!!
        }
    }

    private lateinit var repository: LoadPhotosRepository
    private val requests = mutableMapOf<String, Observable<NetworkLoadPhoto>>()

    private fun with(
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
        val params = LoadPhotoRequestParams(url)
        val weakImageViewReference = WeakReference(imageView)
        val weakPlaceholderReference = WeakReference(placeholder)
        val request = repository.loadPhoto(params)
        request.observe(true, object : Callback<NetworkLoadPhoto> {
            override fun onSuccess(result: NetworkLoadPhoto) {
                bindPhotoToView(
                    result.url,
                    result.bitmap,
                    weakImageViewReference.get(),
                    weakPlaceholderReference.get()
                )
            }

            override fun onError() {
                placeholder.visibility = View.VISIBLE
            }
        })
        requests[url] = request
    }

    private fun bindPhotoToView(url: String, bitmap: Bitmap, imageView: ImageView?, placeholder: View?) {
        if (imageView != null && placeholder != null && imageView.tag == url) {
            imageView.setImageBitmap(bitmap)
            imageView.visibility = View.VISIBLE
            placeholder.visibility = View.GONE
            clearRequest(url)
        } else {
            placeholder?.visibility = View.VISIBLE
        }
    }

    private fun clearRequest(key: String) {
        requests[key]?.clear()
        requests.remove(key)
    }

    fun clear() {
        for (observable in requests.values) {
            observable.clear()
        }
        requests.clear()
        ImageCache.getInstance().clear()
    }
}