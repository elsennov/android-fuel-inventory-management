package com.mariniana.fuelinventorymanagement.main.presenter

import com.mariniana.fuelinventorymanagement.BasePresenter
import com.mariniana.fuelinventorymanagement.api.ApiManager
import com.mariniana.fuelinventorymanagement.firebase.FirebaseManager
import com.mariniana.fuelinventorymanagement.main.model.Refill
import io.reactivex.Observable

/**
 * Created by elsennovraditya on 11/30/16.
 */

open class MainPresenter(private val apiManager: ApiManager,
                         private val firebaseManager: FirebaseManager) : BasePresenter() {

    fun logoutObservable(): Observable<Boolean> {
        return firebaseManager.logoutObservable()
    }

    fun getCurrentUserRoleObservable(): Observable<String> {
        return firebaseManager.getCurrentUserRoleObservable()
    }

    fun getRefillObservable(refillId: String): Observable<Refill> {
        if (refillId.isNullOrEmpty()) {
            return firebaseManager.getLatestRefillObservable()
        } else {
            return firebaseManager.getRefillObservable(refillId)
        }
    }

    fun sendFuelObservable(refillId: String): Observable<Boolean> {
        return firebaseManager.sendFuelObservable(refillId, System.currentTimeMillis())
    }

    fun requestRefillObservable(refillId: String): Observable<Boolean> {
        return firebaseManager.requestRefillObservable(refillId, System.currentTimeMillis())
    }

    fun listenToRefillRequest() {
        firebaseManager.subscribeToTopic("refill_request")
    }

    fun getCurrentVolumeObservable(): Observable<Double> {
        return firebaseManager.getCurrentVolumeObservable()
    }

}