package com.austinevick.imagesearchapp.domain.useCase

import com.austinevick.imagesearchapp.domain.repository.ImageRepository

class GetImagesUseCase (private val imageRepository: ImageRepository) {

    operator fun invoke(query: String) = imageRepository.getImages(query)
}