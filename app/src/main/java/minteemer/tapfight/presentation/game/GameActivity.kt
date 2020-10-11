package minteemer.tapfight.presentation.game

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
import minteemer.tapfight.domain.entities.Player

class GameActivity : AppCompatActivity() {

    private val viewModel: GameViewModel by viewModels()

    // TODO make animator
    private var p1Score: Int = 1
    private var p2Score: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        tap_area_player1.setOnBubbleTapListener { viewModel.onTap(Player.P1) }
        tap_area_player2.setOnBubbleTapListener { viewModel.onTap(Player.P2) }

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

        viewModel.gameState.observe(this) { gameState ->
            when (gameState) {
                is GameViewModel.GameState.Started -> startTimerAnimation(gameState.duration)
                is GameViewModel.GameState.GameOver -> finishGame(gameState.player1Score, gameState.player2Score)
            }
        }

        lifecycle.addObserver(viewModel)
    }

    override fun onStart() {
        super.onStart()
        tap_area_player1.startSpawning()
        tap_area_player2.startSpawning()
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

        fun createIntent(context: Context) = Intent(context, GameActivity::class.java)
    }
}
