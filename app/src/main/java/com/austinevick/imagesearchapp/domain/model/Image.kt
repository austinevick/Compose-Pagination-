package com.austinevick.imagesearchapp.domain.model

import java.util.UUID

data class Image(
    val id: String,
    val imageUrl: String,
    val title: String,
    val uuid: String = UUID.randomUUID().toString()
)