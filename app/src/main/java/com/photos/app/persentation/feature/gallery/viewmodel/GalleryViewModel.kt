package com.photos.app.persentation.feature.gallery.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.photos.app.data.entitiy.Photo
import com.photos.app.data.network.Mapper
import com.photos.app.data.network.core.observable.Callback
import com.photos.app.data.network.request.FetchPhotosRequestParams
import com.photos.app.domain.model.PhotoModel
import com.photos.app.domain.repository.PhotosRepository

class GalleryViewModel(
    private val photoRepository: PhotosRepository,
    private val lifecycleOwner: LifecycleOwner,
    private val domainMapper: Mapper<Photo, PhotoModel>
) : ViewModel() {

    private val params = MutableLiveData<FetchPhotosRequestParams>()
    val states = MutableLiveData<ViewState>()

    init {
        observeEvents()
    }

    private fun observeEvents() = apply {
        params.observe(lifecycleOwner, Observer { loadPhotos(it) })
    }

    private fun loadPhotos(params: FetchPhotosRequestParams) {
        photoRepository.fetchPhotos(params)
            .observe(false, object :
                Callback<List<Photo>> {
                override fun onSuccess(result: List<Photo>) {
                    val mappedPhotos = result.filter { it.farm != "0" }.map { domainMapper.map(it) }
                    onLoaded(mappedPhotos)
                }

                override fun onError() {
                    GalleryViewModel::onError
                }
            })
    }

    @VisibleForTesting
    fun onError() {
        states.value =
            ViewState.Error
    }

    @VisibleForTesting
    fun onLoaded(result: List<PhotoModel>) {
        states.value =
            ViewState.Result(
                result
            )
    }

    fun nextPage() = apply {
        if (states.value != ViewState.LoadMore) {
            states.value =
                ViewState.LoadMore
            params.value = FetchPhotosRequestParams(
                params.value?.text ?: "",
                params.value?.page.incrementOrONe()
            )
        }
    }

    fun search(query: String) = apply {
        states.value =
            ViewState.Loading
        params.postValue(FetchPhotosRequestParams(query, 1))
    }

    private fun Int?.incrementOrONe(): Int = this?.let { if (it >= 1) return it + 1 else 1 } ?: 1

    sealed class ViewState {
        object Loading : ViewState()
        object LoadMore : ViewState()
        object Error : ViewState()
        data class Result(val photos: List<PhotoModel>) : ViewState()
    }
}
