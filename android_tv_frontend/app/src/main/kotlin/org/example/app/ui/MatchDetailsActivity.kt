package org.example.app.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import org.example.app.R

/**
 * Hosts the MatchDetailsFragment which shows expanded information and link to highlights.
 */
class MatchDetailsActivity : FragmentActivity() {

    companion object {
        const val EXTRA_MATCH_ID = "extra_match_id"
        const val EXTRA_MATCH_LEAGUE = "extra_match_league"
        const val EXTRA_MATCH_HOME_NAME = "extra_home_name"
        const val EXTRA_MATCH_HOME_LOGO = "extra_home_logo"
        const val EXTRA_MATCH_AWAY_NAME = "extra_away_name"
        const val EXTRA_MATCH_AWAY_LOGO = "extra_away_logo"
        const val EXTRA_MATCH_SCORE = "extra_match_score"
        const val EXTRA_MATCH_START_TIME = "extra_match_start"
        const val EXTRA_MATCH_HIGHLIGHT = "extra_match_highlight"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tv_details)

        if (savedInstanceState == null) {
            val fragment = MatchDetailsFragment().apply {
                arguments = intent.extras
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.details_host, fragment)
                .commitNow()
        }
    }
}
