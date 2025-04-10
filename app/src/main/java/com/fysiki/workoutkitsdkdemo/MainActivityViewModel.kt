import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.api.toJson
import com.fysiki.workoutkit.utils.JWTVerifier
import com.fysiki.workoutkitsdkdemo.Workout
import com.fysiki.workoutkitsdkdemo.WorkoutRepository
import com.fysiki.workoutkitsdkdemo.type.WorkoutFormat
import com.fysiki.workoutkitsdkdemo.type.WorkoutType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.Date

// ... (Assuming WorkoutRepository, WorkoutBlockSessionItem, and the response data classes are defined elsewhere)

data class WorkoutPreview(
    val id: String,
    val name: String,
    val type: WorkoutType,
    val duration: Int,
    val format: WorkoutFormat,
    val picture: Any?,
)

class MainActivityViewModel(
    private val workoutRepository: WorkoutRepository,
    private val deviceUuid: String,
    private val packageName: String
) : ViewModel() {

    // MainActivityViewModel Factory
    class Factory(
        private val workoutRepository: WorkoutRepository,
        private val deviceUuid: String,
        private val packageName: String
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainActivityViewModel(workoutRepository, deviceUuid, packageName) as T
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

    fun getDemoWorkouts() {
        _isLoading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    workoutRepository.getDemoWorkouts()
                }
                val workoutPreviews = response.data?.publicWorkoutSessions?.edges?.map { edge ->
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
                _workouts.value = workoutPreviews
            } catch (e: Exception) {
                _error.value = "Failed to load workouts: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getDemoWorkout(id: String, isVideo: Boolean) {
        // Similar to getDemoWorkouts, but for a single workout
        _isLoading.value = true
        _error.value = null
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = withContext(Dispatchers.IO) {
                    workoutRepository.getDemoWorkout(id)
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
                }
            }
             catch (e: Exception) {
                _error.postValue("Failed to load workouts: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}