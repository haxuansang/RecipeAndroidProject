package com.enclave.recipeproject.local.memory

import com.enclave.recipeproject.model.RecipeEvent
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class InMemoryRecipeTrackerImp @Inject constructor() : InMemoryRecipeTracker {
    private val publishSubject = PublishSubject.create<RecipeEvent>()

    override fun update(event: RecipeEvent): Completable =
        Completable.fromAction {
            publishSubject.onNext(event)
        }

    override fun get(): Observable<RecipeEvent> = publishSubject.hide()
}

