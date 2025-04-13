package com.austinevick.imagesearchapp.domain.useCase

import com.austinevick.imagesearchapp.domain.repository.DownloadRepository

class ImageDownloadUseCase(private val downloadRepository: DownloadRepository) {

    operator fun invoke(url: String): Long = downloadRepository.downloadFile(url)

}