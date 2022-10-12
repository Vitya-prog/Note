package com.android.notes.di

import com.android.notes.data.api.NoteApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl("http://example.com")
            .build()
    }
    @Provides
    fun provideNoteApi(retrofit: Retrofit):NoteApi{
        return retrofit.create(NoteApi::class.java)
    }

}