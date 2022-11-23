package org.prometheus.videoplayer.ui

import android.content.Intent
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.prometheus.videoplayer.R
import org.prometheus.videoplayer.data.VideoInfo
import org.prometheus.videoplayer.ui.adapter.VideoListAdapter
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var rvVideoList: RecyclerView

    private lateinit var adapter: VideoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvVideoList = findViewById(R.id.rv_video_list)
        rvVideoList.layoutManager = LinearLayoutManager(this)
        adapter = VideoListAdapter(emptyList()) { videoInfo ->
            val intent = Intent(this, VideoPlayerActivity::class.java)
            intent.putExtra("video_info", videoInfo)
            startActivity(intent)
        }

        rvVideoList.adapter = adapter

        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startScanVideos()
            } else {
                Toast.makeText(this, "无权限", Toast.LENGTH_SHORT).show()
            }
        }.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun startScanVideos() {
        val files =
            File(Environment.getExternalStorageDirectory().absolutePath + "/Movies/").listFiles()
                ?: return
        val videoList = mutableListOf<VideoInfo>()
        files.filter { file ->
            checkVideoFileValid(file)
        }.forEach { file ->
            videoList.add(VideoInfo(file.absolutePath, file.name))
        }
        adapter.setData(videoList)
    }

    private fun checkVideoFileValid(file: File): Boolean {
        if (!file.isFile) {
            return false
        }
        val metadataRetriever = MediaMetadataRetriever()
        metadataRetriever.setDataSource(file.absolutePath)
        val hasVideo =
            metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO)
        return TextUtils.equals(hasVideo, "yes")
    }
}