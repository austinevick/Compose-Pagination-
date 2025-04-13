package com.austinevick.imagesearchapp.domain.repository

interface DownloadRepository {
    fun downloadFile(url: String): Long
}