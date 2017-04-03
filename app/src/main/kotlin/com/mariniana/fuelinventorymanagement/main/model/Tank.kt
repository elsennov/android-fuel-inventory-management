package com.mariniana.fuelinventorymanagement.main.model

/**
 * Created by elsennovraditya on 4/2/17.
 */
data class Tank(val currentHeight: Float = 0f,
                val currentVolume: Float = 0f) {

    companion object {
        const val ID = "792bb88d-7e39-47af-86f7-b0f84d132e4e"
        const val CURRENT_HEIGHT = "current_height"
        const val CURRENT_VOLUME = "current_volume"
    }

}