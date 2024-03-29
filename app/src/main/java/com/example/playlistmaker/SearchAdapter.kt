package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SearchAdapter(
    var tracks : List<Track>,
    private val onclickTrackInterface: OnclickTrackInterface
) : RecyclerView.Adapter<SearchViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track, parent, false)
        return SearchViewHolder(view)
    }
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(tracks[position], onclickTrackInterface)
    }
    override fun getItemCount(): Int {
        return tracks.size
    }
    interface OnclickTrackInterface {
        fun onClickTrack(track: Track)
    }
}

