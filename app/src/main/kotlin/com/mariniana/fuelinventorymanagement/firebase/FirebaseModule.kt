package com.mariniana.fuelinventorymanagement.firebase

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by elsennovraditya on 4/2/17.
 */
@Module
class FirebaseModule {

    @Singleton
    @Provides
    fun provideFirebaseManager(gson: Gson): FirebaseManager {
        return FirebaseManager(gson)
    }

}