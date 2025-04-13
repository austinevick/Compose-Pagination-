package com.austinevick.imagesearchapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.austinevick.imagesearchapp.domain.useCase.GetImagesUseCase
import com.austinevick.imagesearchapp.domain.useCase.ImageDownloadUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest

class MainViewModel(
    private val useCase: GetImagesUseCase,
    private val downloadUseCase: ImageDownloadUseCase
) : ViewModel() {

    private val query = MutableStateFlow("flowers")
    val searchQuery = query.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val images = query.filter { it.isNotBlank() }
        .debounce(1000)
        .flatMapLatest { query ->
            useCase.invoke(query).flow
        }.cachedIn(viewModelScope)

    fun setQuery(value: String) {
        query.value = value
    }

    fun downloadImage(url: String) {
        try {
        downloadUseCase.invoke(url)
        }catch (e: Exception) {
            e.printStackTrace()
        }

    }

}