package com.hefesto.mdsp.pokedex

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.android.synthetic.main.activity_pokemon_add.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class PokemonAddActivity : AppCompatActivity() {

    private lateinit var selectedPlace: Place

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_add)
        setupLocationInputClick()
        setupDoneButtonClick()
    }

    private fun setupDoneButtonClick() {
        btn_pronto.setOnClickListener {
            if ((!edt_nameInput.text.isNullOrBlank() && !edt_nameInput.text.isNullOrEmpty()) &&
                (!edt_locationInput.text.isNullOrBlank() && !edt_locationInput.text.isNullOrEmpty())
            ) {
                val name = edt_nameInput.text.toString().toLowerCase(Locale.getDefault())
                fetchPokemonByName(
                    name,
                    onSuccess = {
                        val pokemonWithLocation = it.apply {
                            latitude = selectedPlace.latLng?.latitude ?: 0.0
                            longitude = selectedPlace.latLng?.longitude ?: 0.0
                        }
                        /*returnPokemonAsActivityResult(it)*/
                        AppDatabase.getInstance(this).pokemonDao.insert(pokemonWithLocation)
                        finish()
                    },
                    onError = { shortToast("Algo de errado nao esta certo") }
                )
            } else {
                shortToast("Campos invalidos")
            }
        }
    }

    private fun shortToast(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    private fun setPokemonLocation(pokemon: Pokemon): Pokemon {
        val pokemonWithLocation = pokemon.apply {
            latitude = selectedPlace.latLng?.latitude ?: 0.0
            longitude = selectedPlace.latLng?.longitude ?: 0.0
        }
        return pokemonWithLocation
    }

    private fun fetchPokemonByName(
        name: String,
        onSuccess: (Pokemon) -> Unit,
        onError: () -> Unit
    ) {
        PokeApi.INSTANCE.getPokemonByName(name).enqueue(object : Callback<Pokemon> {
            override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {

                val pokemon = response.body()

                if (response.isSuccessful && pokemon != null) {
                    onSuccess(pokemon)
                    //                                    Toast.makeText(this@PokemonAddActivity,"Sucesso", Toast.LENGTH_SHORT).show()
                } else {
                    shortToast("Error")
                }
            }

            override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                onError()
            }

        })
    }

    private fun setupLocationInputClick() {
        edt_locationInput.setOnClickListener { startAutocompleteActivityForPlace() }
    }

    private fun startAutocompleteActivityForPlace() {
        val fields = listOf(Place.Field.ADDRESS, Place.Field.LAT_LNG)
        Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this).also {
            startActivityForResult(it, AUTOCOMPLETE_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val status = Autocomplete.getStatusFromIntent(data!!)
        Log.e("INFO", "MICAEL " + status.toString())
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE && data != null) {
            if (resultCode == Activity.RESULT_OK) {
                selectedPlace = Autocomplete.getPlaceFromIntent(data)
                edt_locationInput.setText(selectedPlace.address)
            }
        }
    }

    companion object {
        const val AUTOCOMPLETE_REQUEST_CODE = 1
        const val ADD_POKEMON_EXTRA = "AddedPokemon"
    }
}