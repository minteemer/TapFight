package minteemer.tapfight.presentation.bubbles

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.LinearInterpolator
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import minteemer.tapfight.presentation.gameOver.GameOverActivity
import minteemer.tapfight.databinding.ActivityGameBinding
import minteemer.tapfight.domain.entity.*
import minteemer.tapfight.util.extensions.viewbinding.viewBinding

@AndroidEntryPoint
class BubblesGameActivity : AppCompatActivity() {

    private val binding: ActivityGameBinding by viewBinding(ActivityGameBinding::inflate)
    private val viewModel: BubblesGameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mapOf(
            binding.tapAreaPlayer1 to Player.P1,
            binding.tapAreaPlayer2 to Player.P2
        ).forEach { (tapArea, player) ->
            tapArea.setOnBubbleTapListener { viewModel.onBubbleTap(player) }
            tapArea.setOnBubbleTimeoutListener { viewModel.onBubbleTimeout(player) }
        }

        viewModel.scores.observe(this) { scores ->
            binding.p1Score.text = scores.player1.toString()
            binding.p2Score.text = scores.player2.toString()
            updateScore(scores)
        }

        viewModel.bubbleSpawnEvents.observe(this) { player ->
            when (player) {
                Player.P1 -> binding.tapAreaPlayer1.spawnBubble()
                Player.P2 -> binding.tapAreaPlayer2.spawnBubble()
                else -> Unit
            }
        }

        viewModel.gameState.observe(this) { gameState ->
            when (gameState) {
                is BubblesGameViewModel.GameState.Started -> startTimerAnimation(gameState.duration)
                is BubblesGameViewModel.GameState.GameOver -> finishGame(p1Score = gameState.scores.player1, p2Score = gameState.scores.player2)
            }
        }

        lifecycle.addObserver(viewModel)
    }

    private fun updateScore(scores: Scores) {
        val p1Score = scores.player1.coerceAtLeast(1)
        val p2Score = scores.player2.coerceAtLeast(1)
        binding.scoreRatioView.scoreRatio = p1Score / (p1Score + p2Score).toFloat()
    }

    private fun startTimerAnimation(gameDuration: Long) {
        ObjectAnimator.ofFloat(
            binding.timerBarView,
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
