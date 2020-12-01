package com.enclave.recipeproject.model

sealed class RecipeEvent {
    object RecipeAdded : RecipeEvent()
    object RecipeUpdated : RecipeEvent()
    object RecipeDeleted: RecipeEvent()
}