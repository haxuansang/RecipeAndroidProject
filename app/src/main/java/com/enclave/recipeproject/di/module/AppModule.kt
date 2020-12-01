package com.enclave.recipeproject.di.module

import android.content.Context
import com.enclave.recipeproject.RecipeApplication
import dagger.Binds
import dagger.Module
import dagger.android.support.AndroidSupportInjectionModule


@Module(includes = [AndroidSupportInjectionModule::class])
abstract class AppModule {
    @Binds
    abstract fun context(app: RecipeApplication): Context
}