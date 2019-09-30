package com.photos.app.domain.model

import com.photos.app.shouldEqual
import com.photos.app.data.entitiy.Photo
import org.junit.Test

class PhotoModelMapperTest {

    @Test
    fun testMap() {
        val input = Photo("id", "farm", "secret", "server")

        val result = PhotoModelMapper().map(input)

        result.id shouldEqual "id"
        result.farm shouldEqual "farm"
        result.secret shouldEqual "secret"
        result.server shouldEqual "server"
    }
}