package minteemer.tapfight.domain.entity

sealed class GameEvent {

    /**
     * The game is started.
     * @param duration Duration of the game in seconds
     */
    class Started(val duration: Long) : GameEvent()

    /**
     * Score of a player has changed.
     * @param scores Current scores of the players
     */
    class ScoreUpdated(val scores: Scores) : GameEvent()

    /**
     * A bubble has spawned.
     * @param player On which side bubble must be spawned
     */
    class SpawnBubble(val player: Player) : GameEvent()

    /**
     * The game is ended
     */
    object GameOver : GameEvent()
}
