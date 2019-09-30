package com.photos.app.domain.contract

interface PhotosResponseContract {
    companion object {
        const val PHOTOS = "photos"
        const val PHOTO = "photo"
        const val ID = "id"
        const val SECRET = "secret"
        const val FARM = "farm"
        const val SERVER = "server"
    }
}