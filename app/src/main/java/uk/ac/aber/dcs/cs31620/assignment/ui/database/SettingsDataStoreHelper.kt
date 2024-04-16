package uk.ac.aber.dcs.cs31620.assignment.ui.database

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

const val DATASTORE_KEY = "UserSettings"


class SettingsDataStoreHelper {
    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATASTORE_KEY)
    }
    fun getDataBoolean(stringKey: String, context: Context): Flow<Boolean> {
        val key = booleanPreferencesKey(stringKey)

        val dataFlow: Flow<Boolean> = context.dataStore.data
            .map { preferences ->
                preferences[key] ?: false
            }
        return dataFlow
    }

    fun getDataInt(stringKey: String, context: Context): Flow<Int> {

        val key = intPreferencesKey(stringKey)

        val dataFlow: Flow<Int> = context.dataStore.data
            .map { preferences ->
                preferences[key] ?: 0
            }
        return dataFlow
    }

    fun getDataString(stringKey: String, context: Context): Flow<String> {

        val key = stringPreferencesKey(stringKey)

        val dataFlow: Flow<String> = context.dataStore.data
            .map { preferences ->
                preferences[key] ?: ""
            }
        return dataFlow
    }

    suspend fun saveDataString(stringKey: String, context: Context, newValue: String) {

        val key = stringPreferencesKey(stringKey)

        context.dataStore.edit { preferences ->
            preferences[key] = newValue
        }

    }

    suspend fun saveDataBoolean(stringKey: String, context: Context, newValue: Boolean) {

        val key = booleanPreferencesKey(stringKey)

        context.dataStore.edit { preferences ->
            preferences[key] = newValue
        }

    }

    suspend fun saveDataInt(stringKey: String, context: Context, newValue: Int) {

        val key = intPreferencesKey(stringKey)

        context.dataStore.edit { preferences ->
            preferences[key] = newValue
        }

    }
}