package com.photos.app.data.network.core.observable

interface Callback<T> {
    fun onSuccess(result: T)
    fun onError()
}