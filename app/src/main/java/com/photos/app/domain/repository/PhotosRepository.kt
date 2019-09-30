package com.photos.app.domain.repository

import com.photos.app.data.entitiy.Photo
import com.photos.app.data.network.core.observable.Observable
import com.photos.app.data.network.request.FetchPhotosRequestParams

interface PhotosRepository {
    fun fetchPhotos(
        params: FetchPhotosRequestParams
    ): Observable<List<Photo>>
}
