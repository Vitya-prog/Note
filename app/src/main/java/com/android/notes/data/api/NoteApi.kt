package com.android.notes.data.api

import retrofit2.Call
import retrofit2.http.GET

interface NoteApi {
    @GET
    fun getNotes(): Call<String>

}