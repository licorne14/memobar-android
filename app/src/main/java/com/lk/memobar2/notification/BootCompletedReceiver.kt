package com.lk.memobar2.notification

import android.content.*
import android.util.Log
import kotlinx.coroutines.*
import com.lk.memobar2.main.SharedPreferenceAccess
import com.lk.memobar2.main.Utils


/**
 * Erstellt von Lena am 28/04/2019.
 */
class BootCompletedReceiver: BroadcastReceiver() {

    private val TAG = "BootCompletedReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "received boot completed; context is : ${context.packageName}")
        val iAction = intent.action
        if(iAction != null && iAction == Intent.ACTION_BOOT_COMPLETED) {
            runBlocking {
                val memoList = SharedPreferenceAccess.readString(Utils.PREF_KEY_NOTIFICATION, context)
                MemoNotificationManager(context).handleMemosUpdate(memoList)
                Log.d(TAG, "Finished update.")
            }
        }
    }
}