package com.photos.app.network.core

import com.photos.app.data.network.core.UrlProvider
import com.photos.app.domain.model.PhotoModel
import com.photos.app.shouldEqual
import org.junit.Test

class UrlProviderTest {

    @Test
    fun testBuildUrl() {
        UrlProvider().createFetchImagesUrl("Amsterdam", 1) shouldEqual "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=3e7cc266ae2b0e0d78e279ce8e361736&format=json&nojsoncallback=1&safe_search=1&per_page=20&text=Amsterdam&page=1&extras=url_s"
        UrlProvider().createLoadImageUrl(
            PhotoModel(
                "100",
                "50",
                "1234",
                "293283"
            )
        ) shouldEqual "https://farm50.static.flickr.com/293283/100_1234_s.jpg"
    }
}