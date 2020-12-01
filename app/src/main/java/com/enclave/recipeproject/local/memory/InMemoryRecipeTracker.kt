package com.enclave.recipeproject.local.memory

import com.enclave.recipeproject.model.RecipeEvent
import io.reactivex.Completable
import io.reactivex.Observable

interface InMemoryRecipeTracker {
    fun update(updateType: RecipeEvent): Completable
    fun get(): Observable<RecipeEvent>
}