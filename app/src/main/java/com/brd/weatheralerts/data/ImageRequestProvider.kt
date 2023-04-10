package com.brd.weatheralerts.data

import android.content.Context
import coil.request.CachePolicy
import coil.request.ImageRequest

object ImageRequestProvider {
    fun withCacheKey(context: Context, key: String): ImageRequest {
        return ImageRequest.Builder(context)
            .data("https://picsum.photos/1000")
            .crossfade(true)
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCacheKey(key)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCacheKey(key)
            .build()
    }
}