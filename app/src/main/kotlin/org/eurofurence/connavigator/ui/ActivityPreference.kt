package org.eurofurence.connavigator.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceActivity
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.util.extensions.logv

class ActivitySettings : PreferenceActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        logv { "Preference %s changed".format(key!!) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }
}