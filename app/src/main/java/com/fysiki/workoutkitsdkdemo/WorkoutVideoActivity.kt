package com.fysiki.workoutkitsdkdemo

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.WindowManager
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.appcompat.app.AlertDialog
import com.fysiki.workoutkit.GoAction
import com.fysiki.workoutkit.fragments.WorkoutVideoFragment
import com.fysiki.workoutkit.interfaces.WorkoutVideoKitInterface
import com.fysiki.workoutkit.states.SaveWorkoutState
import com.fysiki.workoutkit.utils.DispatcherUtils
import com.fysiki.workoutkit.utils.JWTVerificationException
import com.fysiki.workoutkitsdkdemo.databinding.ActivityWorkoutBinding
import org.jdeferred.Promise
import org.jdeferred.impl.DeferredObject
import org.json.JSONObject

class WorkoutVideoActivity : AppCompatActivity(), WorkoutVideoKitInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        if (Build.VERSION.SDK_INT in 21..29) {
            window.statusBarColor = Color.TRANSPARENT
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.decorView.systemUiVisibility =
                SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_STABLE

        } else
            window.statusBarColor = Color.TRANSPARENT
            // Making status bar overlaps with the activity
            WindowCompat.setDecorFitsSystemWindows(window, false)

        val binding = ActivityWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // config
        val workoutConfig = JSONObject().apply {
            put("texts", JSONObject(
                mapOf(
                    "startWorkoutCTA" to "On y va !",
                    "endedWorkoutTitle" to "Bravo !",
                    "saveWorkoutCTA" to "Quitter"
            )))
        }

        intent.getStringExtra(WORKOUT_CONTENT_KEY)?.let {
            WorkoutVideoFragment.newInstance(
                JSONObject(it),
                workoutConfig,
                intent.getStringExtra(JWT_TOKEN_KEY).toString(),
            )?.let { workoutVideoFragment ->
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.goFragment, workoutVideoFragment, "VIDEO_FRAGMENT")
                transaction.commit()
            } ?: run {
                Toast.makeText(this, "Error creating fragment", Toast.LENGTH_SHORT).show()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API 33+
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT
            ) {
                DispatcherUtils.dispatch(GoAction.Workout.BackPressed())
            }
        } else {
            onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    DispatcherUtils.dispatch(GoAction.Workout.BackPressed())
                }
            })
        }
    }

    // Keep screen ON
    override fun onResume() {
        super.onResume()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onStop() {
        super.onStop()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    companion object {
        const val WORKOUT_CONTENT_KEY = "WorkoutVideoActivity.workoutContent"
        const val JWT_TOKEN_KEY = "WorkoutVideoActivity.jwtToken"

        fun newIntent(context: Context, workoutContent: JSONObject, jwtToken: String): Intent {
            val intent = Intent(context, WorkoutVideoActivity::class.java)
            intent.putExtra(WORKOUT_CONTENT_KEY, workoutContent.toString())
            intent.putExtra(JWT_TOKEN_KEY, jwtToken)
            return intent
        }
    }

    override fun goToNextScreen() {
        // TODO: Implement End Screen
        finish()
    }

    override fun logError(message: String, data: HashMap<String, Any>) {

    }

    override fun reviseExercise() {

    }

    override fun onTokenVerificationError(exception: JWTVerificationException) {
        runOnUiThread {
            val message = "Le token JWT est invalide : ${exception.message}"
            val spannableMessage = SpannableString(message).apply {
                setSpan(
                    ForegroundColorSpan(Color.BLACK), // Change to any color (e.g., Color.RED)
                    0,
                    message.length,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )
            }
            AlertDialog.Builder(this)
                .setTitle("Token Verification Error")
                .setMessage(spannableMessage)
                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                .setOnDismissListener { finish() }
                .show()
        }
    }

    override fun saveWorkout(saveState: SaveWorkoutState): Promise<*, *, *> {
        return DeferredObject<SaveWorkoutState, Throwable, Throwable>().resolve(saveState)
    }
}