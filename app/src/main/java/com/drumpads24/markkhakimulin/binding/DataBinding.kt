package com.drumpads24.markkhakimulin.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drumpads24.markkhakimulin.data.model.TrackInfo

class DataBinding {
    companion object {

        @JvmStatic
        @BindingAdapter("android:bindItems")
        fun bindItems(view: RecyclerView, items: List<TrackInfo>?){
            items?.apply {
                view.adapter?.also { adapter ->
                    adapter.notifyDataSetChanged()
                }
            }
        }

    }
}