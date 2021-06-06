package minteemer.tapfight.repository.config

import minteemer.tapfight.data.preferences.GamePreferences
import javax.inject.Inject

internal class GameConfigRepositoryImpl @Inject constructor(
    private val gamePreferences: GamePreferences
) : GameConfigRepository {

    override val speedTappingGameModeDurationSec: Long
        get() = gamePreferences.speedTapTimeoutSec

    override val bubblesGameModeDurationSec: Long
        get() = gamePreferences.bubblesTimeoutSec

    override val bubbles: Int
        get() = gamePreferences.bubbles

    override val initialBubbleSpawnDelayMills: Long = 200
}
