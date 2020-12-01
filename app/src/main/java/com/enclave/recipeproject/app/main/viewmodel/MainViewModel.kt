package com.enclave.recipeproject.app.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.enclave.recipeproject.base.BaseViewModel
import com.enclave.recipeproject.local.memory.InMemoryRecipeTracker
import com.enclave.recipeproject.model.Recipe
import com.enclave.recipeproject.model.RecipeEvent
import com.enclave.recipeproject.model.RecipeType
import com.enclave.recipeproject.usecase.GetRecipeTypes
import com.enclave.recipeproject.repository.LocalRepository
import com.enclave.recipeproject.util.plusAssign
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val recipeLocalRepository: LocalRepository,
    getRecipeTypes: GetRecipeTypes,
    inMemoryRecipeTracker: InMemoryRecipeTracker
) : BaseViewModel() {
    private val listRecipe = MutableLiveData<List<Recipe>>()
    val listRecipeLiveData: LiveData<List<Recipe>> = listRecipe
    private val listRecipeType = MutableLiveData<List<RecipeType>>()
    val listRecipeTypeLiveData: LiveData<List<RecipeType>> = listRecipeType
    private val resetSpinner = MutableLiveData<Boolean>()
    val resetSpinnerLiveData: LiveData<Boolean> = resetSpinner
    private val goToDetailScreen = MutableLiveData<Recipe>()
    val goToDetailScreenLiveData: LiveData<Recipe> = goToDetailScreen
    var currentRecipeTypeId: Int? = null

    init {
        compositeDisposable += getRecipeTypes.execute()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                listRecipeType.value = it
            }
            .subscribe()

        compositeDisposable += inMemoryRecipeTracker.get()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                if (it is RecipeEvent.RecipeAdded) {
                    resetSpinner.value = true
                    getRecipes()
                } else {
                    getRecipes(currentRecipeTypeId)
                }
            }
    }

    fun getRecipes(recipeTypeTitle: Int? = null) {
        if (recipeTypeTitle == null) {
            compositeDisposable += recipeLocalRepository.getAllRecipes()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    if (currentRecipeTypeId == null)
                        listRecipe.value = it
                }
        } else {
            compositeDisposable += recipeLocalRepository.getRecipesWithTypeId(recipeTypeTitle)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { list ->
                    if (currentRecipeTypeId != null)
                        listRecipe.value = list
                }
        }
    }

    fun onItemClicked(recipe: Recipe) {
        goToDetailScreen.value = recipe
    }
}