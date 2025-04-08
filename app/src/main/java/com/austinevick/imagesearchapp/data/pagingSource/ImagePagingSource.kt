package com.austinevick.imagesearchapp.data.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.austinevick.imagesearchapp.data.mappers.ImageResponseDataToImageMapper
import com.austinevick.imagesearchapp.data.mappers.mapAll
import com.austinevick.imagesearchapp.data.remote.ApiService
import com.austinevick.imagesearchapp.domain.model.Image
import kotlinx.coroutines.delay

class ImagePagingSource(
    private val apiService: ApiService,
    private val query: String,
    private val mapper: ImageResponseDataToImageMapper
) : PagingSource<Int, Image>() {
    override fun getRefreshKey(state: PagingState<Int, Image>): Int? {
      return state.anchorPosition?.let {
           state.closestPageToPosition(it)?.prevKey?.plus(1)
               ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
       }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Image> {
        val page = params.key ?: 1
        val pageSize = params.loadSize
        return try {
            val images = apiService.getImages(query = query, page = page)
            LoadResult.Page(
                data = mapper.mapAll(images.hits),
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (images.hits.size < pageSize) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}