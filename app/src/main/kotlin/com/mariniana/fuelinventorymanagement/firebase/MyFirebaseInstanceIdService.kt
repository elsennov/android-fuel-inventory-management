package com.mariniana.fuelinventorymanagement.firebase

import com.google.firebase.iid.FirebaseInstanceIdService
import com.mariniana.fuelinventorymanagement.MyApplication

/**
 * Created by elsennovraditya on 4/2/17.
 */
class MyFirebaseInstanceIdService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val firebaseManager = MyApplication.fimComponent.provideFirebaseManager()
        firebaseManager.registerFcmObservable()
    }

}