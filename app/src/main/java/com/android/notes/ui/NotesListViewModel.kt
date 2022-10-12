package com.android.notes.ui



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.notes.data.Note
import com.android.notes.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel()  {
        val notesList = noteRepository.getNotesListLiveData()
        fun addNote(note: Note){
            noteRepository.addNote(note)
        }
    suspend fun swipeToDeleteNote(position: Int) {
        val notesDeferred = viewModelScope.async {
            noteRepository.getNotesList()
        }
        val noteList = notesDeferred.await()
      noteRepository.deleteNote(noteList[position])
    }
}