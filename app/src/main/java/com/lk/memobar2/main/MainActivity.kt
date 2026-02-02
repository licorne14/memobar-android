package com.lk.memobar2.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.lifecycle.*
import com.google.android.material.color.DynamicColors
import com.lk.memobar2.R
import com.lk.memobar2.database.MemoEntity
import com.lk.memobar2.fragments.ListFragment
import com.lk.memobar2.notification.MemoNotificationManager
import com.lk.memobar2.notification.MemosNotification

class MainActivity : FragmentActivity(), Observer<List<MemoEntity>> {

    private lateinit var viewModel: MemoViewModel
    private lateinit var notificationManager: MemoNotificationManager

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        DynamicColors.applyToActivityIfAvailable(this)
        setContentView(R.layout.activity_main)

        handleNotificationPermission()
        initialiseNotificationHandling()
        changeToRecyclerList()
    }

    private fun handleNotificationPermission() {
        // check if I have it
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED -> {
                Log.d(TAG, "handleNotificationPermission: I already have permission for notifications :)")
            }
            else -> {
                // I guess this will ask until we get it? -> which wouldn't be so great
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // explicit request for Android 13+
                    val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                        if (isGranted){
                            Log.d(TAG, "handleNotificationPermission: yay it got granted :)")
                            // force update, just to be safe in case it was granted later
                            val memos = viewModel.getMemos()
                            notificationManager.handleMemosUpdate(memos)
                        } else {
                            Log.d(TAG, "handleNotificationPermission: permission deny")
                            // TODO add a toast or dialog to explain what that means
                        }
                    }
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    Log.d(TAG, "handleNotificationPermission: version is below 13 -> automatically granted or denied")
                }
            }
        }
    }

    private fun initialiseNotificationHandling(){
        viewModel = ViewModelProvider(this).get(MemoViewModel::class.java)
        viewModel.observeMemos(this, this)
        notificationManager = MemoNotificationManager(application)
    }

    private fun changeToRecyclerList(){
        supportFragmentManager.commit {
            replace(R.id.fl_main, ListFragment())
        }
    }

    override fun onChanged(value: List<MemoEntity>) {
        notificationManager.handleMemosUpdate(value)
    }

    override fun onStop() {
        super.onStop()
        var data = viewModel.getMemos()
        data = data.filter { memo -> memo.isActive }
        val textData = MemosNotification.getNotificationStringFromList(data)
        SharedPreferenceAccess.putString(Utils.PREF_KEY_NOTIFICATION, textData, applicationContext)
    }
}
