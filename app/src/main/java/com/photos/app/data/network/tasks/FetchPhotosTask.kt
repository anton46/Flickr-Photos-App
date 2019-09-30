package com.photos.app.data.network.tasks

import android.os.AsyncTask
import android.util.Log
import com.photos.app.data.entitiy.Photo
import com.photos.app.data.network.Mapper
import com.photos.app.data.network.core.UrlProvider
import com.photos.app.data.network.core.observable.Callback
import com.photos.app.data.network.core.observable.Observable
import com.photos.app.data.network.request.FetchPhotosRequestParams
import com.photos.app.data.network.response.NetworkPhotos
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class FetchPhotosTask(
    private val networkPhotosMapper: Mapper<String, NetworkPhotos>,
    private val params: FetchPhotosRequestParams
) : AsyncTask<FetchPhotosRequestParams, Void, NetworkPhotos>(),
    Observable<List<Photo>> {

    private var callback: Callback<List<Photo>>? = null

    override fun observe(parallel: Boolean, callback: Callback<List<Photo>>) {
        this.callback = callback
        run(parallel)
    }

    override fun doInBackground(vararg params: FetchPhotosRequestParams?): NetworkPhotos? {
        val photoRequestParams = params[0]
        val url = URL(photoRequestParams?.buildUrl())
        val connection: HttpsURLConnection?
        try {
            Log.i("PhotoLoaderTask", "Call -> $url")
            connection = (url.openConnection() as HttpsURLConnection)
            connection.run {
                requestMethod = "GET"
                inputStream.bufferedReader().use {
                    return networkPhotosMapper.map(it.readLine())
                }
            }
        } catch (e: Exception) {
            Log.e("PhotoLoaderTask", "Error -> ${e.message}")
            e.printStackTrace()
        }
        return null
    }

    override fun onPostExecute(result: NetworkPhotos?) {
        super.onPostExecute(result)
        callback?.let {
            if (null != result) {
                Log.i("PhotoLoaderTask", "Response -> $result")
                it.onSuccess(result.photos)
            } else {
                it.onError()
            }
        }
    }

    private fun run(parallel: Boolean) = apply {
        if (parallel)
            executeOnExecutor(THREAD_POOL_EXECUTOR, params)
        else
            execute(params)
    }

    override fun clear() {
        if (!isCancelled)
            cancel(true)
    }

    private fun FetchPhotosRequestParams.buildUrl() =
        UrlProvider().createFetchImagesUrl(this.text, this.page)
}
