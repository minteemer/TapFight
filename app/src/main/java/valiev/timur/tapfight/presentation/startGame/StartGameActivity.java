package valiev.timur.tapfight.presentation.startGame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import valiev.timur.tapfight.R;
import valiev.timur.tapfight.presentation.game.view.GameActivity;

public class StartGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);
    }

    public void startGame(View view) {
        startActivity(new Intent(this, GameActivity.class));
    }
}
