package com.android.notes.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity(tableName="note")
data class Note(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val name:String="Новая заметка",
    val description:String = "",
    val date: Date = Date()
)