package minteemer.tapfight.presentation.startGame

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import minteemer.tapfight.databinding.ActivityStartGameBinding
import minteemer.tapfight.presentation.bubbles.BubblesGameActivity
import minteemer.tapfight.util.extensions.viewbinding.viewBinding

class StartGameActivity : AppCompatActivity() {

    private val binding: ActivityStartGameBinding by viewBinding(ActivityStartGameBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.startGameButton.setOnClickListener {
            startActivity(BubblesGameActivity.createIntent(this))
        }
    }

    companion object {

        fun createIntent(context: Context) = Intent(context, StartGameActivity::class.java)
    }
}
