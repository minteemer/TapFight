package minteemer.tapfight.repository.config

import minteemer.tapfight.data.preferences.GamePreferences

internal class GameConfigRepositoryImpl(
    private val gamePreferences: GamePreferences = GamePreferences.INSTANCE
) : GameConfigRepository {

    override val speedTapTimeoutSec: Long
        get() = gamePreferences.speedTapTimeoutSec

    override val bubblesTimeoutSec: Long
        get() = gamePreferences.bubblesTimeoutSec

    override val bubbles: Int
        get() = gamePreferences.bubbles

    override val initialBubbleSpawnDelayMills: Long = 200
}
