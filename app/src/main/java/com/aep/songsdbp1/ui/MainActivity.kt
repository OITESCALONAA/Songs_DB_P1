package com.aep.songsdbp1.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.aep.songsdbp1.R
import com.aep.songsdbp1.application.SongsDBApp
import com.aep.songsdbp1.data.SongRepository
import com.aep.songsdbp1.data.db.model.SongEntity
import com.aep.songsdbp1.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    //Para el listado de canciones a leer en la bd
    private var songs: MutableList<SongEntity> = mutableListOf()

    //Para el repositorio
    private lateinit var repository: SongRepository

    //Para el adapter del recycler view
    private lateinit var songAdapter: SongAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = (application as SongsDBApp).repository

        //Instanciamos el SongAdapter
        songAdapter = SongAdapter { selectedSong ->
            //Click de una canción
            val dialog = SongDialog(
                newSong = false,
                song = selectedSong,
                updateUI = {
                    updateUI()
                },
                message = { text ->
                    message(text)
                }
            )
            dialog.show(supportFragmentManager, "dialog2")
        }

        binding.apply {
            rvGames.layoutManager = LinearLayoutManager(this@MainActivity)
            rvGames.adapter = songAdapter
        }
        updateUI()
    }

    private fun updateUI() {
        lifecycleScope.launch {
            //Obtenemos todos las canciones
            songs = repository.getAllSongs()

            binding.tvSinRegistros.visibility =
                if (songs.isNotEmpty()) View.INVISIBLE else View.VISIBLE

            songAdapter.updateList(songs)
        }
    }

    fun click(view: View) {
        //Mostramos el diálogo
        val dialog = SongDialog(
            updateUI = {
                updateUI()
            },
            message = { text ->
                message(text)
            }
        )
        dialog.show(supportFragmentManager, "dialog1")
    }

    private fun message(text: String) {
        Snackbar.make(binding.main, text, Snackbar.LENGTH_SHORT)
            .setTextColor(getColor(R.color.white))
            .setBackgroundTint(getColor(R.color.main))
            .show()
    }
}