package dev.tonholo.study.pokedex.data.dao

import androidx.room.*
import dev.tonholo.study.pokedex.data.entity.RemoteKey

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(remoteKeys: RemoteKey)

    @Query("SELECT * FROM RemoteKey WHERE pokemon_number = :pokemonNumber")
    suspend fun get(pokemonNumber: Int): RemoteKey

    @Query("DELETE FROM RemoteKey WHERE pokemon_number = :pokemonNumber")
    suspend fun delete(pokemonNumber: Int)

    @Query("DELETE FROM RemoteKey")
    suspend fun clearAll()
}
