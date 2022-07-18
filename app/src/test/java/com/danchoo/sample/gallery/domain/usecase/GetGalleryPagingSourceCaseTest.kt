package com.danchoo.sample.gallery.domain.usecase

import com.danchoo.sample.gallery.data.datasource.GalleryPagingSource
import com.danchoo.sample.gallery.domain.repository.GalleryRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

class GetGalleryPagingSourceCaseTest {
    private val repository: GalleryRepository = mockk()

    @Test
    fun testGetGalleryPagingSourceCase() {

        val useCase = GetGalleryPagingSourceCase(repository)
        val pagingSource = GalleryPagingSource(mockk())

        every { repository.getGalleryPagingSource() } returns pagingSource

        Assert.assertEquals(useCase.invoke(), pagingSource)
    }
}