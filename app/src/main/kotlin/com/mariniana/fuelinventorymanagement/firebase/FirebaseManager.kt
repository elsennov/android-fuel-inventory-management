package com.mariniana.fuelinventorymanagement.firebase

import android.os.AsyncTask
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.mariniana.fuelinventorymanagement.login.model.User
import com.mariniana.fuelinventorymanagement.main.model.Refill
import com.mariniana.fuelinventorymanagement.main.model.Tank
import com.mariniana.fuelinventorymanagement.utils.LogUtils
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject


/**
 * Created by elsennovraditya on 4/2/17.
 */
class FirebaseManager(private val gson: Gson) {

    private val tag = FirebaseManager::class.java.simpleName
    private val subscribedTopics = mutableListOf<String>()

    companion object {
        const val TOPIC_REFILL_REQUEST = "/topics/refill_request"

        private const val REF_REGISTRATION_IDS = "registration_ids"
        private const val REF_REGISTRATION_ID = "registration_id"
        private const val REF_REFILLS = "refills"
        private const val REF_TANKS = "tanks"
        private const val REF_USERS = "users"
        private const val REF_ROLE = "role"
        private const val REF_REFILL_REQUESTS = "refill_requests"
        private const val LEGACY_SERVER_KEY = "AIzaSyBW0MbOxtrTiskzBwAo6esEOhNpr7SNfEM"
        private val JSON = MediaType.parse("application/json; charset=utf-8")
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

    fun logoutObservable(): Observable<Boolean> {
        return Observable.create { subscriber ->
            subscribedTopics.forEach { FirebaseMessaging.getInstance().unsubscribeFromTopic(it) }

            FirebaseAuth.getInstance().signOut()
            subscriber.onNext(true)
            subscriber.onComplete()
        }
    }

    fun getCurrentHeightObservable(): Observable<Double> {
        return keepTokenFreshObservable()
            .flatMap {
                Observable.create<Double> { subscriber ->
                    val firebaseDatabase = FirebaseDatabase.getInstance()
                    firebaseDatabase
                        .getReference(REF_TANKS)
                        .child(Tank.ID)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(onCancelled: DatabaseError?) {
                                LogUtils.debug(tag, "Failure in getCurrentHeightObservable")
                                subscriber.onError(onCancelled?.toException() ?: Throwable())
                            }

                            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                                LogUtils.debug(tag, "onDataChange in getCurrentHeightObservable")
                                val tank = (dataSnapshot?.value as HashMap<String, Any>?)
                                subscriber.onNext(tank?.get(Tank.CURRENT_HEIGHT) as Double? ?: 0.0)
                                subscriber.onComplete()
                            }
                        })
                }
            }
    }

    fun getCurrentVolumeObservable(): Observable<Double> {
        return keepTokenFreshObservable()
            .flatMap {
                Observable.create<Double> { subscriber ->
                    val firebaseDatabase = FirebaseDatabase.getInstance()
                    firebaseDatabase
                        .getReference(REF_TANKS)
                        .child(Tank.ID)
                        .addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(onCancelled: DatabaseError?) {
                                LogUtils.debug(tag, "Failure in getCurrentVolumeObservable")
                                subscriber.onError(onCancelled?.toException() ?: Throwable())
                            }

                            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                                LogUtils.debug(tag, "onDataChange in getCurrentVolumeObservable")
                                val tank = (dataSnapshot?.value as HashMap<String, Any>?)
                                subscriber.onNext(tank?.get(Tank.CURRENT_VOLUME) as Double? ?: 0.0)
                            }
                        })
                }
            }
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

    fun getCurrentUserRoleObservable(): Observable<String> {
        return keepTokenFreshObservable()
            .flatMap {
                Observable.create<String> { subscriber ->
                    val firebaseDatabase = FirebaseDatabase.getInstance()
                    firebaseDatabase
                        .getReference(REF_USERS)
                        .child(FirebaseAuth.getInstance().currentUser?.uid ?: "")
                        .child(REF_ROLE)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(onCancelled: DatabaseError?) {
                                LogUtils.debug(tag, "Failure in getCurrentUserRoleObservable")
                                subscriber.onError(onCancelled?.toException() ?: Throwable())
                            }

                            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                                LogUtils.debug(tag, "onDataChange in getCurrentUserRoleObservable")
                                subscriber.onNext(dataSnapshot?.value as String?)
                                subscriber.onComplete()
                            }
                        })
                }
            }
    }

    fun getLatestRefillObservable(): Observable<Refill> {
        return keepTokenFreshObservable()
            .flatMap {
                Observable.create<Refill> { subscriber ->
                    val firebaseDatabase = FirebaseDatabase.getInstance()
                    firebaseDatabase
                        .getReference(REF_REFILLS)
                        .orderByKey()
                        .limitToLast(1)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(onCancelled: DatabaseError?) {
                                LogUtils.debug(tag, "Failure in getLatestRefillObservable")
                                subscriber.onError(onCancelled?.toException() ?: Throwable())
                            }

                            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                                LogUtils.debug(tag, "onDataChange in getLatestRefillObservable: $dataSnapshot")
                                val refill = (dataSnapshot?.value as HashMap<String, HashMap<String, Any>>?)
                                val refillId = refill?.keys?.firstOrNull()
                                subscriber.onNext(Refill(
                                    refillId,
                                    refill?.get(refillId)?.get(Refill.STATUS) as String? ?: "",
                                    refill?.get(refillId)?.get(Refill.UPDATED_AT) as Long? ?: 0
                                ))
                                subscriber.onComplete()
                            }
                        })
                }
            }
    }

    fun getRefillObservable(refillId: String): Observable<Refill> {
        return keepTokenFreshObservable()
            .flatMap {
                Observable.create<Refill> { subscriber ->
                    val firebaseDatabase = FirebaseDatabase.getInstance()
                    firebaseDatabase
                        .getReference(REF_REFILLS)
                        .child(refillId)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(onCancelled: DatabaseError?) {
                                LogUtils.debug(tag, "Failure in getRefillObservable")
                                subscriber.onError(onCancelled?.toException() ?: Throwable())
                            }

                            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                                LogUtils.debug(tag, "onDataChange in getRefillObservable: $dataSnapshot")
                                subscriber.onNext(gson.fromJson(dataSnapshot.toString(), Refill::class.java))
                                subscriber.onComplete()
                            }
                        })
                }
            }
    }

    fun sendFuelObservable(refillId: String,
                           currentTimeMillis: Long): Observable<Boolean> {
        return keepTokenFreshObservable()
            .flatMap {
                Observable.create<Boolean> { subscriber ->
                    val firebaseDatabase = FirebaseDatabase.getInstance()
                    val refill = HashMap<String, Any>()
                    refill.put(Refill.STATUS, Refill.FILLED)
                    refill.put(Refill.UPDATED_AT, currentTimeMillis)

                    firebaseDatabase
                        .reference
                        .child(REF_REFILLS)
                        .child(refillId)
                        .setValue(
                            refill,
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

    fun requestRefillObservable(refillId: String, currentTimeMillis: Long): Observable<Boolean> {
        return keepTokenFreshObservable()
            .flatMap {
                Observable.create<Boolean> { subscriber ->
                    val firebaseDatabase = FirebaseDatabase.getInstance()
                    val refillRequest = HashMap<String, Any>()
                    refillRequest.put("message", "Please refill")

                    firebaseDatabase
                        .getReference(REF_REFILL_REQUESTS)
                        .child(currentTimeMillis.toString())
                        .setValue(
                            refillRequest,
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
            .flatMap {
                Observable.create<Boolean> { subscriber ->
                    val firebaseDatabase = FirebaseDatabase.getInstance()
                    val refill = HashMap<String, Any>()
                    refill.put(Refill.STATUS, Refill.REQUESTED)
                    refill.put(Refill.UPDATED_AT, currentTimeMillis)

                    firebaseDatabase
                        .reference
                        .child(REF_REFILLS)
                        .child(refillId)
                        .setValue(
                            refill,
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
            .flatMap {
                Observable.create<Boolean> { subscriber ->
                    object : AsyncTask<Unit, Unit, Unit>() {
                        override fun doInBackground(vararg params: Unit?) {
                            try {
                                val client = OkHttpClient()
                                val json = JSONObject()
                                val dataJson = JSONObject()
                                dataJson.put("body", "I need a refill now!")
                                dataJson.put("title", "Refill Request")
                                json.put("data", dataJson)
                                json.put("to", TOPIC_REFILL_REQUEST)
                                val body = RequestBody.create(JSON, json.toString())
                                val request = Request.Builder()
                                    .header("Authorization", "key=" + LEGACY_SERVER_KEY)
                                    .url("https://fcm.googleapis.com/fcm/send")
                                    .post(body)
                                    .build()
                                val response = client.newCall(request).execute()
                                val finalResponse = response.body().string()
                                subscriber.onNext(true)
                                subscriber.onComplete()
                            } catch (exception: Exception) {
                                subscriber.onError(exception)
                            }
                        }
                    }.execute()
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

    fun getCurrentUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }

    fun subscribeToTopic(topic: String) {
        if (!subscribedTopics.contains(topic)) {
            subscribedTopics.add(topic)
        }
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
    }

}