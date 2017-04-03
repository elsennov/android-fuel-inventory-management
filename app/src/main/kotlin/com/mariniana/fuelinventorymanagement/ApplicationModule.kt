package com.mariniana.fuelinventorymanagement

import android.app.Application
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by elsennovraditya on 11/30/16.
 */
@Module
class ApplicationModule(val application: Application) {

    @Singleton
    @Provides
    fun provideApplication(): Application {
        return application
    }

    /**
     * Build gson instance using lower case with underscores policy
     */
    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
    }

}