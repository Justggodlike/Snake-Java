package com.example.snake;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.snake.MainMenuActivity.screenWidth;
import static com.example.snake.SnakeActivity.CURRENTACTIVE;

public class DonateActivity extends AppCompatActivity {
    ImageView donateOneDollarButton;
    ImageView donateTenDollarsButton;
    ImageView backButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CURRENTACTIVE = false;

        {
            //Remove title bar
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);

            //Remove notification bar
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();
        }

        {
            setContentView(R.layout.donate_main);
            donateOneDollarButton = (ImageView) findViewById(R.id.image_button_donate_one_dollar);
            donateTenDollarsButton = (ImageView) findViewById(R.id.image_button_donate_ten_dollars);
            backButton = (ImageView) findViewById(R.id.image_button_back);

            Bitmap bitmapDonateDollarsButton = BitmapFactory.decodeResource(getResources(), R.drawable.button_donate_money);
            Bitmap bitmapBackButton = BitmapFactory.decodeResource(getResources(), R.drawable.button_back);

            donateOneDollarButton.setImageBitmap(Bitmap.createScaledBitmap(bitmapDonateDollarsButton, (int) (screenWidth * 0.1792), (int) (screenWidth * 0.1792), false));
            donateTenDollarsButton.setImageBitmap(Bitmap.createScaledBitmap(bitmapDonateDollarsButton, (int) (screenWidth * 0.1792), (int) (screenWidth * 0.1792), false));
            backButton.setImageBitmap(Bitmap.createScaledBitmap(bitmapBackButton, (int) (screenWidth * 0.602), (int) (screenWidth * 0.2481), false));

            donateOneDollarButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            donateTenDollarsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(DonateActivity.this, GameOverActivity.class));
                }
            });

        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(DonateActivity.this, GameOverActivity.class));
    }

}
