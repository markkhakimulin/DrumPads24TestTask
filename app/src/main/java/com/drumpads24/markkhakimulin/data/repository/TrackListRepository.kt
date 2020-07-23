package com.drumpads24.markkhakimulin.data.repository

import android.content.Context
import com.drumpads24.markkhakimulin.R
import com.drumpads24.markkhakimulin.data.model.TrackInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class TrackListRepository(private val context: Context?) {

    suspend fun getTracklist() :ArrayList<TrackInfo> {

        val itemsListType: Type =
            object : TypeToken<ArrayList<TrackInfo?>?>() {}.type

        val objectArrayString: String = context!!.resources.openRawResource(R.raw.test_files_config).bufferedReader().use { it.readText() }

        return Gson().fromJson(objectArrayString, itemsListType)

    }


}