package com.android.notes.ui

import androidx.lifecycle.ViewModel
import com.android.notes.data.Note
import com.android.notes.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel()  {

        val notesList = noteRepository.getNotesList()
        fun addNote(note: Note){
            noteRepository.addNote(note)
        }
}