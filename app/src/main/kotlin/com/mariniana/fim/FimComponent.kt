package com.mariniana.fim

import com.mariniana.fim.api.ApiModule
import com.mariniana.fim.api.RetrofitModule
import com.mariniana.fim.main.MainActivityPresenter
import com.mariniana.fim.main.MainModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by elsennovraditya on 11/30/16.
 */
@Singleton
@Component(modules = arrayOf(
    ApplicationModule::class,
    RetrofitModule::class,
    ApiModule::class,
    MainModule::class
))
interface FimComponent {
    fun provideMainActivityPresenter(): MainActivityPresenter
}
