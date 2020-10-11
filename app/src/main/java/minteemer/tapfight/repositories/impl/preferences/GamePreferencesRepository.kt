package minteemer.tapfight.repositories.impl.preferences

import android.content.Context
import android.content.SharedPreferences
import minteemer.tapfight.TapFightApplication
import minteemer.tapfight.repositories.preferences.GamePreferencesRepository

class GamePreferencesRepositoryImpl private constructor(preferences: SharedPreferences) :
        BaseSharedPreferences(preferences), GamePreferencesRepository {

    override var fightTimeSec : Long by longPreference("fight_time_sec", 20)

    companion object {
        private const val PREFERENCES_FILE_NAME = "game_preferences"

        val INSTANCE: GamePreferencesRepository by lazy {
            GamePreferencesRepositoryImpl(
                    TapFightApplication.instance.getSharedPreferences(
                            PREFERENCES_FILE_NAME,
                            Context.MODE_PRIVATE
                    )
            )
        }
    }
}
