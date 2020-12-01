package com.enclave.recipeproject.di.component

import com.enclave.recipeproject.RecipeApplication
import com.enclave.recipeproject.di.module.ActivityModule
import com.enclave.recipeproject.di.module.AppModule
import com.enclave.recipeproject.local.room.di.DataModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ActivityModule::class,
        DataModule::class,
        AndroidSupportInjectionModule::class
    ]
)
interface AppComponent : AndroidInjector<RecipeApplication> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: RecipeApplication): Builder

        fun build(): AndroidInjector<RecipeApplication>
    }
}