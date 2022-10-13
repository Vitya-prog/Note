package com.android.notes.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager
import com.android.notes.R
import dagger.hilt.android.AndroidEntryPoint
private const val TAG = "MainActivity"
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        isAgain = sp.getBoolean("isAgain",false)
        Log.d(TAG,"$isAgain")
        val e = sp.edit()
        e.putBoolean("isAgain",true)
        e.apply()
    }
    companion object{
        var isAgain = false
    }
}