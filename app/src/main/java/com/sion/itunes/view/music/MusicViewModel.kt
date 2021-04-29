package com.sion.itunes.view.music

import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.sion.itunes.model.api.ApiRepository
import com.sion.itunes.model.vo.Music
import com.sion.itunes.view.base.BaseViewModel
import com.sion.itunes.view.music.repository.MusicPagingSource
import com.sion.itunes.view.music.repository.PageKeyedRemoteMediator
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
@ExperimentalPagingApi
class MusicViewModel: BaseViewModel() {
    fun search(keyword: String): Flow<PagingData<Music>> {
        return Pager(
            config = PagingConfig(
                pageSize = ApiRepository.NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MusicPagingSource(apiRepository, keyword) }
        ).flow.cachedIn(viewModelScope)
    }

    fun searchThroughDB(keyword: String) : Flow<PagingData<Music>> {
        return Pager(
            config = PagingConfig(
                pageSize = ApiRepository.NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = PageKeyedRemoteMediator(itunesDb, apiRepository, keyword)
        ) {
            itunesDb.musics().musicsByKeyword(keyword)
        }.flow
    }
}