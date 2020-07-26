package com.drumpads24.markkhakimulin.ui.tracklist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.api.get
import com.drumpads24.markkhakimulin.R
import com.drumpads24.markkhakimulin.databinding.TrackListItemBinding
import com.drumpads24.markkhakimulin.util.Coroutines

class TrackListAdapter (
    private var viewModel: TrackListViewModel,
    private val listener: TrackClickListener
) : RecyclerView.Adapter<TrackListAdapter.TrackListHolder>(){

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getItemCount() =viewModel.tracks.value?.size?:0

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

        viewModel.tracks.value?.let {
            holder.trackListItemBinding.trackInfo = it[position]
            holder.trackListItemBinding.imageLoaded = false
            holder.trackListItemBinding.executePendingBindings()

            Coroutines.ioThenMain(
                { Coil.get(it[position].image)},
                { holder.trackListItemBinding.imageView.setImageDrawable(it)
                    holder.trackListItemBinding.imageLoaded = true}
            )
        }
        holder.trackListItemBinding.imageButton.setOnClickListener {
            listener.onRecyclerViewItemClick(position)
        }
    }

    inner class TrackListHolder(
        val trackListItemBinding: TrackListItemBinding
    ) : RecyclerView.ViewHolder(trackListItemBinding.root)

}