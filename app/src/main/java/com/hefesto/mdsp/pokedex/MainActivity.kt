package com.hefesto.mdsp.pokedex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.libraries.places.api.Places
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var pokemons: MutableList<Pokemon> = mutableListOf()

    private var adapter: PokemonAdapter = PokemonAdapter(pokemons) {
        Intent(this, PokemonDetailActivity::class.java).apply {
            putExtra(PokemonDetailActivity.POKEMON_EXTRA, it)
        }.also { startActivity(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Places.initialize(applicationContext, BuildConfig.GOOGLE_API_KEY)

        setupPokemonListWithRecyclerView()
        setupAddPokemonButtonClick()

        shouldDisplayEmptyView()
    }

    private fun setupAddPokemonButtonClick() {
        floatingActionButton.setOnClickListener { startPokemonAddActivityForNewPokemon() }
    }

    private fun setupPokemonListWithRecyclerView() {
        rv_pokemons.adapter = adapter
    }

    private fun startPokemonAddActivityForNewPokemon () {
        Intent(this,PokemonAddActivity::class.java).also {
            startActivityForResult(it, ADD_POKEMON_REQUEST_CODE)
        }
    }

    fun shouldDisplayEmptyView() {
        emptyView.visibility = if (pokemons.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        val updatedPokemonList = AppDatabase.getInstance(this).pokemonDao.selectAll()
        updateRecyclerView(updatedPokemonList)
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_POKEMON_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.getParcelableExtra<Pokemon>(PokemonAddActivity.ADD_POKEMON_EXTRA)?.let {
                appendPokemonToRecyclerView(it)
            }
        }
    }*/

    private fun updateRecyclerView(updatedPokemonList: List<Pokemon>) {
        pokemons.clear()
        pokemons.addAll(updatedPokemonList)
        adapter.notifyDataSetChanged()
        shouldDisplayEmptyView()
    }

    companion object {
        const val ADD_POKEMON_REQUEST_CODE = 1
    }
}