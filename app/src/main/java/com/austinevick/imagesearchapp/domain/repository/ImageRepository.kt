package com.austinevick.imagesearchapp.domain.repository

import androidx.paging.Pager
import com.austinevick.imagesearchapp.domain.model.Image

interface ImageRepository {

    fun getImages(query: String): Pager<Int, Image>

}