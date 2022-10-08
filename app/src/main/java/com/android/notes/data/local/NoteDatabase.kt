package com.android.notes.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.notes.data.Note
import com.android.notes.data.dao.NoteDao

@Database(version = 1, entities = [Note :: class],exportSchema = false)
@TypeConverters(NoteTypeConverter :: class)
abstract class NoteDatabase:RoomDatabase() {

    abstract fun noteDao():NoteDao
}
