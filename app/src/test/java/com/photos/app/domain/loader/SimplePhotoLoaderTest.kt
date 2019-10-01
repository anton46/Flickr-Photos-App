package com.photos.app.domain.loader

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import com.photos.app.any
import com.photos.app.data.network.core.observable.Observable
import com.photos.app.data.network.request.LoadPhotoRequestParams
import com.photos.app.data.network.response.NetworkLoadPhoto
import com.photos.app.domain.repository.LoadPhotosRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SimplePhotoLoaderTest {

    lateinit var photoLoader: SimplePhotoLoader

    @Mock
    lateinit var imageView: ImageView

    @Mock
    lateinit var placeholder: View

    @Mock
    lateinit var bitmap: Bitmap

    @Mock
    lateinit var imageCache: ImageCache

    @Mock
    lateinit var loadPhotosRepository: LoadPhotosRepository

    @Mock
    lateinit var observable: Observable<NetworkLoadPhoto>

    @Test
    fun testLoadPhoto() {
        //First call load from network
        val photoUrl = "http://photo.com"
        val params = LoadPhotoRequestParams(photoUrl)

        photoLoader = SimplePhotoLoader(imageCache).attachRepository(loadPhotosRepository)
        `when`(loadPhotosRepository.loadPhoto(params)).thenReturn(observable)

        photoLoader.loadPhoto(photoUrl, imageView, placeholder)

        verify(observable).observe(anyBoolean(), any())

        //Second call load from cache
        `when`(imageCache.get(photoUrl)).thenReturn(bitmap)
        `when`(imageView.tag).thenReturn(photoUrl)

        photoLoader.loadPhoto(photoUrl, imageView, placeholder)

        verify(imageView).setImageBitmap(bitmap)
        verify(imageView).visibility = View.VISIBLE
        verify(placeholder).visibility = View.GONE
    }
}