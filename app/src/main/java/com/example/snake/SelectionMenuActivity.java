package com.example.snake;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import java.util.Random;
import static com.example.snake.MainMenuActivity.screenHeight;
import static com.example.snake.MainMenuActivity.screenWidth;
import static com.example.snake.SnakeActivity.CURRENTACTIVE;

public class SelectionMenuActivity extends AppCompatActivity {

    SeekBar seekBarSpeed;
    SeekBar seekBarField;
    TextView textSeekBarSpeed;
    TextView textSeekBarField;
    ImageView imageButtonClassic;
    ImageView imageButtonCustom;
    CheckBox wallsEnabledChip;
    static int CUSTOM_SPEED;
    static boolean WALLS_ENABLED;
    static int CUSTOM_FIELD;
    static boolean CUSTOM_GAME;

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

        setContentView(R.layout.menu_selection);

        {

            CURRENTACTIVE = false;
            CUSTOM_SPEED = 500;
            CUSTOM_FIELD = 10;
            WALLS_ENABLED = false;
            CUSTOM_GAME = false;

            seekBarSpeed = (SeekBar) findViewById(R.id.seekBarSpeed);
            seekBarField = (SeekBar) findViewById(R.id.seekBarField);
            textSeekBarSpeed = (TextView) findViewById(R.id.textSeekBarSpeed);
            textSeekBarField = (TextView) findViewById(R.id.textSeekBarField);
            imageButtonClassic = (ImageView) findViewById(R.id.imageButtonClassic);
            imageButtonCustom = (ImageView) findViewById(R.id.imageButtonCustom);
            wallsEnabledChip = (CheckBox) findViewById(R.id.WallsEnabledChip);

            Bitmap bitmapCustom = BitmapFactory.decodeResource(getResources(), R.drawable.button_arrow);
            Bitmap bitmapClassic = BitmapFactory.decodeResource(getResources(), R.drawable.button_arrow);


            imageButtonClassic.setImageBitmap(Bitmap.createScaledBitmap(bitmapClassic, (int) (/*screenWidth * 0.3875*/104), (int) (/*screenWidth * 0.1507*/104), false));
            imageButtonCustom.setImageBitmap(Bitmap.createScaledBitmap(bitmapCustom, (int) (/*screenWidth * 0.3875*/104), (int) (/*screenWidth * 0.1507*/104), false));
        }

        imageButtonClassic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectionMenuActivity.this, SnakeActivity.class));
                CUSTOM_GAME = false;
            }
        });

        imageButtonCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectionMenuActivity.this, SnakeActivity.class));
                CUSTOM_SPEED = 1000 / seekBarSpeed.getProgress();
                CUSTOM_FIELD = seekBarField.getProgress();
                CUSTOM_GAME = true;
            }
        });

        seekBarSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub

                textSeekBarSpeed.setText("Speed: " + Integer.toString(seekBarSpeed.getProgress()) + "t/s");

            }
        });

        seekBarField.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub

                textSeekBarField.setText("Field: " + Integer.toString(seekBarField.getProgress()) + "x" + Integer.toString(seekBarField.getProgress()));

            }
        });

        wallsEnabledChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){WALLS_ENABLED = true;}
                else {WALLS_ENABLED = false;}
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SelectionMenuActivity.this, MainMenuActivity.class));
    }

}