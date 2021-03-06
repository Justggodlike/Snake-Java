package com.example.snake;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.snake.MainMenuActivity.screenHeight;
import static com.example.snake.MainMenuActivity.screenWidth;

public class GameOverActivity extends AppCompatActivity {

    TextView finalScoreReview;
    ImageView restartButton;
    ImageView gameOverAnnouncement;
    ImageView finalScoreAnnouncement;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        {
            //Remove title bar
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);

            //Remove notification bar
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();
        }

        {
            setContentView(R.layout.gameover_main);
            finalScoreReview = (TextView) findViewById(R.id.finalScoreReview);
            restartButton = (ImageView) findViewById(R.id.restartButton);
            gameOverAnnouncement = (ImageView) findViewById(R.id.gameOverAnnouncement);
            finalScoreAnnouncement = (ImageView) findViewById(R.id.finalScoreAnnouncement);

            finalScoreReview.setText(Integer.toString(SnakeActivity.snake_size - 3));

            Bitmap bitmapFinalScoreAnnouncement = BitmapFactory.decodeResource(getResources(), R.drawable.text_final_score);
            Bitmap bitmapGameOverAnnouncement = BitmapFactory.decodeResource(getResources(), R.drawable.text_game_over);
            Bitmap bitmapRestartButton = BitmapFactory.decodeResource(getResources(), R.drawable.button_restart);

            restartButton.setImageBitmap(Bitmap.createScaledBitmap(bitmapRestartButton, (int) (screenWidth * 0.3625), (int) (screenWidth * 0.141), false));
            gameOverAnnouncement.setImageBitmap(Bitmap.createScaledBitmap(bitmapGameOverAnnouncement, (int) (screenWidth * 0.3583), (int) (screenWidth * 0.1393), false));
            finalScoreAnnouncement.setImageBitmap(Bitmap.createScaledBitmap(bitmapFinalScoreAnnouncement, (int) (screenWidth * 0.78125), (int) (screenWidth * 0.3038), false));
        }

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SnakeActivity.gameOver = false;
                startActivity(new Intent(GameOverActivity.this, SnakeActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GameOverActivity.this, MainMenuActivity.class));
    }

}
