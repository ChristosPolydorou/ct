package com.example.ct

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.Builder
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.MediaBrowserServiceCompat

class MyMediaBrowserServiceCompat : MediaBrowserServiceCompat() {

    private lateinit var mediaSession: MediaSessionCompat

    private val mediaList = listOf(
        MediaMetadataCompat.Builder()
            .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, "1")
            .putString(MediaMetadataCompat.METADATA_KEY_TITLE, "Rock Song")
            .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "Rock Band")
            .putString(MediaMetadataCompat.METADATA_KEY_GENRE, "Rock")
            .build(),

        MediaMetadataCompat.Builder()
            .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, "2")
            .putString(MediaMetadataCompat.METADATA_KEY_TITLE, "Pop Song")
            .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "Pop Artist")
            .putString(MediaMetadataCompat.METADATA_KEY_GENRE, "Pop")
            .build()
    )

    override fun onCreate() {
        super.onCreate()

        mediaSession = MediaSessionCompat(this, "MyMediaBrowserServiceCompat").apply {
            setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
            setCallback(object : MediaSessionCompat.Callback() {
                // Implement playback controls like play, pause, skip, etc.
            })
            setSessionToken(sessionToken)
        }
    }

    override fun onDestroy() {
        mediaSession.release()
        super.onDestroy()
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        return BrowserRoot("root", null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<List<MediaItem>>
    ) {
        if (parentId == "root") {
            val items = mediaList.map { metadata ->
                MediaItem(metadata.description, MediaItem.FLAG_PLAYABLE)
            }
            result.sendResult(items)
        } else {
            result.sendResult(null)
        }
    }
}