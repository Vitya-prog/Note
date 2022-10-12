package com.android.notes.ui




import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.notes.NoteBroadcastReceiver
import com.android.notes.R
import com.android.notes.adapter.NoteListAdapter
import com.android.notes.data.Note
import com.android.notes.databinding.FragmentNoteListBinding
import dagger.hilt.android.AndroidEntryPoint


private const val TAG ="NotesListFragment"
@AndroidEntryPoint
class NotesListFragment : Fragment(),NoteListAdapter.OnItemClickListener,NoteBroadcastReceiver.OnChangeUi{

    private val noteBroadcastReceiver = NoteBroadcastReceiver(this)
    private var isAgain:Boolean? = false
    private lateinit var binding: FragmentNoteListBinding
    private val notesListViewModel:NotesListViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isAgain = arguments?.getBoolean("isAgain")
        binding = FragmentNoteListBinding.inflate(inflater,container,false)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
             activity?.registerReceiver(noteBroadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
            notesListViewModel.notesList.observe(viewLifecycleOwner) { notes ->
//                Log.d(TAG, "$notes")
                val adapter = NoteListAdapter(this)
                adapter.submitList(notes)
                binding.recyclerView.adapter = adapter
                swipeToDelete(notes)
            }

    }
    override fun onStart() {
        super.onStart()
        binding.addButton.setOnClickListener {
            notesListViewModel.addNote(Note())
        }
    }

    private fun swipeToDelete(notes:List<Note>){
        val swipeToDeleteCallBack = object:SwipeToDeleteCallBack(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                Log.d(TAG,"$position")
                notesListViewModel.deleteNote(notes[position])
                binding.recyclerView.adapter?.notifyItemRemoved(position)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

    }

    override fun onItemClickListener(note: Note) {
        val bundle = bundleOf("id" to note.id.toString())
//        activity?.findNavController(R.id.fragmentContainerView)?.navigate(R.id.action_notesListFragment_to_noteFragment,bundle)
        view?.findNavController()?.navigate(R.id.action_notesListFragment_to_noteFragment,bundle)
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.unregisterReceiver(noteBroadcastReceiver)
    }

    override fun onChange() {
        binding.NoInternetTextView.visibility = View.VISIBLE
    }

}