package com.enclave.recipeproject.app.detail.di

import androidx.lifecycle.ViewModel
import com.enclave.recipeproject.app.detail.viewmodel.DetailViewModel
import com.enclave.recipeproject.app.main.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DetailModule {
    @IntoMap
    @Binds
    @ViewModelKey(DetailViewModel::class)
    abstract fun detail(viewModel: DetailViewModel): ViewModel
}