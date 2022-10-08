package com.android.notes.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.android.notes.data.Note
import java.util.*

@Dao
interface NoteDao {
@Query("SELECT * FROM note")
fun getNotesList(): LiveData<List<Note>>
@Query("SELECT * FROM note WHERE id =:id")
fun getNote(id: UUID):LiveData<Note>
@Update
fun updateNote(note: Note)
@Delete
fun deleteNote(note: Note)
@Insert
fun addNote(note: Note)
}