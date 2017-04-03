package com.mariniana.fuelinventorymanagement.main

import com.mariniana.fuelinventorymanagement.api.ApiManager
import com.mariniana.fuelinventorymanagement.firebase.FirebaseManager
import com.mariniana.fuelinventorymanagement.main.presenter.MainPresenter
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
    fun provideMainActivityPresenter(apiManager: ApiManager,
                                     firebaseManager: FirebaseManager): MainPresenter {
        return MainPresenter(apiManager, firebaseManager)
    }

}