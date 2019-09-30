package com.photos.app.data.repository

import com.photos.app.data.network.request.LoadPhotoRequestParams
import com.photos.app.data.network.response.NetworkLoadPhoto
import com.photos.app.data.network.core.observable.Observable
import com.photos.app.data.network.tasks.PhotoLoaderTask
import com.photos.app.domain.repository.LoadPhotosRepository

class LoadPhotosRepositoryImpl : LoadPhotosRepository {

    override fun loadPhoto(loadPhotoRequestParams: LoadPhotoRequestParams): Observable<NetworkLoadPhoto> {
        return PhotoLoaderTask(loadPhotoRequestParams)
    }
}
