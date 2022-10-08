package com.android.notes.repository

import androidx.lifecycle.LiveData
import com.android.notes.data.Note
import com.android.notes.data.dao.NoteDao
import java.util.UUID
import java.util.concurrent.Executors
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val noteDao: NoteDao
) {
    private val executor =  Executors.newSingleThreadExecutor()
    fun getNotesList(): LiveData<List<Note>>{
        return noteDao.getNotesList()
    }
    fun getNote(id:UUID):LiveData<Note>{
        return noteDao.getNote(id)
    }
    fun addNote(note: Note){
        executor.execute{
            noteDao.addNote(note)
        }
    }
    fun updateNote(note: Note){
        executor.execute{
            noteDao.updateNote(note)
        }
    }
    fun deleteNote(note: Note){
        executor.execute {
            noteDao.deleteNote(note)
        }
    }

}