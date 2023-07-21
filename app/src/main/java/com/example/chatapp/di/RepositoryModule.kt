package com.example.chatapp.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.chatapp.data.repository.AuthRepository
import com.example.chatapp.data.repository.AuthRepositoryImpl
import com.example.chatapp.data.repository.ChatRepository
import com.example.chatapp.data.repository.ChatRepositoryImpl
import com.example.chatapp.data.repository.MenuRepository
import com.example.chatapp.data.repository.MenuRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        dataStore: DataStore<Preferences>,
        auth: FirebaseAuth,
        database: DatabaseReference,
        storage: StorageReference
    ): AuthRepository {
        return AuthRepositoryImpl(dataStore, auth, database, storage)
    }

    @Provides
    @Singleton
    fun provideChatRepository(
        auth: FirebaseAuth,
        database: DatabaseReference,
        storage: StorageReference
    ): ChatRepository {
        return ChatRepositoryImpl(auth, database, storage)
    }

    @Provides
    @Singleton
    fun provideMenuRepository(
        auth: FirebaseAuth,
        database: DatabaseReference,
        storage: StorageReference
    ): MenuRepository {
        return MenuRepositoryImpl(auth, database, storage)
    }
}