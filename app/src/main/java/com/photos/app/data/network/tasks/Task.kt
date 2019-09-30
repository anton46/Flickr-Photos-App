package com.photos.app.data.network.tasks

import com.photos.app.data.network.core.observable.Callback

interface Task<T, R> {
    fun run(parallel: Boolean, params: T)
    fun observe(callback: Callback<R>)
}