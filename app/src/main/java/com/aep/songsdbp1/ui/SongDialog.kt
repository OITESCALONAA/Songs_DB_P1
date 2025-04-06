package com.aep.songsdbp1.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.aep.songsdbp1.R
import com.aep.songsdbp1.application.SongsDBApp
import com.aep.songsdbp1.data.SongRepository
import com.aep.songsdbp1.data.db.model.SongEntity
import com.aep.songsdbp1.databinding.SongDialogBinding
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.IOException

class SongDialog(
    private val newSong: Boolean = true,
    private var song: SongEntity = SongEntity(
        title = "",
        artist = "",
        genre = ""
    ),
    private val updateUI: () -> Unit,
    private val message: (String) -> Unit
): DialogFragment() {
    //Para agregar viewbinding al fragment
    private var _binding: SongDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var dialog: Dialog

    //Para poder tener acceso al botón de guardar
    private var saveButton: Button? = null

    //Para el repositorio
    private lateinit var repository: SongRepository

    //Aquí se crea y configura el diálogo
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        _binding = SongDialogBinding.inflate(requireActivity().layoutInflater)

        binding.apply {
            tietTitle.setText(song.title)
            tietArtist.setText(song.artist)
            tietGenre.setText(song.genre)
        }

        // Get the string array from resources
        val genreOptions = resources.getStringArray(R.array.genre_options)

        // Create an ArrayAdapter
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, genreOptions)

        // Apply the adapter
        binding.tietGenre.setAdapter(adapter)

        dialog = if (newSong)
            buildDialog(getString(R.string.save), getString(R.string.cancel), {
                //Guardar
                binding.apply {
                    song.title = tietTitle.text.toString()
                    song.artist = tietArtist.text.toString()
                    song.genre = tietGenre.text.toString()
                }

                try {
                    lifecycleScope.launch {
                        val result = async {
                            repository.insertSong(song)
                        }

                        result.await()
                        message(getString(R.string.song_created))
                        updateUI()
                    }
                } catch (e: IOException) {
                    //Manejamos la excepción
                    e.printStackTrace()
                    message(getString(R.string.error_song_created))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, {
                //Cancelar: no hagas nada
            })
        else
            buildDialog(getString(R.string.update), getString(R.string.delete), {
                //Actualizar
                binding.apply {
                    song.title = tietTitle.text.toString()
                    song.artist = tietArtist.text.toString()
                    song.genre = tietGenre.text.toString()
                }

                try {
                    lifecycleScope.launch {
                        val result = async {
                            repository.updateSong(song)
                        }

                        result.await()
                        message(getString(R.string.song_updated))
                        updateUI()
                    }
                } catch (e: IOException) {
                    //Manejamos la excepción
                    e.printStackTrace()
                    message(getString(R.string.error_song_updated))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, {
                //Eliminar

                //Guardamos el contexto antes de la corrutina para que no se pierda
                val context = requireContext()

                //Diálogo para confirmar
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.confirmation))
                    .setMessage(getString(R.string.delete_confirmation, song.title))
                    .setPositiveButton(getString(R.string.ok)) { _, _ ->
                        try {
                            lifecycleScope.launch {
                                val result = async {
                                    repository.deleteSong(song)
                                }

                                result.await()
                                message(context.getString(R.string.song_deleted))
                                updateUI()
                            }
                        } catch (e: IOException) {
                            //Manejamos la excepción
                            e.printStackTrace()
                            message(getString(R.string.error_song_deleted))
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    .setNegativeButton(getString(R.string.cancel)) { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                    .create()
                    .show()
            })
        return dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    //Se llama después de que se muestra el diálogo en pantalla
    override fun onStart() {
        super.onStart()

        //Instanciamos el repositorio
        repository = (requireContext().applicationContext as SongsDBApp).repository

        //Referenciamos el botón "Guardar" del diálogo
        saveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        saveButton?.isEnabled = false
        binding.apply {
            setupTextWatcher(
                tietTitle,
                tietArtist,
                tietGenre
            )
        }
    }

    private fun validateFields(): Boolean =
        binding.tietTitle.text.toString().isNotEmpty()
                && binding.tietArtist.text.toString().isNotEmpty()
                && binding.tietGenre.text.toString().isNotEmpty()

    private fun setupTextWatcher(vararg fields: android.view.View) {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                saveButton?.isEnabled = validateFields()
            }
        }
        //Agregar el watchera los elementos
        fields.forEach { view ->
            when (view) {
                is TextInputEditText -> view.addTextChangedListener(textWatcher)
                is AutoCompleteTextView -> view.addTextChangedListener(textWatcher)
            }
        }
    }

    private fun buildDialog(
        btn1Text: String,
        btn2Text: String,
        positiveButton: () -> Unit,
        negativeButton: () -> Unit,
    ): Dialog =
        AlertDialog.Builder(requireContext()).setView(binding.root)
            .setTitle(getString(R.string.song))
            .setPositiveButton(btn1Text) { _, _ ->
                //Click para el botón positivo
                positiveButton()
            }
            .setNegativeButton(btn2Text) { _, _ ->
                //Click para el botón negativo
                negativeButton()
            }
            .create()
}