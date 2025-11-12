package org.example.app.ui

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import org.example.app.R

/**
 * Simple highlight video player using Media3 ExoPlayer.
 */
class HighlightPlayerActivity : FragmentActivity() {

    companion object {
        const val EXTRA_URL = "extra_video_url"
    }

    private var player: ExoPlayer? = null
    private lateinit var playerView: PlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        playerView = findViewById(R.id.player_view)
    }

    override fun onStart() {
        super.onStart()
        val url = intent.getStringExtra(EXTRA_URL)
        val mediaItem = MediaItem.fromUri(Uri.parse(url ?: "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"))
        player = ExoPlayer.Builder(this).build().also { p ->
            playerView.player = p
            p.setMediaItem(mediaItem)
            p.prepare()
            p.playWhenReady = true
        }
    }

    override fun onStop() {
        super.onStop()
        playerView.player = null
        player?.release()
        player = null
    }
}
