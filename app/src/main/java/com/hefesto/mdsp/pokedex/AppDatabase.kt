package com.hefesto.mdsp.pokedex

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Pokemon::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract val pokemonDao: PokemonDao

    companion object {
        fun getInstance(context: Context) =
                Room.databaseBuilder(context,AppDatabase::class.java, "pokedex-db")
                        .allowMainThreadQueries()
                        .build()
    }
}