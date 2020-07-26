package com.drumpads24.markkhakimulin.ui.tracklist

import android.view.View
import com.drumpads24.markkhakimulin.data.model.TrackInfo

interface TrackClickListener {
    fun onRecyclerViewItemClick(position: Int)
}