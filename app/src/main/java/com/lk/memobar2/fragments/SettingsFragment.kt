package com.lk.memobar2.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.preference.PreferenceFragmentCompat
import com.lk.memobar2.R
import com.lk.memobar2.main.Utils
import com.lk.memobar2.ui.SettingsScreen
import com.lk.memobar2.ui.Themes

/**
 * Erstellt von Lena am 26/04/2019.
 */
class SettingsFragment: PreferenceFragmentCompat() {

    private val TAG = "SettingsFragment"

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // empty for now
        Log.d(TAG, "onCreatePreferences: ")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.fragment_settings_compose, container, false)
        val composeView = rootView.findViewById<ComposeView>(R.id.cv_settings_ui)!!
        setupComposeView(composeView)
        return rootView
    }

    private fun setupComposeView(composeView: ComposeView) {
        composeView.apply {
            // Dispose of the Composition when the view's LifecycleOwner is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                // In Compose world
                Themes.setStatusBarIconColours(this@SettingsFragment.requireActivity().window)
                val colorScheme = if (Utils.isBuildVersionGreaterThan(Build.VERSION_CODES.S)) {
                    Themes.getMaterialColorScheme(this@SettingsFragment.requireContext())
                } else {
                    MaterialTheme.colorScheme
                }
                MaterialTheme(colorScheme = colorScheme) {
                    SettingsScreen()
                }
            }
        }
    }

}