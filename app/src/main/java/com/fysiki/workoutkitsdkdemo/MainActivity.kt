package com.fysiki.workoutkitsdkdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fysiki.workoutkit.WorkoutKit
import com.fysiki.workoutkitsdkdemo.type.WorkoutFormat
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Init workout kit sdk
        WorkoutKit.initialize(application)
        CloudClient.initialize(application, BuildConfig.CLOUD_URL)

        val viewModel = MainActivityViewModel.Factory(
            WorkoutRepository()
        ).create(MainActivityViewModel::class.java)

        viewModel.getDemoWorkouts()

        viewModel.workoutToLaunch.observe(this) { workout ->
            if (workout == null) return@observe
            if (workout.isVideo) {
                startActivity(WorkoutVideoActivity.newIntent(this, workout.content, workout.JwtToken))
            } else {
                startActivity(WorkoutActivity.newIntent(this, workout.content, workout.JwtToken))
            }
        }

        setContent {
            val itemList = viewModel.workouts.observeAsState(initial = emptyList())
            val isLoaderDisplayed = viewModel.isLoading.observeAsState(initial = true)
            val workoutPreview = viewModel.workoutPreviewItemToDisplay.observeAsState(initial = null)
            val error = viewModel.error.observeAsState(initial = null)

            error.value?.let { errorMessage ->
                val onDismiss = {
                    viewModel.resetError()
                }
                AlertDialog(
                    onDismissRequest = onDismiss,
                    title = { Text(text = "Erreur") },
                    text = { Text(text = errorMessage) },
                    confirmButton = {
                        TextButton(onClick = onDismiss) {
                            Text("Ok")
                        }
                    }
                )
            }

            workoutPreview.value?.let { previewData ->
                val onDismiss = {
                    viewModel.resetWorkoutPreviewDialog()
                }
                AlertDialog(
                    onDismissRequest = onDismiss,
                    title = { Text(text = previewData.title) },
                    text = { Text(text = previewData.body) },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.getDemoWorkoutContent(
                                previewData.id,
                                previewData.format == WorkoutFormat.PLAY
                            )
                            onDismiss.invoke()
                        }) {
                            Text("Ok")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = onDismiss) {
                            Text("Annuler")
                        }
                    }
                )
            }

            WorkoutKitTestTheme {
                if (isLoaderDisplayed.value) {
                    Box {
                        CircularProgressIndicator()
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                        .systemBarsPadding()
                        .windowInsetsPadding(
                            WindowInsets.navigationBars
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Workout Kit SDK Demo",
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        fontSize = 24.sp,
                        color = Color.White
                    )

                    LazyVerticalStaggeredGrid(
                        modifier = Modifier.fillMaxSize(),
                        columns = StaggeredGridCells.Fixed(2),
                        verticalItemSpacing = 16.dp,
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        items(items = itemList.value) {
                            WorkoutTile(it) { id ->
                                viewModel.getDemoWorkout(id)
                            }
                        }
                    }
                }
            }
        }
    }
}

data class Workout(val isVideo: Boolean, val content: JSONObject, val JwtToken: String)