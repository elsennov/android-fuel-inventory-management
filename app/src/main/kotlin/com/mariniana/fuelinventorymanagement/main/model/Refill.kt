package com.mariniana.fuelinventorymanagement.main.model

import com.mariniana.fuelinventorymanagement.utils.model.Emptiable
import java.io.Serializable

/**
 * Created by elsennovraditya on 4/3/17.
 */
data class Refill(val status: String?,
                  val updatedAt: Long?) : Serializable, Emptiable {

    companion object {
        val empty = Refill(null, null)
    }

    override fun isEmpty(): Boolean {
        return status == null && updatedAt == null
    }

}