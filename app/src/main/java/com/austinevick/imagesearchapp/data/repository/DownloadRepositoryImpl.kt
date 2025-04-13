package com.austinevick.imagesearchapp.data.repository

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import com.austinevick.imagesearchapp.domain.repository.DownloadRepository
import kotlin.jvm.java

class DownloadRepositoryImpl(private val context: Context) : DownloadRepository {

    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    override fun downloadFile(url: String): Long {

        val request = DownloadManager.Request(url.toUri())
            .setMimeType("image/*")
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle("MyImage")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "MyImage.jpg")
        return downloadManager.enqueue(request)
    }

}