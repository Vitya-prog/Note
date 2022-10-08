package com.android.notes.ui


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.android.notes.R
import com.android.notes.data.Note
import com.android.notes.databinding.FragmentNoteBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

private const val TAG = "NoteFragment"
@AndroidEntryPoint
class NoteFragment : Fragment() {
    private var noteId:String? = null
    private lateinit var binding:FragmentNoteBinding
    private val noteViewModel:NoteViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteBinding.inflate(inflater,container,false)
        Log.d(TAG,"${arguments?.getString("id")}")
        noteId = arguments?.getString("id")
        noteViewModel.loadNote(UUID.fromString(noteId))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost:MenuHost = requireActivity()
        menuHost.addMenuProvider(object:MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fragment_note_list,menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId){
                    R.id.update_note->{
                        noteViewModel.updateNote(Note(
                            UUID.fromString(noteId),
                            binding.noteNameEditText.text.toString(),
                            binding.noteDescriptionEditText.text.toString(),
                            Date()
                        ))
                        view.findNavController().navigate(R.id.action_noteFragment_to_notesListFragment)
                        true
                    }
                    else -> {false}
                }
            }

        },viewLifecycleOwner)
        noteViewModel.note.observe(viewLifecycleOwner){
            binding.noteNameEditText.setText(it.name)
            binding.noteDescriptionEditText.setText(it.description)
        }
    }
private fun createMenuProvider(){

}

}