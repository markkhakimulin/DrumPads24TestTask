package com.drumpads24.markkhakimulin.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

object Coroutines {

    fun<T: Any> ioThenMain(work: suspend (() -> T?), callback: ((T?)->Unit),exception: (Exception)->Unit) =
        CoroutineScope(Dispatchers.Main).launch {

            val data = CoroutineScope(Dispatchers.IO).async rt@{ return@rt work() }
            try {
                val res = data.await()
                callback(res)
            } catch(e: Exception) {
                exception(e)
            }

        }


}