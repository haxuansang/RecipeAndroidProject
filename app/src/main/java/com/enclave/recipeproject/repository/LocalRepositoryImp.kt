package com.enclave.recipeproject.repository

import com.enclave.recipeproject.util.RecipeMapper
import com.enclave.recipeproject.local.room.AppDatabase
import com.enclave.recipeproject.model.Recipe
import io.reactivex.Completable
import io.reactivex.Maybe
import javax.inject.Inject


class LocalRepositoryImp @Inject constructor(
    private val appDatabase: AppDatabase
) : LocalRepository {
    override fun getAllRecipes(): Maybe<List<Recipe>> = appDatabase.recipeDao.getRecipes().map {
        RecipeMapper.mapRecipe(it)
    }

    override fun getRecipesWithTypeId(typeId: Int): Maybe<List<Recipe>> =
        appDatabase.recipeDao.getRecipesWithFilter(typeId).map {
            RecipeMapper.mapRecipe(it)
        }

    override fun updateRecipe(recipe: Recipe): Completable =
        appDatabase.recipeDao.update(RecipeMapper.mapRecipe(recipe))

    override fun deleteRecipe(recipeId: Int): Completable = appDatabase.recipeDao.delete(recipeId)

    override fun addRecipe(recipe: Recipe): Completable =
        appDatabase.recipeDao.insert(RecipeMapper.mapRecipe(recipe))
}