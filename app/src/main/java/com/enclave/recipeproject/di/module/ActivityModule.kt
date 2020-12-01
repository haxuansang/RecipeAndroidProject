package com.enclave.recipeproject.di.module

import com.enclave.recipeproject.app.detail.DetailActivity
import com.enclave.recipeproject.app.detail.di.DetailModule
import com.enclave.recipeproject.app.main.MainActivity
import com.enclave.recipeproject.app.main.di.MainModule
import com.enclave.recipeproject.di.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainModule::class])
    abstract fun main(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [DetailModule::class])
    abstract fun detail(): DetailActivity
}