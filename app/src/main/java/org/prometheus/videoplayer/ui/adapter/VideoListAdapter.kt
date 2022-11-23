package org.prometheus.videoplayer.ui.adapter

import android.annotation.SuppressLint
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import org.prometheus.videoplayer.R
import org.prometheus.videoplayer.data.VideoInfo
import java.io.File

class VideoListAdapter(
    private var videoList: List<VideoInfo>,
    private val onItemClickListener: (VideoInfo) -> Unit
) :
    Adapter<VideoListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvDuration: TextView = itemView.findViewById(R.id.tv_duration)
        val ivThumbnail: ImageView = itemView.findViewById(R.id.iv_thumbnail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_video_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val videoInfo = videoList[position]

        holder.tvTitle.text = videoInfo.name
        val metadataRetriever = MediaMetadataRetriever()
        metadataRetriever.setDataSource(videoInfo.path)

        val duration =
            metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        holder.tvDuration.text = duration

        val thumbnail =
            ThumbnailUtils.createVideoThumbnail(File(videoInfo.path), Size(512, 384), null)
        holder.ivThumbnail.setImageBitmap(thumbnail)

        holder.itemView.setOnClickListener {
            onItemClickListener.invoke(videoInfo)
        }
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(videoList: List<VideoInfo>) {
        this.videoList = videoList
        notifyDataSetChanged()
    }
}