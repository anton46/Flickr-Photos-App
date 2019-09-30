package com.photos.app.data.network.core

import com.photos.app.domain.model.PhotoModel

class UrlProvider {

    companion object {
        const val GET_PHOTOS_BASE_URL =
            "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=3e7cc266ae2b0e0d78e279ce8e361736&format=json&nojsoncallback=1&safe_search=1"
        const val PER_PAGE = 20
    }

    fun createFetchImagesUrl(text: String = "", page: Int = 1): String {
        val query = text.split(" ").joinToString("%20")
        return GET_PHOTOS_BASE_URL
            .plus("&")
            .plus("per_page=$PER_PAGE")
            .plus("&")
            .plus("text=$query")
            .plus("&")
            .plus("page=$page")
            .plus("&")
            .plus("extras=url_s")
    }

    fun createLoadImageUrl(photo: PhotoModel): String {
        return "https://farm${photo.farm}.static.flickr.com/${photo.server}/${photo.id}_${photo.secret}_s.jpg"
    }


}