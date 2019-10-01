package com.photos.app.common

import com.photos.app.data.entitiy.Photo
import com.photos.app.data.network.Mapper
import com.photos.app.data.network.response.NetworkPhotos
import com.photos.app.data.network.response.mappers.NetworkPhotosMapper
import com.photos.app.data.repository.LoadPhotosRepositoryImpl
import com.photos.app.data.repository.PhotosRepositoryImpl
import com.photos.app.domain.loader.ImageCacheImpl
import com.photos.app.domain.model.PhotoModel
import com.photos.app.domain.model.PhotoModelMapper
import com.photos.app.domain.repository.LoadPhotosRepository
import com.photos.app.domain.repository.PhotosRepository

fun providePhotoRepository(): PhotosRepository =
    PhotosRepositoryImpl(provideNetworkPhotosMapper())

fun providePhotoModelMapper(): Mapper<Photo, PhotoModel> =
    PhotoModelMapper()

fun provideLoadPhotosRepository() : LoadPhotosRepository =
    LoadPhotosRepositoryImpl()

fun provideNetworkPhotosMapper(): Mapper<String, NetworkPhotos> =
    NetworkPhotosMapper()

fun provideImageCache() = ImageCacheImpl()
