package com.austinevick.imagesearchapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.austinevick.imagesearchapp.data.mappers.ImageResponseDataToImageMapper
import com.austinevick.imagesearchapp.data.pagingSource.ImagePagingSource
import com.austinevick.imagesearchapp.data.remote.ApiService
import com.austinevick.imagesearchapp.domain.model.Image
import com.austinevick.imagesearchapp.domain.repository.ImageRepository

class ImageRepositoryImpl(
    private val apiService: ApiService,
    private val mapper: ImageResponseDataToImageMapper
) : ImageRepository {
    override fun getImages(query: String): Pager<Int, Image> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                enablePlaceholders = false,
                prefetchDistance = 1
            ),
            pagingSourceFactory = { ImagePagingSource(apiService, query, mapper) }
        )
    }
}