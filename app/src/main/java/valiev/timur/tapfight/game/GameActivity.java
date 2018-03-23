package valiev.timur.tapfight.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;

import valiev.timur.tapfight.GameOverActivity;
import valiev.timur.tapfight.Player;
import valiev.timur.tapfight.R;

public class GameActivity extends AppCompatActivity implements GameView {

    private HashMap<Player, TextView> scoreViews = new HashMap<>();
    private ProgressBar progressBar;

    private GamePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        scoreViews.put(Player.P1, (TextView) findViewById(R.id.p1Score));
        scoreViews.put(Player.P2, (TextView) findViewById(R.id.p2Score));

        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        progressBar.setMax(PresenterImpl.FIGHT_TIME);

        presenter = new PresenterImpl(this, new ModelImpl());
    }

    @Override
    public void updatePlayerScore(Player player, int score) {
        scoreViews.get(player).setText(Integer.toString(score));
    }

    @Override
    public void endGame(int p1Score, int p2Score) {
        Intent intent = new Intent(this, GameOverActivity.class);
        intent.putExtra("P1_SCORE", p1Score);
        intent.putExtra("P2_SCORE", p2Score);
        startActivity(intent);
        finish();
    }

    @Override
    public void updateProgressBar(int progress) {
        progressBar.setProgress(progress);
    }

    public void onTapBtn(View view) {
        switch (view.getId()) {
            case R.id.p1Button:
                presenter.tapBtn(Player.P1);
                break;
            case R.id.p2Button:
                presenter.tapBtn(Player.P2);
                break;
        }
    }
}
