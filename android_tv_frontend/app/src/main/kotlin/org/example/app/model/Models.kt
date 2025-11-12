package org.example.app.model

// PUBLIC_INTERFACE
data class Team(
    /** Team display name */
    val name: String = "",
    /** URL to team logo image */
    val logoUrl: String = ""
)

// PUBLIC_INTERFACE
data class Match(
    /** Unique match id used as database key and navigation id */
    val id: String = "",
    /** Home team info */
    val home: Team = Team(),
    /** Away team info */
    val away: Team = Team(),
    /** Score in "home-away" form, e.g., "2-1" */
    val score: String = "0-0",
    /** League, tournament, or category name used as row header */
    val league: String = "General",
    /** ISO8601 or readable time string */
    val startTime: String = "",
    /** Optional highlight video url */
    val highlightUrl: String? = null
)
