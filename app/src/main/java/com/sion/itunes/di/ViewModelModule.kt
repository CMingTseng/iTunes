package com.sion.itunes.di

import androidx.paging.ExperimentalPagingApi
import com.sion.itunes.view.music.MusicViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module


@OptIn(KoinApiExtension::class)
@ExperimentalPagingApi
val viewModelModule = module {
    viewModel { MusicViewModel(get(), get()) }
}