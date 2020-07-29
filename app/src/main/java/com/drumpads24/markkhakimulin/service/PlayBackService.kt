package com.drumpads24.markkhakimulin.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import com.drumpads24.markkhakimulin.data.model.TrackInfo
import kotlin.math.roundToInt


class PlayBackService : Service() {

    inner class PlayerBinder : Binder() {
        fun getService() : PlayBackService {
            return this@PlayBackService
        }
    }
    private var handlerDuration: Handler = Handler()

    private lateinit var mediaPlayer: MediaPlayer
    private val playerBind :IBinder = PlayerBinder()
    private lateinit var mTrackInfo:TrackInfo


    override fun onBind(intent: Intent?): IBinder? {
        return playerBind;
    }

    override fun onUnbind(intent: Intent?): Boolean {
        mediaPlayer.stop();
        mediaPlayer.release();
        return false;
    }
    override fun onCreate() {
        super.onCreate()

        mediaPlayer = MediaPlayer()

    }


    fun play(trackInfo: TrackInfo) {

        mTrackInfo = trackInfo
        mediaPlayer.setDataSource(trackInfo.audio)
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
            if (trackInfo.playBackCallback != null) {
                trackInfo.playBackCallback?.onPrepared()
            }
            if (mediaPlayer.isPlaying) {
                handlerDuration.postDelayed(DurationRunnable(),1000)
            }

        }
        mediaPlayer.setOnCompletionListener {

            if (trackInfo.playBackCallback != null && !mediaPlayer.isPlaying) {
                trackInfo.playBackCallback?.onCompleted()
            }
        }
        mediaPlayer.setOnErrorListener { _, what, _ ->

            if (trackInfo.playBackCallback != null && !mediaPlayer.isPlaying) {
                trackInfo.playBackCallback?.onError(trackInfo,what)
            }
            true
        }

        mediaPlayer.prepareAsync()
    }
    fun pause() {
        mediaPlayer.pause()
    }
    fun stop() {
        mediaPlayer.stop()
        mediaPlayer.reset()
    }
    fun resume() {
        if (mediaPlayer.currentPosition >= mediaPlayer.duration) {
            mediaPlayer.reset()
            play(mTrackInfo)
            return
        }

        mediaPlayer.start()
        if (mediaPlayer.isPlaying) {
            handlerDuration.postDelayed(DurationRunnable(),1000)
        }
}

    override fun onDestroy() {
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    inner class DurationRunnable() :Runnable {

        override fun run() {
            var progress: Int = 0
            try {
                progress =
                    ((mediaPlayer.currentPosition.toDouble() / mediaPlayer.duration.toDouble()) * 100).roundToInt()
            }catch (e:Exception) {}
            if (mTrackInfo.playBackCallback != null) {
                mTrackInfo.playBackCallback?.onProgress(progress)
            }
            if (mediaPlayer.isPlaying) {
                handlerDuration.postDelayed(DurationRunnable(),1000)
            }
        }
    }

    interface PlayBackCallback {
        fun onPrepared()
        fun onCompleted()
        fun onError(trackInfo: TrackInfo,err:Any?)
        fun onProgress(value: Int)
    }
}
