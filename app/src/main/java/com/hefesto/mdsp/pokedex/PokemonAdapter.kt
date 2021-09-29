package com.hefesto.mdsp.pokedex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_pokemon.view.*

class PokemonAdapter(
    private val pokemons: List<Pokemon>,
    private val onItemClick: (Pokemon) -> Unit
): RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {
    class PokemonViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_pokemon, parent, false)
            .let {
                PokemonViewHolder(it)
            }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        with(holder.itemView) {
            tv_pokemonName.text = pokemons[position].name
            tv_pokemonNumber.text = "#%03d".format(pokemons[position].number)
            Picasso.get().load(pokemons[position].imageUrl).into(iv_pokemonPic)

            holder.itemView.setOnClickListener {
                onItemClick(pokemons[position])
            }
        }
    }

    override fun getItemCount() = pokemons.size
}