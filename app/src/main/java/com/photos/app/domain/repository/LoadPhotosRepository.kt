package com.photos.app.domain.repository

import com.photos.app.data.network.core.observable.Observable
import com.photos.app.data.network.request.LoadPhotoRequestParams
import com.photos.app.data.network.response.NetworkLoadPhoto

interface LoadPhotosRepository {
    fun loadPhoto(loadPhotoRequestParams: LoadPhotoRequestParams): Observable<NetworkLoadPhoto>
}
