package com.austinevick.imagesearchapp.data.di

import com.austinevick.imagesearchapp.data.mappers.ImageResponseDataToImageMapper
import com.austinevick.imagesearchapp.data.remote.ApiService
import com.austinevick.imagesearchapp.data.remote.ImageRepositoryImpl
import com.austinevick.imagesearchapp.domain.repository.ImageRepository
import com.austinevick.imagesearchapp.domain.useCase.GetImagesUseCase
import com.austinevick.imagesearchapp.presentation.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


fun provideRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://pixabay.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun provideApiService(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)
}

fun provideImageResponseDataToImageMapper(): ImageResponseDataToImageMapper {
    return ImageResponseDataToImageMapper()
}

fun provideImageRepository(
    apiService: ApiService,
    mapper: ImageResponseDataToImageMapper
): ImageRepository {
    return ImageRepositoryImpl(apiService, mapper)
}

fun provideGetImagesUseCase(imageRepository: ImageRepository): GetImagesUseCase {
    return GetImagesUseCase(imageRepository)
}


val appModule = module {
    single { provideRetrofit() }
    single { provideApiService(get()) }
    single { provideImageResponseDataToImageMapper() }
    single { provideImageRepository(get(), get()) }
    single { provideGetImagesUseCase(get()) }
    viewModel { MainViewModel(get()) }

}