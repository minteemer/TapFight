package valiev.timur.tapfight.presentation.game.view

import valiev.timur.tapfight.domain.entities.PlayerId

interface GameView {
    fun updatePlayerScore(player: PlayerId, score: Int)

    fun endGame(p1Score: Int, p2Score: Int)
    fun startTimerAnimation()
}
