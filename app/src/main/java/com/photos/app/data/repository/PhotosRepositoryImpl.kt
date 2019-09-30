package com.photos.app.data.repository

import com.photos.app.data.entitiy.Photo
import com.photos.app.data.network.Mapper
import com.photos.app.data.network.core.observable.Observable
import com.photos.app.data.network.request.FetchPhotosRequestParams
import com.photos.app.data.network.response.NetworkPhotos
import com.photos.app.data.network.tasks.FetchPhotosTask
import com.photos.app.domain.repository.PhotosRepository

class PhotosRepositoryImpl(
    private val networkMapper: Mapper<String, NetworkPhotos>
) : PhotosRepository {

    override fun fetchPhotos(params: FetchPhotosRequestParams): Observable<List<Photo>> {
        return FetchPhotosTask(networkMapper, params)
    }
}
