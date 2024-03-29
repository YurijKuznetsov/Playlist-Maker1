package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale


class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val artistName: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)
    private val trackImage: ImageView = itemView.findViewById(R.id.track_image)
    fun bind(model: Track, onclickTrackInterface: SearchAdapter.OnclickTrackInterface) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis)
        val cornerRadius = itemView.resources.getDimension(R.dimen.padding_02)
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(cornerRadius.toInt()))
            .placeholder(R.drawable.placeholder)
            .into(trackImage)
        itemView.setOnClickListener {
            onclickTrackInterface.onClickTrack(model)
        }

    }
}



