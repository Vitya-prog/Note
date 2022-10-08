package com.android.notes.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.android.notes.data.Note
import com.android.notes.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {
    private val noteId = MutableLiveData<UUID>()
    var note:LiveData<Note> =
        Transformations.switchMap(noteId){noteId->
            noteRepository.getNote(noteId)
        }

    fun loadNote(id:UUID){
        noteId.value = id
    }
    fun updateNote(note: Note){
        noteRepository.updateNote(note)
    }
}