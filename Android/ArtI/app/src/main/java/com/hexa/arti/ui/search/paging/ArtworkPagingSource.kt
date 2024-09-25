package com.hexa.arti.ui.search.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hexa.arti.data.model.artwork.Artwork
import com.hexa.arti.repository.ArtWorkRepository

class ArtworkPagingSource(
    private val artWorkRepository: ArtWorkRepository,
    private val query: String
) : PagingSource<Int, Artwork>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Artwork> {
        try {
            val page = params.key ?: 1
            artWorkRepository.getArtWorksByString(query).onSuccess { responseArtworks ->
                return LoadResult.Page(
                    data = responseArtworks,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (responseArtworks.isEmpty()) null else page + 1
                )
            }
            return LoadResult.Page(
                data = emptyList(),
                prevKey = null,
                nextKey = null
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Artwork>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}