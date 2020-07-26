package com.drumpads24.markkhakimulin.ui.tracklist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drumpads24.markkhakimulin.data.model.TrackInfo
import com.drumpads24.markkhakimulin.data.repository.TrackListRepository
import com.drumpads24.markkhakimulin.util.Coroutines.ioThenMain
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class TrackListViewModel(
    private val repository: TrackListRepository): ViewModel() {
    private val mCurrentTrack :MutableLiveData<TrackInfo> = MutableLiveData<TrackInfo>()
    val currentTrack: MutableLiveData<TrackInfo>
        get() = mCurrentTrack

    private var _tracks : MutableLiveData<ArrayList<TrackInfo>>  =
         MutableLiveData<ArrayList<TrackInfo>>()
    val tracks: MutableLiveData<ArrayList<TrackInfo>>
        get() = _tracks

    private lateinit var job: Job

    fun getTracklist() {
        if (tracks.value == null)
            job = ioThenMain(
                { repository.getTracklist() },
                { _tracks.value = it }
            )
        }

        override fun onCleared() {
            super.onCleared()
            if(::job.isInitialized) job.cancel()
        }


    fun setCurrentTrackInfo(index: Int) {

        if (currentTrack.value != null) {
            //pause/play current track
            playPauseCurrentTrack(false)
        }

        val mTrack = if (index>0) {
            tracks.value!![index]
        } else null

        if (mTrack != currentTrack.value) {

            //the following cycle is for recycler view binding update only
            for (ti:TrackInfo in tracks.value!!){
                ti.isPlaying = false
            }
            //

            currentTrack.value  = mTrack
            if (mTrack != null) {
                playPauseCurrentTrack(true)
            } else {
                viewModelScope.launch {
                    repository.stop()
                }
            }
        }
    }


    fun stopCurrentTrack() {
        setCurrentTrackInfo(-1)
    }

    fun playPauseCurrentTrack(isNew:Boolean) {

        if (isNew) {
            viewModelScope.launch {
                repository.stop()
                repository.play(currentTrack.value!!.audio, null)
            }
            currentTrack.value!!.isAudioLoading = true
        } else {

            if (currentTrack.value!!.isPlaying) {
                viewModelScope.launch {
                    repository.pause()
                }

            } else {
                viewModelScope.launch {
                    repository.resume()
                }
            }
        }
        currentTrack.value!!.isPlaying = !currentTrack.value!!.isPlaying
        currentTrack.value?.notifyChange()

    }
}