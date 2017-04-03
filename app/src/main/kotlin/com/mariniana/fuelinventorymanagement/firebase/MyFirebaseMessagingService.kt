package com.mariniana.fuelinventorymanagement.firebase

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mariniana.fuelinventorymanagement.R
import com.mariniana.fuelinventorymanagement.utils.LogUtils

/**
 * Created by elsennovraditya on 4/2/17.
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val tag = MyFirebaseMessagingService::class.java.simpleName

    companion object {
        private const val MESSAGE_NOTIFICATION_REQUEST_CODE = 10
        const private val MESSAGE_NOTIFICATION_ID = 1
        private const val ACTION = "com.mariniana.fuelinventorymanagemenet.NOTIFICATION_TAPPED"
        private const val VIBRATION_DELAY: Long = 250
        private const val VIBRATION_DURATION: Long = 250
        private const val LIGHT_ON_MS = 1000
        private const val LIGHT_OFF_MS = 3000
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        LogUtils.debug(tag, "Message received from FCM")
        LogUtils.debug(tag, "From: " + remoteMessage?.from)
        LogUtils.debug(tag, "To: " + remoteMessage?.to)
        LogUtils.debug(tag, "Message id: " + remoteMessage?.messageId)
        LogUtils.debug(tag, "Message Data: " + remoteMessage?.data)

        val contentIntent = buildMessageContentIntent(this)
        val notification = buildMessageNotification(this, contentIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(MESSAGE_NOTIFICATION_ID, notification)
    }

    private fun buildMessageContentIntent(context: Context): PendingIntent {
        val intent = Intent(context, NotificationReceiver::class.java)
        intent.action = ACTION

        return PendingIntent.getBroadcast(
            context, MESSAGE_NOTIFICATION_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun buildMessageNotification(context: Context, contentIntent: PendingIntent): Notification {
        return NotificationCompat.Builder(context)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_format_color_fill_black_24dp))
            .setSmallIcon(R.drawable.ic_format_color_fill_white_24dp)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setVibrate(longArrayOf(VIBRATION_DELAY, VIBRATION_DURATION))
            .setLights(ContextCompat.getColor(context, android.R.color.holo_blue_light), LIGHT_ON_MS, LIGHT_OFF_MS)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle("Fuel Inventory Management")
            .setContentText("Low Fuel! Please refill your tank!")
            .setContentIntent(contentIntent)
            .build()
    }

}