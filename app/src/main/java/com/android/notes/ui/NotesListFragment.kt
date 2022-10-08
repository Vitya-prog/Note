package com.android.notes.ui



import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.notes.R
import com.android.notes.adapter.NoteListAdapter
import com.android.notes.data.Note
import com.android.notes.databinding.FragmentNoteListBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


private const val TAG ="NotesListFragment"
@AndroidEntryPoint
class NotesListFragment : Fragment(),NoteListAdapter.OnItemClickListener {

    private lateinit var binding: FragmentNoteListBinding
    private val notesListViewModel:NotesListViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteListBinding.inflate(inflater,container,false)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        val swipeToDeleteCallBack = object:SwipeToDeleteCallBack(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                binding.recyclerView.adapter?.notifyItemRemoved(position)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notesListViewModel.notesList.observe(viewLifecycleOwner){ notes ->
            Log.d(TAG,"$notes")
            mockData(notes)
            val adapter = NoteListAdapter(this)
            adapter.submitList(notes)
            binding.recyclerView.adapter = adapter
        }

    }
    override fun onStart() {
        super.onStart()
        binding.addButton.setOnClickListener {
            notesListViewModel.addNote(Note())
        }
    }

    private fun mockData(notes:List<Note>) {
        if (notes.isEmpty()){
            notesListViewModel.addNote(
            Note(UUID.randomUUID(),"Еда","Купить еду",
             SimpleDateFormat("dd.MM.yyyy hh:mm",Locale.ENGLISH).parse("05.10.2022 15:20") as Date))
            notesListViewModel.addNote(
                Note(UUID.randomUUID(),"Убрать","Комната, кухня",
                SimpleDateFormat("dd.MM.yyyy hh:mm",Locale.ENGLISH).parse("02.09.2022 13:14") as Date))
            notesListViewModel.addNote(
                Note(UUID.randomUUID(),"Постирать","Мои вещи и членов семьи",Date()))
            notesListViewModel.addNote(
                Note(UUID.randomUUID(),"Сделать конспект","Електроника",
                    SimpleDateFormat("dd.MM.yyyy hh:mm",Locale.ENGLISH).parse("01.03.2022 22:07") as Date))
        }

    }

    override fun onItemClickListener(note: Note) {
        val bundle = bundleOf("id" to note.id.toString())
        view?.findNavController()?.navigate(R.id.action_notesListFragment_to_noteFragment,bundle)
    }


}