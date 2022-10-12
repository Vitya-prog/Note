package com.android.notes

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.android.notes.worker.NoteWorkManager

private const val TAG = "NoteBroadcastReceiver"
class NoteBroadcastReceiver(private val listener:OnChangeUi) : BroadcastReceiver() {
    interface OnChangeUi {
        fun onChange()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context, intent: Intent) {
        if (isOnline(context)){
            val workRequest = OneTimeWorkRequestBuilder<NoteWorkManager>()
                .build()
            WorkManager.getInstance(context).enqueue(workRequest)
           Log.d(TAG,"Online")
        } else {
            listener.onChange()
            Log.d(TAG,"Offline")
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isOnline(context: Context):Boolean{
        return try{
            val check = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = check.activeNetwork
            netInfo != null
        } catch (e:Exception){
            false
        }
    }
}