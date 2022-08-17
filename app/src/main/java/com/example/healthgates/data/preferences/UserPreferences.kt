package com.example.healthgates.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val dataStore: DataStore<Preferences>) {

    val loggedIn: Flow<Boolean?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_LOGGED_IN]
        }

    val locale: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_LOCALE]
        }

    val userId: Flow<Int?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_USER_ID]
        }

    val partnerId: Flow<Int?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_PARTNER_ID]
        }

    val patientId: Flow<Int?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_PATIENT_ID]
        }

    val fileAccess: Flow<Boolean?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_FILE_ACCESS]
        }

    val loginToken: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_LOGIN_TOKEN]
        }

    suspend fun saveLoggedIn(loggedIn: Boolean, userId: Int) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[KEY_LOGGED_IN] = loggedIn
            mutablePreferences[KEY_USER_ID] = userId
        }
    }

    suspend fun saveLocale(locale: String) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[KEY_LOCALE] = locale
        }
    }

    suspend fun savePatientId(patientId: Int) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[KEY_PATIENT_ID] = patientId
        }
    }

    suspend fun saveFileAccess(fileAccess: Boolean) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[KEY_FILE_ACCESS] = fileAccess
        }
    }

    suspend fun saveLoginToken(token: String) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[KEY_LOGIN_TOKEN] = token
        }
    }


    suspend fun clear() {
        dataStore.edit { mutablePreferences ->
            mutablePreferences.clear()
        }
    }

    companion object {
        private val KEY_LOGGED_IN = booleanPreferencesKey("logged_in")
        private val KEY_LOGIN_TOKEN = stringPreferencesKey("login_token")
        private val KEY_LOCALE = stringPreferencesKey("locale")
        private val KEY_USER_ID = intPreferencesKey("user_id")
        private val KEY_PARTNER_ID = intPreferencesKey("partner_id")
        private val KEY_PATIENT_ID = intPreferencesKey("patient_id")
        private val KEY_FILE_ACCESS = booleanPreferencesKey("file_access")
    }

}