package minteemer.tapfight.presentation.startGame

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_start_game.*
import minteemer.tapfight.R
import minteemer.tapfight.presentation.bubbles.BubblesGameActivity

class StartGameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_game)

        start_game_button.setOnClickListener {
            startActivity(BubblesGameActivity.createIntent(this))
        }
    }

    companion object {

        fun createIntent(context: Context) = Intent(context, StartGameActivity::class.java)
    }
}