package com.aep.songsdbp1.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aep.songsdbp1.data.db.model.SongEntity
import com.aep.songsdbp1.databinding.SongElementBinding

class SongAdapter(
    private val onSongClick: (SongEntity) -> Unit
): RecyclerView.Adapter<SongViewHolder>() {
    private var songs: List<SongEntity> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = SongElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding)
    }

    override fun getItemCount(): Int = songs.size

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]

        holder.bind(song)
        //Listener del click de cada elemento
        holder.itemView.setOnClickListener {
            onSongClick(song)
        }
    }

    //Actualizamos el adapter para los nuevos elementos actualizados
    fun updateList(list: MutableList<SongEntity>){
        songs = list
        notifyDataSetChanged()
    }
}