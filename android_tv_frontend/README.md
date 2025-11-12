# Live Sports Scores Android TV App

This Android TV app shows real-time sports matches using Firebase Realtime Database with a Leanback-based TV UI. It auto-updates without manual refresh, supports navigation to a match detail screen, and plays highlight videos using Media3 ExoPlayer.

Features:
- Leanback TV UI (rows by league, D-pad focusable cards)
- Live updates via Firebase Realtime Database listeners
- Match details screen with logos, score, and metadata
- Highlight video player (Media3 ExoPlayer)
- Mock fallback when Firebase is not configured so the preview still runs

Build
- From repo root:
  ./gradlew :app:installDebug
- Launch "Live Sports Scores TV" on an Android TV emulator (API 30+)

Firebase Setup
- This project initializes Firebase using three resource strings:
  - firebase_project_id
  - firebase_application_id
  - firebase_api_key
- Set these in app/src/main/res/values/strings.xml or via resource overlays.
- If any are empty, the app runs in MOCK mode and shows sample matches.
- Realtime Database expected path (default): liveMatches

Sample Realtime Database Schema:
liveMatches: {
  "1": {
    "league": "Premier League",
    "score": "2-1",
    "startTime": "2025-11-12T19:30:00Z",
    "home": {"name": "City FC", "logoUrl": "https://.../city.png"},
    "away": {"name": "United FC", "logoUrl": "https://.../united.png"},
    "highlightUrl": "https://.../highlight.mp4"
  },
  "2": { ... }
}

Notes
- The UI groups matches by `league` into Leanback rows.
- Selecting a match opens details; "Play Highlights" launches the player.
- Highlight playback uses Media3 ExoPlayer with a minimal setup.

Environment Variables
- Do not hardcode secrets in code. Use the string resources as placeholders that can be replaced in CI or with a flavors-specific resource file.

TV Best Practices
- D-pad focusable cards, large imagery, clear typography.
- Headers are hidden for a simplified browse; can be enabled if desired.
