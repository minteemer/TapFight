package minteemer.tapfight.presentation.gameOver

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_game_over.*
import minteemer.tapfight.R
import minteemer.tapfight.util.fastLazy

class GameOverActivity : AppCompatActivity() {

    private val p1Score by fastLazy { intent.getIntExtra(EXTRA_P1_SCORE, 0) }
    private val p2Score by fastLazy { intent.getIntExtra(EXTRA_P2_SCORE, 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over) // TODO add restart button

        p1_score.text = p1Score.toString()
        p2_score.text = p2Score.toString()

        winnerText.setText(
            when {
                p1Score > p2Score -> R.string.game_over_p1_wins
                p1Score < p2Score -> R.string.game_over_p2_wins
                else -> R.string.game_over_text_draw
            }
        )
    }

    companion object {
        private const val EXTRA_P1_SCORE = "p1_score"
        private const val EXTRA_P2_SCORE = "p2_score"

        fun createIntent(context: Context, player1Score: Int, player2Score: Int) =
            Intent(context, GameOverActivity::class.java)
                .putExtra(EXTRA_P1_SCORE, player1Score)
                .putExtra(EXTRA_P2_SCORE, player2Score)
    }
}