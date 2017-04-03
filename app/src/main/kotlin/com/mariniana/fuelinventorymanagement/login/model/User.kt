package com.mariniana.fuelinventorymanagement.login.model

import android.support.annotation.StringDef
import com.mariniana.fuelinventorymanagement.utils.model.Emptiable
import java.io.Serializable

/**
 * Created by elsennovraditya on 4/2/17.
 */
data class User(val userUid: String?,
                val email: String?) : Serializable, Emptiable {

    companion object {
        val empty = User(null, null)

        const val ROLE_SELLER = "seller"
        const val ROLE_SUPPLIER = "supplier"
    }

    override fun isEmpty(): Boolean {
        return userUid.isNullOrEmpty()
    }

    @StringDef(ROLE_SELLER, ROLE_SUPPLIER)
    @Retention(AnnotationRetention.SOURCE)
    annotation class RoleDef

}