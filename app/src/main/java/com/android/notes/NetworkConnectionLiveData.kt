package com.android.notes

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData

class NetworkConnectionLiveData(val context:Context): LiveData<Boolean>() {
    private var connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActive() {
        super.onActive()
        updateConnection()
        context.registerReceiver(networkReceiver,
            IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        )
    }

    override fun onInactive() {
        super.onInactive()
        context.unregisterReceiver(networkReceiver)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun updateConnection() {
        val activeNetwork = connectivityManager.activeNetwork
        postValue(activeNetwork != null)
    }

    private val networkReceiver = object : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.M)
        override fun onReceive(context: Context, intent: Intent) {
            updateConnection()
        }
    }
}