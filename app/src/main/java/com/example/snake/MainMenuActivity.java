package com.example.snake;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import static com.example.snake.SnakeActivity.CURRENTACTIVE;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainMenuActivity extends AppCompatActivity {

    ImageView imageButtonStart;
    ImageView imageButtonExit;
    ImageView imageMainLogo;
    public static int screenWidth;
    public static int screenHeight;

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
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
            screenHeight = size.y;

            setContentView(R.layout.menu_main);
            imageButtonStart = (ImageView) findViewById(R.id.image_button_start);
            imageButtonExit = (ImageView) findViewById(R.id.image_button_exit);
            imageMainLogo = (ImageView) findViewById(R.id.image_app_title_snake_2d);

            Bitmap bitmapStart = BitmapFactory.decodeResource(getResources(), R.drawable.button_start);
            Bitmap bitmapExit = BitmapFactory.decodeResource(getResources(), R.drawable.button_exit);
            Bitmap bitmapLogo = BitmapFactory.decodeResource(getResources(), R.drawable.app_title_snake_2d);


            imageButtonStart.setImageBitmap(Bitmap.createScaledBitmap(bitmapStart, (int) (screenWidth * 0.4875), (int) (screenWidth * 0.1896), false));
            imageButtonExit.setImageBitmap(Bitmap.createScaledBitmap(bitmapExit, (int) (screenWidth * 0.4875), (int) (screenWidth * 0.1896), false));
            imageMainLogo.setImageBitmap(Bitmap.createScaledBitmap(bitmapLogo, (int) (screenWidth), (int) (screenHeight * 0.2375), false));
        }

        imageButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenuActivity.this, SelectionMenuActivity.class));
            }
        });

        imageButtonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });

    }

    @Override
    public void onBackPressed() {
    }

}