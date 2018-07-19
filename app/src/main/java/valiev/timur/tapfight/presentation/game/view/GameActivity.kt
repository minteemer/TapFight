package valiev.timur.tapfight.presentation.game.view

import android.animation.ObjectAnimator
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_game.*


import valiev.timur.tapfight.presentation.gameOver.GameOverActivity
import valiev.timur.tapfight.R
import valiev.timur.tapfight.domain.entities.PlayerId
import valiev.timur.tapfight.presentation.game.presenter.GamePresenter
import valiev.timur.tapfight.repositories.impl.preferences.GamePreferencesDAO
import valiev.timur.tapfight.repositories.preferences.GamePreferencesRepository


class GameActivity : AppCompatActivity(), GameView {

    private var presenter = GamePresenter(this)

    private val preferences: GamePreferencesRepository = GamePreferencesDAO.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        presenter.startGame()
    }

    override fun updatePlayerScore(player: PlayerId, score: Int) {
        when(player){
            PlayerId.P1 -> p1_score
            PlayerId.P2 -> p2_score
        }.text = score.toString()
    }

    override fun startTimerAnimation(){
        val progressAnimator = ObjectAnimator.ofFloat(progress_game_time, "progress", 0.0f,1.0f)
        progressAnimator.duration = preferences.fightTimeSec
        progressAnimator.start()
    }

    override fun endGame(p1Score: Int, p2Score: Int) {
        val intent = Intent(this, GameOverActivity::class.java)
        intent.putExtra("P1_SCORE", p1Score)
        intent.putExtra("P2_SCORE", p2Score)
        startActivity(intent)
        finish()
    }

    fun onTapBtn(view: View) {
        when (view.id) {
            R.id.button_p1 -> presenter.tapBtn(PlayerId.P1)
            R.id.button_p2 -> presenter.tapBtn(PlayerId.P2)
        }
    }

}
