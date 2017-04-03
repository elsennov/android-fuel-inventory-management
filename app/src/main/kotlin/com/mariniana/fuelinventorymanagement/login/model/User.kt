package com.mariniana.fuelinventorymanagement.login.model

import com.mariniana.fuelinventorymanagement.utils.model.Emptiable
import java.io.Serializable

/**
 * Created by elsennovraditya on 4/2/17.
 */
data class User(val userUid: String?,
                val email: String?) : Serializable, Emptiable {

    companion object {
        val empty = User(null, null)
    }

    override fun isEmpty(): Boolean {
        return userUid.isNullOrEmpty()
    }

}