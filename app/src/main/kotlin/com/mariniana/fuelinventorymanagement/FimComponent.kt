package com.mariniana.fuelinventorymanagement

import com.mariniana.fuelinventorymanagement.api.ApiModule
import com.mariniana.fuelinventorymanagement.api.RetrofitModule
import com.mariniana.fuelinventorymanagement.main.MainActivityPresenter
import com.mariniana.fuelinventorymanagement.main.MainModule
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
