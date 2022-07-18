package com.danchoo.sample.gallery.data.datasource

import android.database.Cursor
import androidx.paging.PagingSource
import com.danchoo.sample.gallery.domain.model.GalleryItemModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GalleryPagingSourceTest {

    private val dataSource: GalleryDataSource = mockk()
    private val cursor: Cursor = mockk()
    private val resultList = mutableListOf<GalleryItemModel>()

    @Before
    fun setup() {
        for (index in 0..100) {
            resultList.add(
                GalleryItemModel(
                    id = index,
                    name = "GalleryItemModel $index"
                )
            )
        }

        every { dataSource.getDefaultCursor() } returns cursor
    }


    @Test
    fun testGalleryPagingSource() = runTest {
        val pagingSource = GalleryPagingSource(dataSource)

        val key = 0
        val loadSize = 30
        val offset = key * loadSize

        val result = resultList.subList(offset, loadSize)
        every { dataSource.getGalleryItemList(cursor, key, loadSize) } returns result


        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = key,
                loadSize = loadSize,
                placeholdersEnabled = false
            )
        ) as PagingSource.LoadResult.Page


        Assert.assertEquals(loadSize, loadResult.data.size)
        Assert.assertEquals(loadSize, result.size)
        Assert.assertArrayEquals(loadResult.data.toTypedArray(), result.toTypedArray())
    }
}