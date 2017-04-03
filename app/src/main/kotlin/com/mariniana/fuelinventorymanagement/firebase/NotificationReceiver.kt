package com.mariniana.fuelinventorymanagement.firebase

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.TaskStackBuilder
import com.mariniana.fuelinventorymanagement.login.composer.LoginActivity

/**
 * Created by elsennovraditya on 4/3/17.
 */
class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val mainActivityIntent = Intent(context, LoginActivity::class.java)
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val taskStackBuilder = TaskStackBuilder
            .create(context)
            .addNextIntent(mainActivityIntent)

        taskStackBuilder.startActivities()
    }

}