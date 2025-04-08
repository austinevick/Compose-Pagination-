package com.austinevick.imagesearchapp.data.mappers

import com.austinevick.imagesearchapp.data.model.ImageResponseData
import com.austinevick.imagesearchapp.domain.model.Image

class ImageResponseDataToImageMapper: Mapper<ImageResponseData, Image> {
    override fun map(from: ImageResponseData): Image {
        return Image(
            id = from.id.toString(),
            imageUrl = from.largeImageURL.toString(),
        )
    }
}