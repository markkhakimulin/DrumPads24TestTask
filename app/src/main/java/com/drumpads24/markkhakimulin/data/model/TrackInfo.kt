package com.drumpads24.markkhakimulin.data.model

data class TrackInfo(
    val audio: String,
    val id: Int,
    val image: String,
    val name: String,
    val path: String
) {
    val isEmptyAudio:Boolean = audio == ""
    var isPlaying:Boolean = false

    override fun equals(other: Any?): Boolean {
        return id == ((other as TrackInfo).id)
    }
}