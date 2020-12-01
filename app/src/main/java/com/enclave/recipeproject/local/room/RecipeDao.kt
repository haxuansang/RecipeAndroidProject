package com.enclave.recipeproject.local.room

import androidx.room.*
import com.enclave.recipeproject.local.room.model.RecipeEntity
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface RecipeDao {
    @Query("SELECT * from recipe_table ORDER BY id DESC")
    fun getRecipes(): Maybe<List<RecipeEntity>>

    @Query("SELECT * from recipe_table WHERE type = :typeId ORDER BY id DESC")
    fun getRecipesWithFilter(typeId: Int): Maybe<List<RecipeEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(recipe: RecipeEntity): Completable

    @Update
    fun update(recipe: RecipeEntity): Completable

    @Query("DELETE FROM recipe_table WHERE id = :id")
    fun delete(id: Int): Completable
}