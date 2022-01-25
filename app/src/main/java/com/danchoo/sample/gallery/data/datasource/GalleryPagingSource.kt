package com.danchoo.sample.gallery.data.datasource

import android.database.Cursor
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.danchoo.sample.gallery.domain.model.GalleryItemModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GalleryPagingSource(
    private val localDataSource: GalleryDataSource
) : PagingSource<Int, GalleryItemModel>() {
    private val cursor: Cursor by lazy { localDataSource.getDefaultCursor() }

    override fun getRefreshKey(state: PagingState<Int, GalleryItemModel>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GalleryItemModel> {
        return withContext(Dispatchers.IO) {
            val key = params.key ?: 0
            val galleryItemList = getGalleryList(key, params.loadSize)

            val prevKey = if (key == 0) null else key - 1
            val nextKey = if (galleryItemList.isNullOrEmpty()) null else key + 1

            try {
                LoadResult.Page(
                    data = galleryItemList,
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }
    }

    private fun getGalleryList(key: Int, loadSize: Int): List<GalleryItemModel> {
        val offset = key * loadSize
        val galleryItemList = localDataSource.getGalleryItemList(cursor = cursor, offset, loadSize)

        return galleryItemList
    }
}