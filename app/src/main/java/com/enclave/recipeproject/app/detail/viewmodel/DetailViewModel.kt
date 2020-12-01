package com.enclave.recipeproject.app.detail.viewmodel

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

class DetailViewModel @Inject constructor(
    private val recipeLocalRepository: LocalRepository,
    private val recipeTracker: InMemoryRecipeTracker,
    getRecipeTypes: GetRecipeTypes
) : BaseViewModel() {

    companion object {
        const val ADD_NEW_RECIPE = 1
        const val UPDATE_RECIPE = 2
        const val DELETE_RECIPE = 3
        private const val EMPTY_STRING = ""
        private const val REPLACE_UNDERLINE_STRING = " "
        private const val UNDERLINE_STRING = "\n"
    }

    val listRecipeType = MutableLiveData<List<RecipeType>>()
    val listRecipeTypeLiveData: LiveData<List<RecipeType>> = listRecipeType
    val isDetailView = MutableLiveData<Boolean>()
    val isUpdating = MutableLiveData<Boolean>()
    val isUpdatingLiveData: LiveData<Boolean> = isUpdating
    private val isEmpty = MutableLiveData<Boolean>()
    val isEmptyLiveData: LiveData<Boolean> = isEmpty
    private val finishActivity = MutableLiveData<Boolean>()
    val finishActivityLiveData: LiveData<Boolean> = finishActivity
    val name = MutableLiveData<String>()
    val img = MutableLiveData<String>()
    val ingredients = MutableLiveData<String>()
    val step = MutableLiveData<String>()
    val typeId = MutableLiveData<Int>()
    val typeIdLiveData: LiveData<Int> = typeId

    //var typeName = MutableLiveData<String>()
    var firstRecipe: Recipe? = null
        set(value) {
            value?.let { updateRecipeData(it) }
            field = value
        }

    private val isSuccessfully = MutableLiveData<Int>()
    val isSuccessfullyLiveData: LiveData<Int> = isSuccessfully

    init {
        compositeDisposable += getRecipeTypes.execute()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                listRecipeType.value = it
            }
            .subscribe()
        isUpdating.value = false
    }

    fun setImg(value: String) {
        img.value = value
    }

    fun setTypeId(recipeTypeId: Int) {
        typeId.value = recipeTypeId
    }

    fun setIsDetailView(value: Boolean) {
        isDetailView.value = value
    }

    fun setIsUpdating(value: Boolean) {
        isUpdating.value = value
    }

    private fun updateRecipeData(value: Recipe) {
        img.value = value.img
        name.value = value.name
        ingredients.value = value.ingredients
        step.value = value.step
        typeId.value = value.typeId
    }

    fun process() {
        if (true == isUpdating.value && firstRecipe != null) {
            if (checkNullOrEmptyFields())
                updateRecipe()
            else
                isEmpty.value = true
        } else {
            if (checkNullOrEmptyFields())
                addRecipe()
            else
                isEmpty.value = true
        }
    }

    private fun checkNullOrEmptyFields(): Boolean {
        return !img.value.isNullOrEmpty()
                && !name.value.isNullOrEmpty()
                && !step.value.isNullOrEmpty()
                && !ingredients.value.isNullOrEmpty()
                && typeId.value != null
    }

    private fun updateRecipe() {
        val recipe = Recipe(
            firstRecipe?.id,
            name.value?.trim()?.replace(UNDERLINE_STRING, REPLACE_UNDERLINE_STRING) ?: EMPTY_STRING,
            step.value?.trim() ?: EMPTY_STRING,
            typeId.value ?: 1,
            ingredients.value?.trim() ?: EMPTY_STRING,
            img.value ?: EMPTY_STRING
        )
        compositeDisposable += recipeLocalRepository.updateRecipe(recipe)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnComplete {
                isUpdating.value = false
                isSuccessfully.value = UPDATE_RECIPE
                updateRecipeTracker(RecipeEvent.RecipeUpdated)
                firstRecipe = recipe
            }
            .subscribe()
    }

    fun deleteRecipe() {
        firstRecipe?.id?.let {
            compositeDisposable += recipeLocalRepository.deleteRecipe(it)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnComplete {
                    finishActivity.value = true
                    isSuccessfully.value = DELETE_RECIPE
                    updateRecipeTracker(RecipeEvent.RecipeDeleted)
                }
                .subscribe()
        }
    }

    private fun addRecipe() {
        val recipe = Recipe(
            null,
            name.value?.trim()?.replace(UNDERLINE_STRING, REPLACE_UNDERLINE_STRING) ?: EMPTY_STRING,
            step.value?.trim() ?: EMPTY_STRING,
            typeId.value ?: 1,
            ingredients.value?.trim() ?: EMPTY_STRING,
            img.value ?: EMPTY_STRING
        )

        compositeDisposable += recipeLocalRepository.addRecipe(recipe)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnComplete {
                updateRecipeTracker(RecipeEvent.RecipeAdded)
                finishActivity.value = true
                isSuccessfully.value = ADD_NEW_RECIPE
            }
            .subscribe()
    }

    fun cancel() {
        if (firstRecipe != null) {
            firstRecipe?.let {
                updateRecipeData(it)
            }
            isUpdating.value = false
        } else {
            finishActivity.value = true
        }
    }

    private fun updateRecipeTracker(event: RecipeEvent) {
        compositeDisposable += recipeTracker.update(event)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}