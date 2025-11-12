androidApplication {
    namespace = "org.example.app"

    dependencies {
        implementation("org.apache.commons:commons-text:1.11.0")
        implementation(project(":utilities"))

        // Android TV UI
        implementation("androidx.leanback:leanback:1.2.0")
        implementation("androidx.leanback:leanback-paging:1.1.0-alpha09")

        // AndroidX core + lifecycle + fragment (required for FragmentActivity)
        implementation("androidx.core:core-ktx:1.13.1")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
        implementation("androidx.fragment:fragment-ktx:1.8.3")
        implementation("androidx.activity:activity-ktx:1.9.3")

        // Firebase Realtime Database (use ktx artifact with available version)
        implementation("com.google.firebase:firebase-database-ktx:21.0.0")
        implementation("com.google.firebase:firebase-common:21.0.0")
        implementation("com.google.android.gms:play-services-base:18.5.0")

        // Media3 ExoPlayer for highlights
        implementation("androidx.media3:media3-exoplayer:1.4.1")
        implementation("androidx.media3:media3-ui:1.4.1")

        // Image loading
        implementation("io.coil-kt:coil:2.6.0")
        implementation("io.coil-kt:coil-svg:2.6.0")

        // JUnit4 for unit test discovery without explicit JUnit Platform configuration
        implementation("junit:junit:4.13.2")
        // Ensure JUnit Platform engine is available so JUnit Jupiter tests are discovered
        implementation("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    }
}
