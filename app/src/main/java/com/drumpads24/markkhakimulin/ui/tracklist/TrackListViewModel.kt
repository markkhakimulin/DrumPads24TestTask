package com.drumpads24.markkhakimulin.ui.tracklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.drumpads24.markkhakimulin.data.model.TrackInfo
import com.drumpads24.markkhakimulin.data.repository.TrackListRepository
import kotlinx.coroutines.*

class TrackListViewModel(
    private val repository: TrackListRepository): ViewModel() {

    private val _currentTrackInfo: MutableLiveData<TrackInfo?> by lazy {
        MutableLiveData<TrackInfo?>()
    }
    val currentTrackInfo: MutableLiveData<TrackInfo?>
        get() = _currentTrackInfo

    private val _tracks = MutableLiveData<ArrayList<TrackInfo>>()
    val tracks: LiveData<ArrayList<TrackInfo>>
        get() = _tracks

    private lateinit var job: Job


    fun getTracklist() {
            job = ioThenMain(
                { repository.getTracklist() },
                {_tracks.value = it }
            )
        }

        override fun onCleared() {
            super.onCleared()
            if(::job.isInitialized) job.cancel()
        }

        fun<T: Any> ioThenMain(work: suspend (() -> T?), callback: ((T?)->Unit)) =
            CoroutineScope(Dispatchers.Main).launch {
            val data = CoroutineScope(Dispatchers.IO).async  rt@{
                return@rt work()
            }.await()
            callback(data)
        }

    fun updateTrackInfo(trackInfo: TrackInfo) {

        for (i:TrackInfo in _tracks.value!!) {
            if (! trackInfo.equals(i)) {
                i.isPlaying = false
            } else {
                i.isPlaying = trackInfo.isPlaying
            }
        }
        _tracks.value = _tracks.value;
    }

    fun setCurrentTrackInfo(trackInfo: TrackInfo?) {
        _currentTrackInfo.value = trackInfo
    }

    fun switchStopPlayCurrentTrackInfo() {
        if (_currentTrackInfo.value != null) {
            _currentTrackInfo.value!!.isPlaying = !_currentTrackInfo.value!!.isPlaying
            setCurrentTrackInfo(_currentTrackInfo.value)
            updateTrackInfo(_currentTrackInfo.value!!)
        }

    }
}