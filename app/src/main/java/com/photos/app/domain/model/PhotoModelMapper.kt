package com.photos.app.domain.model

import com.photos.app.data.entitiy.Photo
import com.photos.app.data.network.Mapper

class PhotoModelMapper :
    Mapper<Photo, PhotoModel> {
    override fun map(input: Photo): PhotoModel {
        return PhotoModel(
            input.id,
            input.farm,
            input.secret,
            input.server
        )
    }
}