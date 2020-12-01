package com.enclave.recipeproject.local.room.di

import android.content.Context
import androidx.room.Room
import com.enclave.recipeproject.local.memory.InMemoryRecipeTracker
import com.enclave.recipeproject.local.memory.InMemoryRecipeTrackerImp
import com.enclave.recipeproject.local.room.AppDatabase
import com.enclave.recipeproject.repository.LocalRepository
import com.enclave.recipeproject.repository.LocalRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class DataModule {

    @Module
    companion object {
        private const val DB_NAME = "recipe.db"
        private const val DEFAULT_DB_DIRECTORY = "database/default_database.db"

        @Provides
        @Singleton
        @JvmStatic
        fun provideLocalDb(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                .createFromAsset(DEFAULT_DB_DIRECTORY)
                .build()
    }

    @Binds
    abstract fun recipeLocalRepository(repository: LocalRepositoryImp): LocalRepository

    @Binds
    @Singleton
    abstract fun recipeTracker(recipeTrackerImp: InMemoryRecipeTrackerImp): InMemoryRecipeTracker
}