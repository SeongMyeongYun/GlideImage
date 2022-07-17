package com.danchoo.sample.gallery.data.datasource

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.danchoo.sample.gallery.domain.model.GalleryItemModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GalleryDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : GalleryDataSource {
    override fun getDefaultCursor(): Cursor {
        return getCursor(getMediaDefaultProjection())
    }

    override fun getGalleryItemList(count: Int): Flow<List<GalleryItemModel>> {
        return flow {
            val mediaItemList = mutableListOf<GalleryItemModel>()
            val projection = getMediaDefaultProjection()
            val cursor = getCursor(projection)

            while (cursor.moveToNext()) {
                val mediaItemModel = getMediaItemModel(cursor, projection)
                mediaItemList.add(mediaItemModel)

                if (mediaItemList.size == count) {
                    emit(mediaItemList.toList())
                    mediaItemList.clear()
                }
            }

            if (mediaItemList.isNotEmpty()) {
                emit(mediaItemList.toList())
            }

            cursor.close()
        }
    }

    override fun getGalleryItemList(start: Int, count: Int): List<GalleryItemModel> {
        val mediaItemList = mutableListOf<GalleryItemModel>()
        val projection = getMediaDefaultProjection()
        val cursor = getCursor(projection)

        cursor.moveToPosition(start - 1)

        while (cursor.moveToNext()) {
            val mediaItemModel = getMediaItemModel(cursor, projection)
            mediaItemList.add(mediaItemModel)

            if (mediaItemList.size == count) {
                break
            }
        }

        cursor.close()

        return mediaItemList
    }

    override fun getGalleryItemList(
        cursor: Cursor,
        start: Int,
        count: Int
    ): List<GalleryItemModel> {
        val mediaItemList = mutableListOf<GalleryItemModel>()
        val projection = getMediaDefaultProjection()

        cursor.moveToPosition(start - 1)

        while (cursor.moveToNext()) {
            val mediaItemModel = getMediaItemModel(cursor, projection)
            mediaItemList.add(mediaItemModel)

            if (mediaItemList.size == count) {
                break
            }
        }

        return mediaItemList
    }

    private fun getCursor(projection: Array<String>): Cursor {
        val selection = getAllMediaSelection()
        val resolver = context.contentResolver

        return resolver.query(
            MediaStore.Files.getContentUri("external"),
            projection,
            selection,
            null,
            MediaStore.Images.Media.DATE_MODIFIED + " desc"
        ) ?: throw Exception()
    }

    private fun getMediaItemModel(cursor: Cursor, projection: Array<String>): GalleryItemModel {
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(projection[0]))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(projection[1])) ?: ""
        val addedDate = cursor.getString(cursor.getColumnIndexOrThrow(projection[2])) ?: ""
        val modifiedDate =
            cursor.getString(cursor.getColumnIndexOrThrow(projection[3])) ?: ""
        val size = cursor.getLong(cursor.getColumnIndexOrThrow(projection[4]))
        val createDate = cursor.getLong(cursor.getColumnIndexOrThrow(projection[5]))
        val mediaType = cursor.getInt(cursor.getColumnIndexOrThrow(projection[6]))
        val mineType = cursor.getString(cursor.getColumnIndexOrThrow(projection[7])) ?: ""
        val columnIndexId = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)

        val uri = when (mediaType) {
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO -> {
                Uri.withAppendedPath(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    cursor.getLong(columnIndexId).toString()
                )
            }
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE -> {
                Uri.withAppendedPath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    cursor.getLong(columnIndexId).toString()
                )
            }
            else -> null
        }

        return GalleryItemModel(
            id = id,
            uri = uri,
            name = name,
            addedDate = addedDate,
            modifiedDate = modifiedDate,
            size = size,
            createDate = createDate,
            mediaType = mediaType,
            mineType = mineType
        )
    }

    private fun getMediaDefaultProjection(): Array<String> {
        return arrayOf(
            MediaStore.MediaColumns._ID,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.DATE_ADDED,
            MediaStore.MediaColumns.DATE_MODIFIED,
            MediaStore.MediaColumns.SIZE,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.MIME_TYPE
        )
    }

    private fun getAllMediaSelection(): String {
        return StringBuilder()
            .append(MediaStore.Files.FileColumns.MEDIA_TYPE)
            .append("=")
            .append(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
            .append(" OR ")
            .append(MediaStore.Files.FileColumns.MEDIA_TYPE)
            .append("=")
            .append(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)
            .toString()
    }
}