package com.hefesto.mdsp.pokedex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_pokemon_detail.*

class PokemonDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_detail)

        bindViewToPokemonData()
    }

    private fun bindViewToPokemonData() {
        intent.getParcelableExtra<Pokemon>(POKEMON_EXTRA)?.let { pokemon ->
            tv_pokemonName.text = pokemon.name

            tv_pokemonNumber.text = "#%03d".format(pokemon.number)

            bindTextViewsToPokemonTypes(pokemon)

            tv_weight.text = "%.1f Kg".format(pokemon.weight)

            tv_height.text = "%.2f m".format(pokemon.height)

            Picasso.get().load(pokemon.imageUrl).into(iv_pokemonPic)

            bindMapFragmentToPokemonLocation(pokemon)
        }
    }

    private fun bindMapFragmentToPokemonLocation(pokemon: Pokemon) {
        (supportFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment).apply {
            getMapAsync { googleMap ->
                googleMap.uiSettings.isZoomControlsEnabled = true

                val latLng = LatLng(pokemon.latitude, pokemon.longitude)

                MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location))
                    .also { googleMap.addMarker(it) }

                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
            }
        }
    }

    private fun bindTextViewsToPokemonTypes(pokemon: Pokemon) {

        tv_firstType.text = pokemon.types.getOrNull(0)

        val secondType = pokemon.types.getOrNull(1)

        if (secondType == null) {
            tv_secondType.visibility = View.GONE
        } else {
            tv_secondType.visibility = View.VISIBLE
            tv_secondType.text = secondType
        }
    }

    companion object {
        const val POKEMON_EXTRA = "Pokemon"
    }
}