package com.android.notes.ui





import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.android.notes.NetworkConnectionLiveData
import com.android.notes.R
import com.android.notes.adapter.NoteListAdapter
import com.android.notes.data.Note
import com.android.notes.databinding.FragmentNoteListBinding
import com.android.notes.worker.NoteWorkManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch



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
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val workRequest = OneTimeWorkRequestBuilder<NoteWorkManager>().addTag("request").build()
        val workManager = WorkManager.getInstance(requireContext())
        NetworkConnectionLiveData(requireContext()).observe(viewLifecycleOwner){ isConnected ->
            if (MainActivity.isAgain){
                binding.progressBarHorizontal.visibility = View.VISIBLE
                if(isConnected){
                workManager.beginUniqueWork("request",ExistingWorkPolicy.KEEP,workRequest).enqueue()
                workManager.getWorkInfoByIdLiveData(workRequest.id).observe(viewLifecycleOwner) { workInfo ->
                    workInfo.let {
                        val progress = workInfo.progress
                        val value = progress.getInt("Progress",0)
                        binding.progressBarHorizontal.progress = value
                        if (it.state == WorkInfo.State.SUCCEEDED){
                            binding.layout.removeView(binding.progressBarHorizontal)
                        }
                    }
                }
                } else {
                    workManager.cancelAllWorkByTag("request")
                    val snackBar = Snackbar.make(view,"Проверьте подключение",Snackbar.LENGTH_SHORT)
                    snackBar.show()
                }
            } else {
                binding.progressBar.visibility = View.VISIBLE
                binding.NoInternetTextView.visibility = View.GONE
                if (isConnected){
                    workManager.beginUniqueWork("request",ExistingWorkPolicy.KEEP,workRequest).enqueue()
                    workManager.getWorkInfoByIdLiveData(workRequest.id).observe(viewLifecycleOwner) { workInfo ->
                        workInfo.let {
                    if(it.state == WorkInfo.State.SUCCEEDED){
                        binding.layout.removeView(binding.progressBar)
                        binding.layout.removeView(binding.NoInternetTextView)
                    }
                        }
                    }
                } else {
                    binding.progressBar.visibility = View.GONE
                    workManager.cancelAllWorkByTag("request")
                    binding.NoInternetTextView.visibility = View.VISIBLE
                }
            }
        }
            notesListViewModel.notesList.observe(viewLifecycleOwner) { notes ->
                if (notes.isEmpty()) binding.noDataTextView.visibility = View.VISIBLE
                else binding.noDataTextView.visibility = View.GONE
                val adapter = NoteListAdapter(this)
                adapter.submitList(notes)
                binding.recyclerView.adapter = adapter
            }
        swipeToDelete()

    }
    override fun onStart() {
        super.onStart()
        binding.addButton.setOnClickListener {
            notesListViewModel.addNote(Note())
        }
    }

    private fun swipeToDelete(){
        val swipeToDeleteCallBack = object:SwipeToDeleteCallBack(){
            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                viewLifecycleOwner.lifecycleScope.launch {
                    notesListViewModel.swipeToDeleteNote(position)
                }

                binding.recyclerView.adapter?.notifyItemRemoved(position)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

    }

    override fun onItemClickListener(note: Note) {
        val bundle = bundleOf("id" to note.id.toString())
        view?.findNavController()?.navigate(R.id.action_notesListFragment_to_noteFragment,bundle)
    }
}