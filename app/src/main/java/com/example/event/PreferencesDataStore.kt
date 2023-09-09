package com.example.event

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class PreferencesRepository private constructor(private val dataStore: DataStore<Preferences>) {

    private val COLLECTION_NAME= stringPreferencesKey("collectionName")


    val collectionName: Flow<String> = this.dataStore.data.map { prefs ->
        prefs[COLLECTION_NAME] ?:  INITIAL_VALUE
    }.distinctUntilChanged() as Flow<String>

    private suspend fun firstSaveIntValue(key: Preferences.Key<String>, value: String) {
        this.dataStore.edit { prefs ->
            prefs[key] = value
        }
    }

    suspend fun saveCollectionName(value: String) {
        firstSaveIntValue(COLLECTION_NAME, value)
    }


    companion object {
        private const val PREFERENCES_DATA_FILE_NAME = "settings"
        private var INSTANCE: PreferencesRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                val dataStore = PreferenceDataStoreFactory.create {
                    context.preferencesDataStoreFile(PREFERENCES_DATA_FILE_NAME)
                }
                INSTANCE = PreferencesRepository(dataStore)
            }
        }
        fun getRepository(): PreferencesRepository {
            return INSTANCE ?: throw IllegalStateException("AppPreferencesRepository not initialized yet")
        }
    }
}