package com.photos.app.persentation.feature.gallery.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.photos.app.shouldEqual
import com.photos.app.data.entitiy.Photo
import com.photos.app.data.network.Mapper
import com.photos.app.domain.model.PhotoModel
import com.photos.app.domain.repository.PhotosRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GalleryViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: GalleryViewModel

    @Mock
    private lateinit var photosRepository: PhotosRepository

    @Mock
    private lateinit var lifecycleOwner: LifecycleOwner

    @Mock
    private lateinit var lifecycle: Lifecycle

    @Mock
    private lateinit var mapper: Mapper<Photo, PhotoModel>

    @Mock
    private lateinit var observer: Observer<GalleryViewModel.ViewState>


    @Before
    fun setUp() {
        `when`(lifecycleOwner.lifecycle).thenReturn(lifecycle)
        `when`(lifecycle.currentState).thenReturn(Lifecycle.State.RESUMED)

        viewModel = GalleryViewModel(
            photosRepository,
            lifecycleOwner,
            mapper
        )
    }

    @Test
    fun testStateChanged() {
        viewModel.states.observeForever(observer)

        viewModel.search("Amsterdam")

        viewModel.states.value shouldEqual GalleryViewModel.ViewState.Loading

        viewModel.nextPage()

        viewModel.states.value shouldEqual GalleryViewModel.ViewState.LoadMore

        viewModel.onLoaded(listOf())

        (viewModel.states.value is GalleryViewModel.ViewState.Result) shouldEqual true

        viewModel.onError()

        viewModel.states.value shouldEqual GalleryViewModel.ViewState.Error

        verify(observer, times(4)).onChanged(ArgumentMatchers.any())

    }

}