package com.example.pharmaadminpro.preference

import android.content.Context
import androidx.compose.runtime.rememberCoroutineScope
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore  by preferencesDataStore("Preference DataStore")

class PreferenceDataStore(private val context: Context) {



    companion object{
        private val Email = stringPreferencesKey("Email")
        private val Password = stringPreferencesKey("Password")
        private val User_Id = stringPreferencesKey("User_Id")
        private val UserName = stringPreferencesKey("User_Name")


    }

    suspend fun saveUserCredentialsInPreference( emailValue: String,passwordValue:String){
        context.dataStore.edit { prefs ->
            prefs[Email]= emailValue
            prefs[Password]= passwordValue
        }
    }
    suspend fun saveUserId(id: String){
        context.dataStore.edit {
            it[User_Id] = id
        }
    }

    suspend fun saveUserName(userName: String){
        context.dataStore.edit {
            it[UserName] = userName
        }
    }


    suspend fun clearCredentials() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    val email: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[Email] ?: ""
    }

    val password: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[Password] ?: ""
    }

    val userId: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[User_Id] ?: ""
    }

    val userName: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[UserName] ?: ""
    }





}
