package com.mariniana.fuelinventorymanagement.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.mariniana.fuelinventorymanagement.login.model.User
import com.mariniana.fuelinventorymanagement.main.model.Tank
import com.mariniana.fuelinventorymanagement.utils.LogUtils
import io.reactivex.Observable

/**
 * Created by elsennovraditya on 4/2/17.
 */
class FirebaseManager(gson: Gson) {

    private val tag = FirebaseManager::class.java.simpleName

    companion object {
        private const val REF_REGISTRATION_IDS = "registration_ids"
        private const val REF_REGISTRATION_ID = "registration_id"
        private const val REF_REFILLS = "refills"
        private const val REF_TANKS = "tanks"
    }

    fun loginObservable(email: String, password: String): Observable<User> {
        return Observable.create { subscriber ->
            val firebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    LogUtils.debug(tag, "Complete in loginObservable")
                }
                .addOnSuccessListener {
                    LogUtils.debug(tag, "Success in loginObservable")
                    subscriber.onNext(User(
                        it.user.uid,
                        it.user.email
                    ))
                    subscriber.onComplete()
                }
                .addOnFailureListener {
                    LogUtils.debug(tag, "Failure in loginObservable")
                    subscriber.onError(it)
                }
        }
    }

    fun getCurrentHeightObservable(): Observable<Float> {
        return keepTokenFreshObservable()
            .flatMap {
                Observable.create<String> { subscriber ->
                    val firebaseDatabase = FirebaseDatabase.getInstance()
                    firebaseDatabase
                        .getReference(REF_TANKS)
                        .child(Tank.ID)
                        .addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(onCancelled: DatabaseError?) {
                                LogUtils.debug(tag, "Failure in getCurrentHeight")
                                subscriber.onError(onCancelled?.toException() ?: Throwable())
                            }

                            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                                LogUtils.debug(tag, "onDataChange in getCurrentHeight")
                                subscriber.onNext(dataSnapshot.toString())
                                subscriber.onComplete()
                            }
                        })
                }
            }
            .map { 0f }
    }

    fun registerFcmObservable(): Observable<Boolean> {
        return keepTokenFreshObservable()
            .flatMap {
                Observable.create<Boolean> { subscriber ->
                    val firebaseDatabase = FirebaseDatabase.getInstance()
                    firebaseDatabase
                        .getReference(REF_REGISTRATION_IDS)
                        .child(FirebaseAuth.getInstance().currentUser?.uid ?: "")
                        .child(REF_REGISTRATION_ID)
                        .setValue(
                            FirebaseInstanceId.getInstance().token,
                            {
                                databaseError, _ ->
                                if (databaseError != null) {
                                    subscriber.onError(databaseError.toException())
                                } else {
                                    subscriber.onNext(true)
                                    subscriber.onComplete()
                                }
                            }
                        )
                }
            }
    }

    private fun keepTokenFreshObservable(): Observable<Boolean> {
        return Observable.create { subscriber ->
            val firebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth
                .currentUser
                ?.getToken(true)
                ?.addOnSuccessListener {
                    subscriber.onNext(true)
                    subscriber.onComplete()
                }
                ?.addOnFailureListener {
                    subscriber.onError(it)
                }
        }
    }

}