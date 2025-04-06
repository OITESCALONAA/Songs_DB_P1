package com.aep.songsdbp1.ui

import androidx.recyclerview.widget.RecyclerView
import com.aep.songsdbp1.R
import com.aep.songsdbp1.data.db.model.SongEntity
import com.aep.songsdbp1.databinding.SongElementBinding
import com.aep.songsdbp1.utils.Constants

class SongViewHolder(
    private val binding: SongElementBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(game: SongEntity){
        binding.apply {
            tvTitle.text = game.title
            tvArtist.text = game.artist
            tvGenre.text = game.genre
            when (game.genre){
                Constants.GENRE_ALTERNATIVE -> ivIcon.setImageResource(R.drawable.alternative)
                Constants.GENRE_CLASSIC -> ivIcon.setImageResource(R.drawable.classic)
                Constants.GENRE_COUNTRY -> ivIcon.setImageResource(R.drawable.country)
                Constants.GENRE_ELECTRONIC -> ivIcon.setImageResource(R.drawable.electronic)
                Constants.GENRE_HIP_HOP_RAP -> ivIcon.setImageResource(R.drawable.hip_hop_rap)
                Constants.GENRE_POP -> ivIcon.setImageResource(R.drawable.pop)
                Constants.GENRE_ROCK -> ivIcon.setImageResource(R.drawable.rock)
            }
        }
    }
}