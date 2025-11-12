package org.example.app.ui

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import org.example.app.R
import org.example.app.data.FirebaseConfig

/**
 * TV entry activity hosting the BrowseSupportFragment-based MainTvFragment.
 * Initializes Firebase if possible and passes init state via intent extras.
 */
class MainTvActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tv_main)

        val initialized = FirebaseConfig.initializeIfPossible(this)
        if (savedInstanceState == null) {
            val fragment = MainTvFragment.newInstance(initialized)
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_browse_host, fragment)
                .commitNow()
        }
    }
}
