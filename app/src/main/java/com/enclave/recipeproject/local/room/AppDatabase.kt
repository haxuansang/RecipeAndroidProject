package com.enclave.recipeproject.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.enclave.recipeproject.local.room.model.RecipeEntity

@Database(entities = [RecipeEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val recipeDao: RecipeDao
}