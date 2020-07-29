package com.drumpads24.markkhakimulin.data.repository

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.drumpads24.markkhakimulin.R
import com.drumpads24.markkhakimulin.data.model.TrackInfo
import com.drumpads24.markkhakimulin.service.PlayBackService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class TrackListRepository(internal val context: Context?) {

    private var playIntent: Intent = Intent(context, PlayBackService::class.java)
    private lateinit var playSrv: PlayBackService
    var playerBounded : Boolean = false

    private val playerConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder: PlayBackService.PlayerBinder = service as PlayBackService.PlayerBinder
            //get service
            playSrv = binder.getService()

            playerBounded = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            playerBounded = false
        }
    }

    init {
        start()
    }

    suspend fun getTracklist() :ArrayList<TrackInfo> {

        val itemsListType: Type =
            object : TypeToken<ArrayList<TrackInfo?>?>() {}.type

        val objectArrayString: String = context!!.resources.openRawResource(R.raw.test_files_config).bufferedReader().use { it.readText() }

        return Gson().fromJson(objectArrayString, itemsListType)

    }

    suspend fun play(trackInfo: TrackInfo):Boolean {
        if (playerBounded) {
            playSrv.play(trackInfo)
        }
        return playerBounded
    }
    suspend fun pause():Boolean {
        if (playerBounded) {
            playSrv.pause()
        }
        return playerBounded
    }
    suspend fun resume():Boolean {
        if (playerBounded) {
            playSrv.resume()
        }
        return playerBounded
    }
    suspend  fun stop():Boolean {
        if (playerBounded) {
            playSrv.stop()
        }
        return playerBounded
    }
    private fun start() {
        context?.bindService(playIntent, playerConnection, Context.BIND_AUTO_CREATE)
        context?.startService(playIntent)
    }
    fun release()
    {
        context?.unbindService(playerConnection)
        context?.stopService(playIntent)
    }




}