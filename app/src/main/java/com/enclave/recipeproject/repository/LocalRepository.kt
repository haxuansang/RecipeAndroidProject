package com.enclave.recipeproject.repository

import com.enclave.recipeproject.model.Recipe
import io.reactivex.Completable
import io.reactivex.Maybe

interface LocalRepository {
    fun getAllRecipes(): Maybe<List<Recipe>>
    fun getRecipesWithTypeId(typeId: Int): Maybe<List<Recipe>>
    fun updateRecipe(recipe: Recipe): Completable
    fun deleteRecipe(recipeId: Int): Completable
    fun addRecipe(recipe: Recipe): Completable
}