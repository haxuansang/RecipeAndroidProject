package com.enclave.recipeproject.util

import com.enclave.recipeproject.local.room.model.RecipeEntity
import com.enclave.recipeproject.model.Recipe

object RecipeMapper {
    fun mapRecipe(listRecipeEntity: List<RecipeEntity>): List<Recipe> =
        listRecipeEntity.map { recipeEntity ->
            Recipe(
                recipeEntity.id,
                recipeEntity.name,
                recipeEntity.step,
                recipeEntity.typeId,
                recipeEntity.ingredients,
                recipeEntity.img
            )
        }

    fun mapRecipe(recipe: Recipe): RecipeEntity = RecipeEntity(
        recipe.id,
        recipe.name,
        recipe.step,
        recipe.typeId,
        recipe.ingredients,
        recipe.img
    )
}