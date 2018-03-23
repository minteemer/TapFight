package valiev.timur.tapfight;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        Intent intent = getIntent();

        int p1Count = intent.getIntExtra("P1_SCORE", 0);
        int p2Count = intent.getIntExtra("P2_SCORE", 0);
        System.out.println(p1Count + "/" + p2Count);

        TextView p1Score = findViewById(R.id.p1FinalScore);
        TextView p2Score = findViewById(R.id.p2FinalScore);
        TextView winner = findViewById(R.id.winnerText);

        p1Score.setText(Integer.toString(p1Count));
        p2Score.setText(Integer.toString(p2Count));

        if (p1Count > p2Count)
            winner.setText("Player 1 wins!");
        else if (p2Count > p1Count)
            winner.setText("Player 2 wins!");
    }

    public void onBackPressed() {
        startActivity(new Intent(this, StartGameActivity.class));
    }
}
