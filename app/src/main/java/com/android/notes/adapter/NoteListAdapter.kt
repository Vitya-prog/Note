package com.android.notes.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.notes.R
import com.android.notes.data.Note
import java.text.SimpleDateFormat
import java.util.*
private const val TAG="NoteListAdapter"
class NoteListAdapter(private val listener:OnItemClickListener)
    :ListAdapter<Note,NoteListAdapter.NoteViewHolder>(DiffCallBack()) {
interface OnItemClickListener{
 fun onItemClickListener(note: Note)
}
    class NoteViewHolder(view: View):ViewHolder(view) {
        private val name: TextView = itemView.findViewById(R.id.title)
        private val description: TextView = itemView.findViewById(R.id.description)
        private val date: TextView = itemView.findViewById(R.id.date)
        fun bind(note: Note) {
            name.text = note.name
            description.text = note.description
            val dateFormat = (SimpleDateFormat("dd.MM.yyyy",Locale.ENGLISH))
            val pattern = if (dateFormat.format(note.date) == dateFormat.format(Date())) "hh:mm" else "dd.MM.yyyy"
            date.text = SimpleDateFormat(pattern, Locale.ENGLISH).format(note.date)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_note,parent,false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
        listener.onItemClickListener(getItem(position))
        }
        holder.bind(getItem(position))
    }
}

class DiffCallBack:DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
       return oldItem == newItem
    }

}
