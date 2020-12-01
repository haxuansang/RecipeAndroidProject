package com.enclave.recipeproject

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import com.enclave.recipeproject.di.component.DaggerAppComponent

open class RecipeApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<RecipeApplication> =
        DaggerAppComponent.builder().application(this).build()
}