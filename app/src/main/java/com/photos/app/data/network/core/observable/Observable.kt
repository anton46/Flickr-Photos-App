package com.photos.app.data.network.core.observable

interface Observable<Result> {
    fun observe(parallel: Boolean = false, callback: Callback<Result>)
    fun clear()
}