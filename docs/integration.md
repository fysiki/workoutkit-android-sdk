### Classic Workout
WorkoutKit allows you to display a **classic workout** with a list of video exercises.

**Features:**
- Navigate between exercises.
- View exercise details.
- Play and pause the workout.
- Coach voice guidance for the user.

The SDK provides an **Android Fragment** that can be embedded in your activity layout.

---

**Fragment Initialization**
```kotlin
val workoutConfig = JSONObject().apply {
    put("isHumanAudioCoachAvailable", false)
    put("texts", JSONObject(
        mapOf(
            "startWorkoutCTA" to "On y va !",
            "endedWorkoutTitle" to "Bravo !",
            "saveWorkoutCTA" to "Quitter"
        )
    ))
} // Configuration of the workout (see [here](docs/options.md))

val goFragment = GoFragment.newInstance(
    workoutContent,                              // Data from our GraphQL API
    workoutConfig,                               // Configuration of the workout
    JwtToken                                     // JWT token from our GraphQL API
)
```

Video Workout
WorkoutKit also supports video-based workouts with the following features:

- Skip forward or backward 15 seconds.
- Play and pause the video.
Like the classic workout, the SDK provides an Android Fragment for easy integration into your activity layout.

**Fragment initialization**
```kotlin
val workoutContent = Data from our graphql api

val workoutConfig = JSONObject().apply {
    put("texts", JSONObject(mapOf(
        "startWorkoutCTA" to "On y va !",
        "endedWorkoutTitle" to "Bravo !",
        "saveWorkoutCTA" to "Quitter"
    )))
}

val videoFragment = WorkoutVideoFragment.newInstance(
    workoutContent,                              // Data from our GraphQL API
    workoutConfig,                               // Configuration of the workout
    JwtToken                                     // JWT token from our GraphQL API
)
```

**Keep screen on**
Add the following permission in AndroidManifest.xml to keep the screen on during the workout.
```xml
<uses-permission android:name="android.permission.WAKE_LOCK" />
```

Add the following code to your activity to keep the screen on during the workout.
```kotlin
    // Keep screen ON
override fun onResume() {
    super.onResume()
    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}

override fun onStop() {
    super.onStop()
    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}
```