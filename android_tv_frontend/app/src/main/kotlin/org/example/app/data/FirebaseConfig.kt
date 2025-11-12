package org.example.app.data

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

/**
 * FirebaseConfig initializes Firebase from environment-like placeholders stored in resources.
 * For CI/preview where keys are not present, initialization is skipped (mock mode).
 */
object FirebaseConfig {
    private const val TAG = "FirebaseConfig"

    // PUBLIC_INTERFACE
    fun initializeIfPossible(context: Context): Boolean {
        /** Attempts to initialize Firebase using string resources for keys. Returns whether initialized. */
        return try {
            val projectId = getStringResOrNull(context, "firebase_project_id")
            val appId = getStringResOrNull(context, "firebase_application_id")
            val apiKey = getStringResOrNull(context, "firebase_api_key")
            if (projectId.isNullOrBlank() || appId.isNullOrBlank() || apiKey.isNullOrBlank()) {
                Log.w(TAG, "Firebase not configured. Running in MOCK mode.")
                false
            } else {
                if (FirebaseApp.getApps(context).isEmpty()) {
                    val options = FirebaseOptions.Builder()
                        .setProjectId(projectId)
                        .setApplicationId(appId)
                        .setApiKey(apiKey)
                        .build()
                    FirebaseApp.initializeApp(context, options)
                }
                true
            }
        } catch (e: Throwable) {
            Log.w(TAG, "Firebase init failed, falling back to MOCK mode", e)
            false
        }
    }

    private fun getStringResOrNull(context: Context, name: String): String? {
        val id = context.resources.getIdentifier(name, "string", context.packageName)
        return if (id != 0) context.getString(id) else null
    }
}
