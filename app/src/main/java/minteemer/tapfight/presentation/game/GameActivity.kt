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
import valiev.timur.tapfight.R
import minteemer.tapfight.domain.entities.Player

class GameActivity : AppCompatActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game) // TODO enlarge tapping area

        // TODO make better tap tracking
        button_p1.setOnClickListener { viewModel.onTap(Player.P1) }
        button_p2.setOnClickListener { viewModel.onTap(Player.P2) }

        viewModel.playerScores.observe(this) { scores ->
            p1_score.text = scores.player1Score.toString()
            p2_score.text = scores.player2Score.toString()
        }

        viewModel.gameState.observe(this) { gameState ->
            when (gameState) {
                is GameViewModel.GameState.Started -> startTimerAnimation(gameState.duration)
                is GameViewModel.GameState.GameOver -> finishGame(gameState.player1Score, gameState.player2Score)
            }
        }

        lifecycle.addObserver(viewModel)
    }

    private fun startTimerAnimation(gameDuration: Long) {
        ObjectAnimator.ofInt(
            progress_game_time,
            "progress",
            progress_game_time.progress,
            progress_game_time.max
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
