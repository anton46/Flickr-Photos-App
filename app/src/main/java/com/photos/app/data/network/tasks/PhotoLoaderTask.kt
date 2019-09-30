package com.photos.app.data.network.tasks

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import androidx.core.util.Preconditions
import com.photos.app.data.network.core.observable.Callback
import com.photos.app.data.network.core.observable.Observable
import com.photos.app.data.network.request.LoadPhotoRequestParams
import com.photos.app.data.network.response.NetworkLoadPhoto
import com.photos.app.domain.loader.ImageCache
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.URL

class PhotoLoaderTask(private val params: LoadPhotoRequestParams) :
    AsyncTask<LoadPhotoRequestParams, Void, NetworkLoadPhoto>(),
    Observable<NetworkLoadPhoto> {
    private var callbackReference: WeakReference<Callback<NetworkLoadPhoto>>? = null

    override fun observe(parallel: Boolean, callback: Callback<NetworkLoadPhoto>) {
        this.callbackReference = WeakReference(callback)
        run(parallel)
    }

    override fun doInBackground(vararg loadPhotoRequestParams: LoadPhotoRequestParams): NetworkLoadPhoto? {
        Preconditions.checkArgument(callbackReference != null, "Please provide callback")

        val params = loadPhotoRequestParams[0]
        var bitmap: Bitmap? = null
        var result: NetworkLoadPhoto? = null
        try {
            Log.i("PhotoLoaderTask", "Load image -> ${params.url}")
            val inputStream = URL(params.url).openStream()
            bitmap = BitmapFactory.decodeStream(inputStream)

            bitmap?.let {
                ImageCache.getInstance().put(params.url, it)
                result = NetworkLoadPhoto(params.url, it)
            }
        } catch (ioException: IOException) {
            Log.e("PhotoLoaderTask", "Error -> ${ioException.message}")
            ioException.printStackTrace()
        }

        return result
    }

    override fun onPostExecute(result: NetworkLoadPhoto?) {
        super.onPostExecute(result)
        val callback = callbackReference?.get()
        callback?.let {
            if (null != result)
                it.onSuccess(result)
            else
                it.onError()
        }
    }

    private fun run(parallel: Boolean) {
        if (parallel)
            executeOnExecutor(THREAD_POOL_EXECUTOR, params)
        else
            execute(params)
    }

    override fun clear() {
        if (!isCancelled)
            cancel(true)
    }
}