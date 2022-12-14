package com.android.notes.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.android.notes.data.Note
import com.android.notes.data.api.NoteApi
import com.android.notes.data.dao.NoteDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
private const val TAG = "NoteWorkManager"
@HiltWorker
class NoteWorkManager @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val noteDao: NoteDao,
    private val noteApi: NoteApi,): CoroutineWorker(appContext,params) {
    override suspend fun doWork(): Result {
        delay(1000)
        setProgress(workDataOf("Progress" to 10))
        delay(1000)
        setProgress(workDataOf("Progress" to 43))
        delay(2000)
        setProgress(workDataOf("Progress" to 95))
        delay(500)
        setProgress(workDataOf("Progress" to 100))
        delay(500)
        try {
            noteDao.addNote(
                Note(
                    UUID.fromString("7e5b819a-d447-43e6-bc8b-2c2a0cf04c4c"), "Еда", "Купить еду",
                    SimpleDateFormat(
                        "dd.MM.yyyy hh:mm",
                        Locale.ENGLISH
                    ).parse("05.10.2022 15:20") as Date
                )
            )
        }
        catch (e:Exception){
//            e.printStackTrace()
        }
        try{
            noteDao.addNote(
                Note(
                    UUID.fromString("e1dfad05-0024-4df2-ad02-f68d04d9d6dd"),
                    "Убрать",
                    "Комната, кухня",
                    SimpleDateFormat(
                        "dd.MM.yyyy hh:mm",
                        Locale.ENGLISH
                    ).parse("02.09.2022 13:14") as Date
                )
            )
        }
        catch (e:Exception){
//            e.printStackTrace()
        }
        try{
            noteDao.addNote(
                Note(
                    UUID.fromString("eaa55041-b6b1-43da-a41f-ea0561bc4f27"),
                    "Постирать", "Мои вещи и членов семьи", Date()
                )
            )
        }
        catch (e:Exception){
//            e.printStackTrace()
        }
        try {
            noteDao.addNote(
                Note(
                    UUID.fromString("3c2085af-88d7-4403-b728-62b7b18d85df"),
                    "Сделать конспект",
                    "Електроника",
                    SimpleDateFormat(
                        "dd.MM.yyyy hh:mm",
                        Locale.ENGLISH
                    ).parse("01.03.2022 22:07") as Date
                )
            )
        }
        catch (e:Exception){
//            e.printStackTrace()
        }
//    loadData()
        return Result.success()
    }

    private fun loadData() {
        noteApi.getNotes().enqueue(object :Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                //получаем данные
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d(TAG,"$t")
            }

        })
    }

}