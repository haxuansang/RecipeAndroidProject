package com.enclave.recipeproject.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

//@Singleton
class ViewModelsFactory @Inject constructor(
    private val viewModels: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {
    init {

    }
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>) = viewModels[modelClass]?.get() as T
}