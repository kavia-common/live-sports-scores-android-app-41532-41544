package org.example.app.data

import android.util.Log
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.Flow
import org.example.app.model.Match
import org.example.app.model.Team

/**
 * Repository for retrieving live matches from Firebase Realtime Database with auto-updates.
 * Falls back to mock data if Firebase is not initialized.
 */
class MatchRepository(
    private val firebaseInitialized: Boolean
) {

    companion object {
        private const val TAG = "MatchRepository"
        private const val DEFAULT_DB_PATH = "liveMatches"
    }

    // PUBLIC_INTERFACE
    fun watchMatches(dbPath: String = DEFAULT_DB_PATH): Flow<List<Match>> {
        /** Returns a Flow that emits match lists whenever database updates occur. */
        if (!firebaseInitialized) {
            return mockFlow()
        }
        return callbackFlow {
            val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference(dbPath)
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<Match>()
                    snapshot.children.forEach { child ->
                        try {
                            val id = child.key ?: ""
                            val league = child.child("league").getValue(String::class.java) ?: "General"
                            val score = child.child("score").getValue(String::class.java) ?: "0-0"
                            val startTime = child.child("startTime").getValue(String::class.java) ?: ""
                            val homeName = child.child("home/name").getValue(String::class.java) ?: ""
                            val homeLogo = child.child("home/logoUrl").getValue(String::class.java) ?: ""
                            val awayName = child.child("away/name").getValue(String::class.java) ?: ""
                            val awayLogo = child.child("away/logoUrl").getValue(String::class.java) ?: ""
                            val highlightUrl = child.child("highlightUrl").getValue(String::class.java)

                            val match = Match(
                                id = id,
                                league = league,
                                score = score,
                                startTime = startTime,
                                home = Team(homeName, homeLogo),
                                away = Team(awayName, awayLogo),
                                highlightUrl = highlightUrl
                            )
                            list.add(match)
                        } catch (e: Exception) {
                            Log.w(TAG, "Failed to parse match", e)
                        }
                    }
                    trySend(list.sortedBy { it.league + it.startTime })
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Database listener cancelled: ${error.message}")
                }
            }
            dbRef.addValueEventListener(listener)
            awaitClose { dbRef.removeEventListener(listener) }
        }
    }

    private fun mockFlow(): Flow<List<Match>> = callbackFlow {
        // Emit a rotating set of mock matches to simulate updates
        val base = listOf(
            Match(
                id = "1",
                league = "Premier League",
                score = "2-1",
                startTime = "2025-11-12T19:30:00Z",
                home = Team("City FC", "https://placehold.co/128x128?text=CITY"),
                away = Team("United FC", "https://placehold.co/128x128?text=UNITED"),
                highlightUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
            ),
            Match(
                id = "2",
                league = "La Liga",
                score = "0-0",
                startTime = "2025-11-12T20:00:00Z",
                home = Team("Madrid", "https://placehold.co/128x128?text=MAD"),
                away = Team("Barca", "https://placehold.co/128x128?text=BAR"),
                highlightUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4"
            ),
            Match(
                id = "3",
                league = "Serie A",
                score = "1-3",
                startTime = "2025-11-12T21:00:00Z",
                home = Team("Milan", "https://placehold.co/128x128?text=MIL"),
                away = Team("Juve", "https://placehold.co/128x128?text=JUV"),
                highlightUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"
            )
        )
        trySend(base)
        // No periodic updates for simplicity; real-time preview stable
        awaitClose { /* nothing */ }
    }
}
