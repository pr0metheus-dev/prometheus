package org.prometheus.videoplayer.ui

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import org.prometheus.videoplayer.R
import org.prometheus.videoplayer.data.VideoInfo
import java.io.File


class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var playerView: StyledPlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)
        playerView = findViewById(R.id.player_view)
        val videoInfo: VideoInfo = intent.getSerializableExtra("video_info") as VideoInfo
        val mediaItem = MediaItem.fromUri(Uri.fromFile(File(videoInfo.path)))
        val player = ExoPlayer.Builder(this).build()
        playerView.setBackgroundColor(Color.BLACK)
        playerView.controllerShowTimeoutMs = 2500
        playerView.player = player
        playerView.player?.apply {
            setMediaItem(mediaItem)
            prepare()
            play()
        }
    }

}