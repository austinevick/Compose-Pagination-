package com.austinevick.imagesearchapp.data.model

import com.google.gson.annotations.SerializedName

data class ImageResponseModel(
    @SerializedName("hits")
    val hits: List<ImageResponseData> = listOf(),
    @SerializedName("total")
    val total: Int? = null,
    @SerializedName("totalHits")
    val totalHits: Int? = null
)