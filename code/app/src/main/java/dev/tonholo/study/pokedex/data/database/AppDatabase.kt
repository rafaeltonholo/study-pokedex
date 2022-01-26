package dev.tonholo.study.pokedex.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.tonholo.study.pokedex.data.dao.PokemonDao
import dev.tonholo.study.pokedex.data.dao.PokemonTypeDao
import dev.tonholo.study.pokedex.data.dao.PokemonWithTypeDao
import dev.tonholo.study.pokedex.data.entity.Pokemon
import dev.tonholo.study.pokedex.data.entity.PokemonType
import dev.tonholo.study.pokedex.data.entity.PokemonWithType

private const val DATABASE_NAME = "pokedex_db"

@Database(
    entities = [
        Pokemon::class,
        PokemonType::class,
        PokemonWithType::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun pokemonTypeDao(): PokemonTypeDao
    abstract fun pokemonWithTypeDao(): PokemonWithTypeDao

    companion object {
        operator fun invoke(context: Context) =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DATABASE_NAME,
            ).build()
    }
}
