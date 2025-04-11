package com.fysiki.workoutkitsdkdemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.api.toJson
import com.fysiki.workoutkitsdkdemo.type.WorkoutFormat
import com.fysiki.workoutkitsdkdemo.type.WorkoutType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

data class WorkoutPreview(
    val id: String,
    val name: String,
    val type: WorkoutType,
    val duration: Int,
    val format: WorkoutFormat,
    val picture: Any?,
)

data class WorkoutPreviewDialogData(
    val id: String,
    val title: String,
    val body: String,
    val format: WorkoutFormat,
)

class MainActivityViewModel(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    class Factory(
        private val workoutRepository: WorkoutRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainActivityViewModel(workoutRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    private val _workouts = MutableLiveData<List<WorkoutPreview>>()
    val workouts: LiveData<List<WorkoutPreview>> = _workouts

    private val _workoutToLaunch = MutableLiveData<Workout?>()
    val workoutToLaunch: LiveData<Workout?> = _workoutToLaunch


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _workoutPreviewItemToDisplay = MutableLiveData<WorkoutPreviewDialogData?>(null)
    val workoutPreviewItemToDisplay: LiveData<WorkoutPreviewDialogData?> = _workoutPreviewItemToDisplay

    fun getDemoWorkouts() {
        _isLoading.value = true
        _error.value = null
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = withContext(Dispatchers.IO) {
                    workoutRepository.getDemoWorkouts()
                }
                val workoutPreviews = response.data?.publicWorkouts?.edges?.map { edge ->
                    val item = edge.node.workoutPreviewItem
                    WorkoutPreview(
                        id = item.id,
                        name = item.name,
                        type = item.type,
                        duration = item.duration,
                        format = item.format,
                        picture = item.picture
                    )
                } ?: emptyList()
                _workouts.postValue(workoutPreviews)
            } catch (e: Exception) {
                _error.postValue("Impossible de récupérer les séances.")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun getDemoWorkoutContent(id: String, isVideo: Boolean) {
        _isLoading.value = true
        _error.value = null
        val errorMessage = "Impossible d’ouvrir la séance avec l’ID « $id »."
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = withContext(Dispatchers.IO) {
                    workoutRepository.getDemoWorkoutContent(id)
                }
                val json = response.data?.toJson()?.let {
                    JSONObject(it).optJSONObject("publicWorkoutSession")
                }
                val extensions = response.extensions["workoutkit"] as LinkedHashMap<*, *>
                if (json != null && extensions["token"] != null) {
                    _workoutToLaunch.postValue(
                        Workout(
                            isVideo,
                            json,
                            extensions["token"].toString()
                        )
                    )
                } else {
                    _error.postValue(errorMessage)
                }
            } catch (e: Exception) {
                _error.postValue(errorMessage)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun getDemoWorkout(id: String) {
        _isLoading.value = true
        _error.value = null
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = withContext(Dispatchers.IO) {
                    workoutRepository.getDemoWorkout(id)
                }
                val workoutPreviewItem = response.data?.publicWorkout?.workoutPreviewItem
                if (workoutPreviewItem != null) {
                    _workoutPreviewItemToDisplay.postValue(
                        WorkoutPreviewDialogData(
                            workoutPreviewItem.id,
                            workoutPreviewItem.name,
                            "Vous allez télécharger les contenus pour cette séance.",
                            workoutPreviewItem.format
                        )
                    )
                } else {
                    _error.postValue(
                        "Impossible de récupérer les informations de la séance pour l’ID « $id »."
                    )
                }
            }
             catch (e: Exception) {
                _error.postValue("Failed to load workouts: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun resetWorkoutPreviewDialog() {
        _workoutPreviewItemToDisplay.value = null
    }

    fun resetError() {
        _error.value = null
    }
}