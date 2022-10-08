package com.android.notes.di

import android.app.Application
import androidx.room.Room
import com.android.notes.data.dao.NoteDao
import com.android.notes.data.local.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application):NoteDatabase{
        return Room.databaseBuilder(
            application.applicationContext,
            NoteDatabase::class.java,
            "notes"
        ).build()
    }

    @Provides
    fun provideNoteDao(noteDatabase: NoteDatabase):NoteDao{
        return noteDatabase.noteDao()
    }

}