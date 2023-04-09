package com.example.ct

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import java.util.*

class MusicBrowser(private val context: Context, private val triggerManager: TriggerManager) {
    private var mediaBrowser: MediaBrowserCompat? = null

    private val connectionCallback = object : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            mediaBrowser?.sessionToken?.let { token ->
                val mediaController = MediaControllerCompat(context, token)
                mediaController.registerCallback(controllerCallback)
            }
        }
    }

    private val controllerCallback = object : MediaControllerCompat.Callback() {
        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            val genre = metadata?.getString(MediaMetadataCompat.METADATA_KEY_GENRE)
            if (genre?.toLowerCase(Locale.getDefault()) == "rock") {
                triggerManager.checkMusicTrigger()
            }
        }
    }

    fun connect() {
        val componentName = ComponentName(
            "com.example.musicplayer",
            "com.example.musicplayer.MediaBrowserService"
        )
        mediaBrowser = MediaBrowserCompat(context, componentName, connectionCallback, Bundle.EMPTY).apply {
            connect()
        }
    }

    fun disconnect() {
        mediaBrowser?.disconnect()
        mediaBrowser = null
    }
}
