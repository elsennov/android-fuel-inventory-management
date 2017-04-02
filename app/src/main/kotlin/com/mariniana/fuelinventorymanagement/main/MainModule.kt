package com.mariniana.fuelinventorymanagement.main

import com.mariniana.fuelinventorymanagement.api.ApiManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by elsennovraditya on 11/30/16.
 */
@Module
class MainModule {

    @Singleton
    @Provides
    fun provideMainActivityPresenter(apiManager: ApiManager): MainActivityPresenter {
        return MainActivityPresenter(apiManager)
    }

}