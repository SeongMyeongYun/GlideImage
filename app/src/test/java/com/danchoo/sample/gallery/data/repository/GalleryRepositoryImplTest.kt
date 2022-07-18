package com.danchoo.sample.gallery.data.repository

import android.database.Cursor
import com.danchoo.sample.gallery.data.datasource.GalleryDataSource
import com.danchoo.sample.gallery.domain.model.GalleryItemModel
import com.danchoo.sample.gallery.domain.repository.GalleryRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GalleryRepositoryImplTest {
    private val resultList = mutableListOf<GalleryItemModel>()
    private val dataSource: GalleryDataSource = mockk()
    private val repository: GalleryRepository = GalleryRepositoryImpl(dataSource)

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
    }

    @Test
    fun testGetGalleryItemListWithCount() = runTest {
        val count = 30

        coEvery { dataSource.getGalleryItemList(count) } returns flow {
            emit(resultList.subList(0, count))
        }

        val resultFlow = repository.getGalleryItemList(count)
        val result = resultFlow.first()

        Assert.assertEquals(result.size, count)
        Assert.assertEquals(result, resultList.subList(0, count))
    }

    @Test
    fun testGetGalleryItemListWithStartAndCount() = runTest {
        val count = 30

        every { dataSource.getGalleryItemList(0, count) } returns resultList.subList(0, count)

        val result = repository.getGalleryItemList(0, count)


        Assert.assertEquals(result.size, count)
        Assert.assertEquals(result, resultList.subList(0, count))
    }

    @Test
    fun testGetGalleryItemListWithCursor() = runTest {
        val count = 30
        val cursor: Cursor = mockk()

        every { dataSource.getGalleryItemList(cursor, 0, count) } returns resultList.subList(
            0,
            count
        )

        val result = repository.getGalleryItemList(cursor, 0, count)


        Assert.assertEquals(result.size, count)
        Assert.assertEquals(result, resultList.subList(0, count))
    }

    @Test
    fun testGetGalleryPagingSource() {
        val result = repository.getGalleryPagingSource()
        Assert.assertNotNull(result)
    }
}