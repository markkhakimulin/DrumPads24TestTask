package com.drumpads24.markkhakimulin.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder



class PlayBackService : Service() ,
    MediaPlayer.OnCompletionListener,
    MediaPlayer.OnErrorListener{

    inner class PlayerBinder : Binder() {
        fun getService() : PlayBackService {
            return this@PlayBackService
        }
    }
    private lateinit var mediaPlayer: MediaPlayer
    private val playerBind :IBinder = PlayerBinder()

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
        mediaPlayer.setOnCompletionListener(this)
    }


    fun play(audio:String,callback:OnPrepareComplete?) {
        mediaPlayer.setDataSource(audio)
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
            callback?.onCompletion()
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
        mediaPlayer.start()
    }


    override fun onCompletion(mp: MediaPlayer?) {
        stop()
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        mediaPlayer.stop()
        mediaPlayer.release()
        return false
    }

    override fun onDestroy() {
        mediaPlayer.stop()
        mediaPlayer.release()
    }
    interface OnPrepareComplete {
        fun onCompletion()
    }
}
