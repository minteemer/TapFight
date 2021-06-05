package minteemer.tapfight.repository.config

interface GameConfigRepository {

    /**
     *  Duration of speed tapping game in seconds
     */
    val speedTapTimeoutSec: Long

    /**
     *  Duration of bubbles game in seconds
     */
    val bubblesTimeoutSec: Long

    /**
     * Number of bubbles to be spawned
     */
    val bubbles: Int

    /**
     * Delay between initial spawn of bubbles in milliseconds
     */
    val initialBubbleSpawnDelayMills: Long
}