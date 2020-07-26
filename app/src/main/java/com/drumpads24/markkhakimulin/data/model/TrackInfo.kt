package com.drumpads24.markkhakimulin.data.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

data class TrackInfo(
    @Bindable val audio: String,
    val id: Int,
    @Bindable val image: String,
    @Bindable val name: String,
    val path: String,
    @Bindable var isPlaying: Boolean = false,
    @Bindable var isAudioLoading:Boolean = false
): BaseObservable() {
    val isEmptyAudio:Boolean = audio == ""
    var index:Int = -1
}