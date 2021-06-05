package minteemer.tapfight.data.preferences

import android.content.Context
import minteemer.tapfight.TapFightApplication

class GamePreferences private constructor(context: Context) : BaseSharedPreferences(context, PREFERENCES_FILE_NAME) {

    var bubblesTimeoutSec: Long by longPreference(key = "bubbles_timeout_sec", default = 20)

    var speedTapTimeoutSec: Long by longPreference(key = "speed_tap_timout_sec", default = 10)

    var bubbles: Int by intPreference(key = "bubbles_number", default = 3)
        private set

    companion object {
        private const val PREFERENCES_FILE_NAME = "game_preferences"

        val INSTANCE: GamePreferences by lazy { GamePreferences(TapFightApplication.instance) }
    }
}
