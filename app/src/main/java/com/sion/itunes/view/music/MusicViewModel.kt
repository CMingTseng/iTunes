package com.sion.itunes.view.music

import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.sion.itunes.db.ItunesDb
import com.sion.itunes.model.api.search.ISearchApiRepository
import com.sion.itunes.model.api.search.SearchApiRepository
import com.sion.itunes.model.vo.Music
import com.sion.itunes.view.base.BaseViewModel
import com.sion.itunes.view.music.repository.MusicPagingSource
import com.sion.itunes.view.music.repository.PageKeyedRemoteMediator
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class MusicViewModel(
    private val searchApiRepository: ISearchApiRepository, private val itunesDb: ItunesDb
) : BaseViewModel() {
    fun search(keyword: String, throughDb: Boolean = false): Flow<PagingData<Music>> {
        return if (throughDb) {
            searchThroughDb(keyword)
        } else {
            searchInMemory(keyword)
        }
    }

    private fun searchInMemory(keyword: String): Flow<PagingData<Music>> {
        return Pager(
            config = PagingConfig(
                pageSize = SearchApiRepository.NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MusicPagingSource(searchApiRepository, keyword) }
        ).flow.cachedIn(viewModelScope)
    }

    private fun searchThroughDb(keyword: String): Flow<PagingData<Music>> {
        return Pager(
            config = PagingConfig(
                pageSize = SearchApiRepository.NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = PageKeyedRemoteMediator(itunesDb, searchApiRepository, keyword)
        ) {
            itunesDb.musics().musicsByKeyword(keyword)
        }.flow.cachedIn(viewModelScope)
    }
}