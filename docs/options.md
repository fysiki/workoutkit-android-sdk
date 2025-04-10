# Options
WorkoutKit allows you to customize the **theme** of the workout experience.

To modify **theme settings** (colors, fonts, CTA styles, localization), please contact our team.

## Configuration
You can configure workout screen to change some texts and screen behaviour before presenting the screen.

Use the configuration before launchnig the workout.

**Example**
```kotlin
val workoutConfig = JSONObject().apply {
    put("isHumanAudioCoachAvailable", true)
    put("texts", JSONObject(
        mapOf(
            "startWorkoutCTA" to "On y va !",
            "endedWorkoutTitle" to "Bravo !",
            "saveWorkoutCTA" to "Quitter"
        )
    ))
}
```

## Available keys
### Start screen
Key  | Usage
------------ | -------------------------------------
`startWorkoutCTA`     | Start screen button text


### Speech prompts
WorkoutKit displays a Voice coach overview prompt at the first launch of a workout. It presents the voice coach feature. This prompt is displayed at each workout start until the user answers the Voice coach confirmation prompt.

Voice coach confirmation prompt is displayed at the end of the first section block.

Once Voice confirmation has been answered, no more voice coach prompts will be displayed.

Key  | Usage
------------ | -------------------------------------
`speechOverviewPromptTitle`     | Voice coach overview prompt title
`speechOverviewPromptHint`      | Voice coach overview prompt subtitle
`speechOverviewTurnOnCTA`       | Voice coach overview prompt Activate button text
`speechOverviewTurnOffCTA`      | Voice coach overview prompt Disable button text
`speechConfirmationPromptTitle` | Voice coach confirmation prompt title
`speechConfirmationPromptHint`  | Voice coach confirmation prompt subtitle
`speechConfirmationKeepCTA`     | Voice coach confirmation prompt Keep voice button text
`speechConfirmationTurnOffCTA`  | Voice coach confirmation prompt Disable button text

You can disable voice coach prompts with the following key.

Key  | Usage
------------ | -------------------------------------
`isHumanAudioCoachAvailable` (bool) | Add Voice coach overview prompts if available

### End screen
Key  | Usage
------------ | -------------------------------------
`endedWorkoutTitle`     | End screen main text
`saveWorkoutCTA`        | End screen button text
