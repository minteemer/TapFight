package minteemer.tapfight.presentation.bubbles

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.LinearInterpolator
import androidx.activity.viewModels
import kotlinx.android.synthetic.main.activity_game.*
import minteemer.tapfight.presentation.gameOver.GameOverActivity
import minteemer.tapfight.R
import minteemer.tapfight.domain.entity.Player

class BubblesGameActivity : AppCompatActivity() {

    private val viewModel: BubblesGameViewModel by viewModels()

    // TODO make animator
    private var p1Score: Int = 1
    private var p2Score: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        mapOf(
            tap_area_player1 to Player.P1,
            tap_area_player2 to Player.P2
        ).forEach { (tapArea, player) ->
            tapArea.setOnBubbleTapListener { viewModel.onBubbleTap(player) }
            tapArea.setOnBubbleTimeoutListener { viewModel.onBubbleTimeout(player) }
        }

        viewModel.player1Score.observe(this) { score ->
            p1Score = score
            p1_score.text = score.toString()
            updateScore()
        }
        viewModel.player2Score.observe(this) { score ->
            p2Score = score
            p2_score.text = score.toString()
            updateScore()
        }

        viewModel.bubbleSpawnEvents.observe(this) { player ->
            when (player) {
                Player.P1 -> tap_area_player1.spawnBubble()
                Player.P2 -> tap_area_player2.spawnBubble()
                else -> Unit
            }
        }

        viewModel.gameState.observe(this) { gameState ->
            when (gameState) {
                is BubblesGameViewModel.GameState.Started -> startTimerAnimation(gameState.duration)
                is BubblesGameViewModel.GameState.GameOver -> finishGame(gameState.player1Score, gameState.player2Score)
            }
        }

        lifecycle.addObserver(viewModel)
    }

    private fun updateScore() {
        val p1Score = p1Score.coerceAtLeast(1)
        val p2Score = p2Score.coerceAtLeast(1)
        score_ratio_view.scoreRatio = p1Score / (p1Score + p2Score).toFloat()
    }

    private fun startTimerAnimation(gameDuration: Long) {
        ObjectAnimator.ofFloat(
            timer_bar_view,
            "timeLeft",
            1f,
            0f
        ).apply {
            interpolator = LinearInterpolator()
            duration = gameDuration * 1000
            start()
        }
    }

    private fun finishGame(p1Score: Int, p2Score: Int) {
        startActivity(GameOverActivity.createIntent(this, p1Score, p2Score))
        finish()
    }

    companion object {

        fun createIntent(context: Context) = Intent(context, BubblesGameActivity::class.java)
    }
}
