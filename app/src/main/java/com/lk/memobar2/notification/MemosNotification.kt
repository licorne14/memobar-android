package com.lk.memobar2.notification

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import com.lk.memobar2.R
import com.lk.memobar2.database.MemoEntity
import com.lk.memobar2.main.MainActivity
import com.lk.memobar2.main.Utils

/**
 * Erstellt von Lena am 28/04/2019.
 */
object MemosNotification {

    private const val TAG = "MemosNotification"
    private const val CHANNEL_ID: String = "com.lk.memobar2.memonotification"

    const val NOTIFICATION_ID: Int = 1001

    private lateinit var builder: Notification.Builder

    fun buildNotification(context: Context, memos: List<MemoEntity>): Notification {
        val notificationText = Utils.getNotificationStringFromList(memos)
        return buildNotification(context, notificationText)
    }

    fun buildNotification(context: Context, text: String): Notification {
        createNotChannelIfNecessary(context)
        initialiseBuilder(context)
        setContent(text, context)
        return builder.build()
    }

    private fun createNotChannelIfNecessary(context: Context){
        if(Utils.isBuildVersionGreaterThan(Build.VERSION_CODES.O)) { //API 26
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if(manager.getNotificationChannel(CHANNEL_ID) == null) {
                createNotificationChannel(manager, context)
            }
        }
    }

    private fun createNotificationChannel(manager: NotificationManager, context: Context){
        val resources = context.resources
        val channel = NotificationChannel(
            CHANNEL_ID,
            resources.getString(R.string.channel_title),
            NotificationManager.IMPORTANCE_LOW
        )
        channel.description = resources.getString(R.string.channel_info)
        channel.setShowBadge(false)
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        manager.createNotificationChannel(channel)
    }

    private fun initialiseBuilder(context: Context){
        builder = Notification.Builder(context, CHANNEL_ID)
    }

    private fun setContent(notificationText: String, context: Context){
        builder.setContentText(notificationText)
        builder.setShowWhen(false)
        builder.setSmallIcon(R.drawable.ic_notification)
        builder.style = Notification.BigTextStyle()
        setContentIntent(context)
    }

    private fun setContentIntent(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        val stack = TaskStackBuilder.create(context)
        stack.addParentStack(MainActivity::class.java)
        stack.addNextIntent(intent)
        val pi = stack.getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        builder.setContentIntent(pi)
    }
}