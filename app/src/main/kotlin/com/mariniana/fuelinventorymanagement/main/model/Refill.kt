package com.mariniana.fuelinventorymanagement.main.model

import android.support.annotation.StringDef
import com.mariniana.fuelinventorymanagement.utils.model.Emptiable
import java.io.Serializable

/**
 * Created by elsennovraditya on 4/3/17.
 */
data class Refill(val id: String?,
                  val status: String?,
                  val updatedAt: Long?) : Serializable, Emptiable {

    companion object {
        val empty = Refill(null, null, null)

        const val STATUS = "status"
        const val UPDATED_AT = "updated_at"

        const val NOTIFIED = "notified"
        const val REQUESTED = "requested"
        const val FILLED = "filled"
    }

    override fun isEmpty(): Boolean {
        return id.isNullOrEmpty() && status == null && updatedAt == null
    }

    @StringDef(NOTIFIED, REQUESTED, FILLED)
    @Retention(AnnotationRetention.SOURCE)
    annotation class RoleDef

}