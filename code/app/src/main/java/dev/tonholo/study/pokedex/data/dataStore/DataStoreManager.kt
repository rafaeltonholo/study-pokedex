package dev.tonholo.study.pokedex.data.dataStore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val STORE_NAME = "pokedex_app_preferences_datastore"
private const val TAG = "DataStoreManager"

interface DataStoreManager {
    suspend fun <T> store(key: Preferences.Key<T>, value: T)
    suspend fun <T> read(key: Preferences.Key<T>, onReadFinished: T.() -> Unit)
}

class DataStoreManagerImpl(
    @ApplicationContext private val context: Context,
): DataStoreManager {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(STORE_NAME)

    override suspend fun <T> store(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit {
            it[key] = value
        }
    }

    override suspend fun <T> read(key: Preferences.Key<T>, onReadFinished: T.() -> Unit) {
        context.dataStore.data
            .catch {
                if (it is IOException) {
                    Log.e(TAG, "read: IOException thrown", it)
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }
            .map {
                it[key]
            }
            .collect {
                it?.let(onReadFinished)
            }
    }
}
