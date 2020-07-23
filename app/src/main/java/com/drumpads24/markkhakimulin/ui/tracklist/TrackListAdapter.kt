package com.drumpads24.markkhakimulin.ui.tracklist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.transform.CircleCropTransformation
import com.drumpads24.markkhakimulin.R
import com.drumpads24.markkhakimulin.data.model.TrackInfo
import com.drumpads24.markkhakimulin.databinding.TrackListItemBinding

class TrackListAdapter (
    private var tracks: List<TrackInfo>,
    private val listener: TrackClickListener
) : RecyclerView.Adapter<TrackListAdapter.TrackListHolder>(){

    fun updateTrackList(updatedTracks: List<TrackInfo>) {
        tracks = updatedTracks
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getItemCount() = tracks.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListHolder {

        return TrackListHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.track_list_item,
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: TrackListHolder, position: Int) {

            holder.trackListItemBinding.trackInfo  = tracks[position]

            holder.trackListItemBinding.imageButton.setOnClickListener {

                tracks[position].isPlaying = !tracks[position].isPlaying

                listener.onRecyclerViewItemClick(
                    holder.trackListItemBinding.imageButton,
                    tracks[position]
                )
            }
    }

    inner class TrackListHolder(
        val trackListItemBinding: TrackListItemBinding
    ) : RecyclerView.ViewHolder(trackListItemBinding.root)


    companion object{
        @BindingAdapter("image")
        @JvmStatic
        fun loadImage(view: ImageView, url: String){
            view.load(url) {
                crossfade(true)
                //placeholder(R.drawable.no_image)
                //transformations(CircleCropTransformation())
            }
        }
    }


}