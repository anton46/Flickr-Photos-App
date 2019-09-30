package com.photos.app.data.network.response.mappers

import com.photos.app.data.entitiy.Photo
import com.photos.app.data.network.Mapper
import com.photos.app.data.network.response.NetworkPhotos
import com.photos.app.domain.contract.PhotosResponseContract
import org.json.JSONObject

class NetworkPhotosMapper :
    Mapper<String, NetworkPhotos> {
    override fun map(input: String): NetworkPhotos {
        val responseObject = JSONObject(input).getJSONObject(PhotosResponseContract.PHOTOS)
        val photosJsonArray = responseObject.getJSONArray(PhotosResponseContract.PHOTO)
        val photos = mutableListOf<Photo>()
        for (i in 0 until photosJsonArray.length()) {
            val photoObject = photosJsonArray.getJSONObject(i)
            val photo = Photo(
                photoObject.getString(PhotosResponseContract.ID),
                photoObject.getString(PhotosResponseContract.FARM),
                photoObject.getString(PhotosResponseContract.SECRET),
                photoObject.getString(PhotosResponseContract.SERVER)
            )
            photos.add(photo)
        }
        return NetworkPhotos(photos)
    }
}