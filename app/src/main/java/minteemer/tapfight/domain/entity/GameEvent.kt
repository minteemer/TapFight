package minteemer.tapfight.domain.entity

sealed class GameEvent {

    /**
     * The game is started.
     * @param duration Duration of the game in seconds
     */
    data class Started(val duration: Long) : GameEvent()

    /**
     * Score of a player has changed.
     * @param scores Current scores of the players
     */
    data class ScoreUpdated(val scores: Scores) : GameEvent()

    /**
     * A bubble has spawned.
     * @param player On which side bubble must be spawned
     */
    data class SpawnBubble(val player: Player) : GameEvent()

    /**
     * The game is ended
     */
    object GameOver : GameEvent() {
        override fun toString(): String = "GameOver"
    }
}
