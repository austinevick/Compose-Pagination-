package com.austinevick.imagesearchapp.data.di

import android.content.Context
import com.austinevick.imagesearchapp.data.mappers.ImageResponseDataToImageMapper
import com.austinevick.imagesearchapp.data.remote.ApiService
import com.austinevick.imagesearchapp.data.repository.DownloadRepositoryImpl
import com.austinevick.imagesearchapp.data.repository.ImageRepositoryImpl
import com.austinevick.imagesearchapp.domain.repository.DownloadRepository
import com.austinevick.imagesearchapp.domain.repository.ImageRepository
import com.austinevick.imagesearchapp.domain.useCase.GetImagesUseCase
import com.austinevick.imagesearchapp.domain.useCase.ImageDownloadUseCase
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

fun provideDownloadUseCase(downloadRepository: DownloadRepository): ImageDownloadUseCase {
    return ImageDownloadUseCase(downloadRepository)
}

fun provideDownloadRepository(context: Context): DownloadRepository {
    return DownloadRepositoryImpl(context)
}


val appModule = module {
    single { provideRetrofit() }
    single { provideApiService(get()) }
    single { provideImageResponseDataToImageMapper() }
    single { provideImageRepository(get(), get()) }
    single { provideGetImagesUseCase(get()) }
    single { provideDownloadRepository(get()) }
    single { provideDownloadUseCase(get()) }
    viewModel { MainViewModel(get(), get()) }

}