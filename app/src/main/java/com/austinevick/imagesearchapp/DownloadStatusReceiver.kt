package com.austinevick.imagesearchapp

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class DownloadStatusReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent?.action == "android.intent.action.DOWNLOAD_COMPLETE") {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id != -1L) {
                // Download completed successfully
                Log.d("DownloadStatusReceiver", "Download completed with ID: $id")
            }
        }

    }
}
