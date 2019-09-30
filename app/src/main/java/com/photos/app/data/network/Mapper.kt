package com.photos.app.data.network

interface Mapper<I, O> {
    fun map(input: I): O
}