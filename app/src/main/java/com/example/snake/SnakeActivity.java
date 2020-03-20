
package com.example.snake;

        import androidx.appcompat.app.ActionBar;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.constraintlayout.widget.ConstraintLayout;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.Bundle;
        import android.os.CountDownTimer;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.Window;
        import android.view.WindowManager;
        import android.widget.ImageView;
        import android.widget.TableRow;
        import android.widget.TextView;
        import java.lang.Math;
        import java.util.Random;

        import static com.example.snake.MainMenuActivity.screenWidth;
        import static com.example.snake.SelectionMenuActivity.CUSTOM_FIELD;
        import static com.example.snake.SelectionMenuActivity.CUSTOM_GAME;
        import static com.example.snake.SelectionMenuActivity.CUSTOM_SPEED;
        import static com.example.snake.SelectionMenuActivity.WALLS_ENABLED;

public class SnakeActivity extends AppCompatActivity {

    // declaration
    int MIN_SWIPE_DISTANCE;
    static boolean CURRENTACTIVE;
    int SPEED;
    int FIELD;
    static boolean gameOver;
    String generationMove;
    static int snake_size;
    int[][] state;
    int[][] snake_state;
    int[][] apple_state;
    String direction;
    ConstraintLayout mainLayout;
    ConstraintLayout gameOverLayout;
    TextView mainTextView;
    TextView scoreCounter;
    final Random random = new Random();
    Bitmap bitmapEmpty, bitmapApple;
    Bitmap[] bitmapHead, bitmapStraight, bitmapRound, bitmapTail;
    ImageView[][] imageView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        {
            //Remove title bar
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);

            //Remove notification bar
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();
        }//remove upper bar

        CURRENTACTIVE = true;
        gameOver = false;
        MIN_SWIPE_DISTANCE = 10;
        if(!CUSTOM_GAME){
            FIELD = 10;
            SPEED = 500;
        } else {
            FIELD = CUSTOM_FIELD;
            SPEED = CUSTOM_SPEED;
        }

        {
            setContentView(R.layout.activity_main2);
            mainLayout = (ConstraintLayout) findViewById(R.id.ConstraintLayout);
            gameOverLayout = (ConstraintLayout) findViewById(R.id.gameOverLayout);
            mainTextView = (TextView) findViewById(R.id.mainTextView);
            scoreCounter = (TextView) findViewById(R.id.scoreCounter);

            state = new int[FIELD][FIELD];
            snake_state = new int[(int) Math.pow(FIELD, 2)][2];
            // 0 - void, 1 - snake, 2 - head, 3 - tail,  5 - apple
            apple_state = new int[1][2];
            direction = "right";
            // "right", "left", "down", "up".
            generationMove = "right";
            snake_size = 3;

            bitmapEmpty = BitmapFactory.decodeResource(getResources(), R.drawable.tile_empty);
            bitmapApple = BitmapFactory.decodeResource(getResources(), R.drawable.tile_apple);
            bitmapHead = new Bitmap[4];
            bitmapStraight = new Bitmap[4];
            bitmapTail = new Bitmap[4];
            bitmapRound = new Bitmap[8];
            bitmapHead[0] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_snake_head2_d);
            bitmapHead[1] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_snake_head2_l);
            bitmapHead[2] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_snake_head2_u);
            bitmapHead[3] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_snake_head2_r);
            bitmapStraight[0] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_snake_straight_u_d);
            bitmapStraight[1] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_snake_straight_r_l);
            bitmapStraight[2] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_snake_straight_d_u);
            bitmapStraight[3] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_snake_straight_l_r);
            bitmapRound[0] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_snake_round_u_r);
            bitmapRound[1] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_snake_round_r_d);
            bitmapRound[2] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_snake_round_d_l);
            bitmapRound[3] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_snake_round_l_u);
            bitmapRound[4] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_snake_round_r_u);
            bitmapRound[5] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_snake_round_d_r);
            bitmapRound[6] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_snake_round_l_d);
            bitmapRound[7] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_snake_round_u_l);
            bitmapTail[0] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_snake_tail2_u);
            bitmapTail[1] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_snake_tail2_r);
            bitmapTail[2] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_snake_tail2_d);
            bitmapTail[3] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_snake_tail2_l);

            imageView = new ImageView[FIELD][FIELD];
        } // initialization

        setup();

        countdown();

        {
            final int[] startX = {0};
            final int[] startY = {0};
            final int[] endX = {0};
            final int[] endY = {0};
            final int[] dX = {0};
            final int[] dY = {0};

            mainLayout.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            startX[0] = (int) event.getX();
                            startY[0] = (int) event.getY();
                            break;
                        case MotionEvent.ACTION_UP:
                            endX[0] = (int) event.getX();
                            endY[0] = (int) event.getY();
                            dX[0] = Math.abs(endX[0] - startX[0]);
                            dY[0] = Math.abs(endY[0] - startY[0]);
                            performActions(startX[0], startY[0], endX[0], endY[0], dX[0], dY[0]);
                            break;
                    }
                    return true;
                }
            });
        } //movement

    }

    void countdown() {
        new CountDownTimer(SPEED, 1000) {

            public void onTick(long millisUntilFinished) {}

            public void onFinish() {
                if(gameOver == false) {if(CURRENTACTIVE){Move();}}
                else {}
            }
        }.start();
    } // cycle movement function

    private void Move() {
        generationMove = direction;
        {
            boolean recalculate = false;
            if (generationMove == "right") {

                if(!WALLS_ENABLED){

                if(snake_state[0][0] != FIELD - 1) {if(apple_state[0][0] == snake_state[0][0] + 1  && apple_state[0][1] == snake_state[0][1]) {snake_size++; recalculate = true;}}
                else {if(apple_state[0][0] == 0  && apple_state[0][1] == snake_state[0][1]) {snake_size++; recalculate = true;}}

                } else {
                    if(apple_state[0][0] == snake_state[0][0] + 1  && apple_state[0][1] == snake_state[0][1]) {snake_size++; recalculate = true;}
                }

                for(int i = snake_size - 1; i > 0; i--){
                    snake_state[i][0] = snake_state[i - 1][0];
                    snake_state[i][1] = snake_state[i - 1][1];
                }
                if(!WALLS_ENABLED){

                if(snake_state[0][0] != FIELD - 1) {snake_state[0][0]++;}
                else{snake_state[0][0] = 0;}

                } else {

                    if(snake_state[0][0] != FIELD - 1) {snake_state[0][0]++;}
                    else{gameOver = true;}

                }
            }

            if (generationMove == "left") {

                if(!WALLS_ENABLED){

                if(snake_state[0][0] != 0) {if(apple_state[0][0] == snake_state[0][0] - 1  && apple_state[0][1] == snake_state[0][1]) {snake_size++; recalculate = true;}}
                else {if(apple_state[0][0] == FIELD - 1  && apple_state[0][1] == snake_state[0][1]) {snake_size++; recalculate = true;}}

                } else {

                    if(snake_state[0][0] != 0) {if(apple_state[0][0] == snake_state[0][0] - 1  && apple_state[0][1] == snake_state[0][1]) {snake_size++; recalculate = true;}}

                }

                for(int i = snake_size-1; i > 0; i--){
                    snake_state[i][0] = snake_state[i - 1][0];
                    snake_state[i][1] = snake_state[i - 1][1];
                }

                if(!WALLS_ENABLED){
                if(snake_state[0][0] != 0) {snake_state[0][0]--;}
                else{snake_state[0][0] = FIELD - 1;}
                } else {
                    if(snake_state[0][0] != 0) {snake_state[0][0]--;}
                    else{gameOver = true;}
                }
            }

            if (generationMove == "up") {

                if(!WALLS_ENABLED){
                if(snake_state[0][1] != 0) {if(apple_state[0][0] == snake_state[0][0] && apple_state[0][1] == snake_state[0][1] - 1) {snake_size++; recalculate = true;}}
                else {if(apple_state[0][1] == FIELD - 1  && apple_state[0][0] == snake_state[0][0]) {snake_size++; recalculate = true;}}
                } else {
                    if(snake_state[0][1] != 0) {if(apple_state[0][0] == snake_state[0][0] && apple_state[0][1] == snake_state[0][1] - 1) {snake_size++; recalculate = true;}}
                }

                for(int i = snake_size-1; i > 0; i--){
                    snake_state[i][0] = snake_state[i - 1][0];
                    snake_state[i][1] = snake_state[i - 1][1];
                }

                if(!WALLS_ENABLED){
                if(snake_state[0][1] != 0) {snake_state[0][1]--;}
                else{snake_state[0][1] = FIELD - 1;}
                } else {
                    if(snake_state[0][1] != 0) {snake_state[0][1]--;}
                    else{gameOver = true;}
                }
            }

            if (generationMove == "down") {

                if(!WALLS_ENABLED){
                if(snake_state[0][1] != FIELD - 1) {if(apple_state[0][0] == snake_state[0][0] && apple_state[0][1] == snake_state[0][1] + 1) {snake_size++; recalculate = true;}}
                else {if(apple_state[0][1] == 0  && apple_state[0][0] == snake_state[0][0]) {snake_size++; recalculate = true;}}
                } else {
                    if(snake_state[0][1] != FIELD - 1) {if(apple_state[0][0] == snake_state[0][0] && apple_state[0][1] == snake_state[0][1] + 1) {snake_size++; recalculate = true;}}
                }

                for(int i = snake_size-1; i > 0; i--){
                    snake_state[i][0] = snake_state[i - 1][0];
                    snake_state[i][1] = snake_state[i - 1][1];
                }

                if(!WALLS_ENABLED){
                if(snake_state[0][1] != FIELD - 1) {snake_state[0][1]++;}
                else{snake_state[0][1] = 0;}
                } else {
                    if(snake_state[0][1] != FIELD - 1) {snake_state[0][1]++;}
                    else{gameOver = true;}
                }
            }

            performStateCalculation();
            if (state[snake_state[0][1]][snake_state[0][0]] == 3 || state[snake_state[0][1]][snake_state[0][0]] == 1) {gameOver = true; gameOver();}
            if (recalculate){apple_initialization(); Recalculate(); recalculate = false;}
            if(!gameOver) {performStateReformation();}
            //if(!gameOver) {mainTextView.setText(performStateReformation()); scoreCounter.setText(Integer.toString(snake_size - 3));}
        }
        countdown();
    } // Movement performation

    void gameOver() {
        startActivity(new Intent(SnakeActivity.this, GameOverActivity.class));
    }

    void setup() {

        if(gameOver){setContentView(R.layout.activity_main);}

        {

            for (int i = 0; i < (int) Math.pow(FIELD, 2); i++) {
                for (int k = 0; k < 2; k++) {
                    snake_state[i][k] = -1;
                }
            }

            for (int i = 0; i < FIELD; i++) {
                for (int k = 0; k < FIELD; k++) {
                    state[i][k] = 0;
                    imageView[i][k] = new ImageView(this);
                    imageView[i][k].setImageBitmap(Bitmap.createScaledBitmap(bitmapEmpty, (int)(screenWidth/FIELD), (int)(screenWidth/FIELD), false));
                }
            }

            for (int i = 0; i < FIELD; i++) {
                for (int k = 0; k < FIELD; k++) {
                    TableRow tableRow;
                    switch(i) {
                        case 0:
                            tableRow = (TableRow) findViewById(R.id.TableRow1);
                            tableRow.addView(imageView[i][k]);
                            break;
                        case 1:
                            tableRow = (TableRow) findViewById(R.id.TableRow2);
                            tableRow.addView(imageView[i][k]);
                            break;
                        case 2:
                            tableRow = (TableRow) findViewById(R.id.TableRow3);
                            tableRow.addView(imageView[i][k]);
                            break;
                        case 3:
                            tableRow = (TableRow) findViewById(R.id.TableRow4);
                            tableRow.addView(imageView[i][k]);
                            break;
                        case 4:
                            tableRow = (TableRow) findViewById(R.id.TableRow5);
                            tableRow.addView(imageView[i][k]);
                            break;
                        case 5:
                            tableRow = (TableRow) findViewById(R.id.TableRow6);
                            tableRow.addView(imageView[i][k]);
                            break;
                        case 6:
                            tableRow = (TableRow) findViewById(R.id.TableRow7);
                            tableRow.addView(imageView[i][k]);
                            break;
                        case 7:
                            tableRow = (TableRow) findViewById(R.id.TableRow8);
                            tableRow.addView(imageView[i][k]);
                            break;
                        case 8:
                            tableRow = (TableRow) findViewById(R.id.TableRow9);
                            tableRow.addView(imageView[i][k]);
                            break;
                        case 9:
                            tableRow = (TableRow) findViewById(R.id.TableRow10);
                            tableRow.addView(imageView[i][k]);
                            break;
                        case 10:
                            tableRow = (TableRow) findViewById(R.id.TableRow11);
                            tableRow.addView(imageView[i][k]);
                            break;
                        case 11:
                            tableRow = (TableRow) findViewById(R.id.TableRow12);
                            tableRow.addView(imageView[i][k]);
                            break;
                        case 12:
                            tableRow = (TableRow) findViewById(R.id.TableRow13);
                            tableRow.addView(imageView[i][k]);
                            break;
                        case 13:
                            tableRow = (TableRow) findViewById(R.id.TableRow14);
                            tableRow.addView(imageView[i][k]);
                            break;
                        case 14:
                            tableRow = (TableRow) findViewById(R.id.TableRow15);
                            tableRow.addView(imageView[i][k]);
                            break;
                        case 15:
                            tableRow = (TableRow) findViewById(R.id.TableRow16);
                            tableRow.addView(imageView[i][k]);
                            break;
                        case 16:
                            tableRow = (TableRow) findViewById(R.id.TableRow17);
                            tableRow.addView(imageView[i][k]);
                            break;
                        case 17:
                            tableRow = (TableRow) findViewById(R.id.TableRow18);
                            tableRow.addView(imageView[i][k]);
                            break;
                        case 18:
                            tableRow = (TableRow) findViewById(R.id.TableRow19);
                            tableRow.addView(imageView[i][k]);
                            break;
                        case 19:
                            tableRow = (TableRow) findViewById(R.id.TableRow20);
                            tableRow.addView(imageView[i][k]);
                            break;
                        case 20:
                            tableRow = (TableRow) findViewById(R.id.TableRow21);
                            tableRow.addView(imageView[i][k]);
                            break;
                        case 21:
                            tableRow = (TableRow) findViewById(R.id.TableRow22);
                            tableRow.addView(imageView[i][k]);
                            break;
                        case 22:
                            tableRow = (TableRow) findViewById(R.id.TableRow23);
                            tableRow.addView(imageView[i][k]);
                            break;
                        case 23:
                            tableRow = (TableRow) findViewById(R.id.TableRow24);
                            tableRow.addView(imageView[i][k]);
                            break;
                        case 24:
                            tableRow = (TableRow) findViewById(R.id.TableRow25);
                            tableRow.addView(imageView[i][k]);
                            break;
                        case 25:
                            tableRow = (TableRow) findViewById(R.id.TableRow26);
                            tableRow.addView(imageView[i][k]);
                            break;
                        case 26:
                            tableRow = (TableRow) findViewById(R.id.TableRow27);
                            tableRow.addView(imageView[i][k]);
                            break;
                        case 27:
                            tableRow = (TableRow) findViewById(R.id.TableRow28);
                            tableRow.addView(imageView[i][k]);
                            break;
                        case 28:
                            tableRow = (TableRow) findViewById(R.id.TableRow29);
                            tableRow.addView(imageView[i][k]);
                            break;
                        case 29:
                            tableRow = (TableRow) findViewById(R.id.TableRow30);
                            tableRow.addView(imageView[i][k]);
                            break;
                        default:
                            break;
                    }

                    }
                }

            state[1][1] = 3;
            state[1][2] = 1;
            state[1][3] = 2;

            snake_state[0][0] = 3;
            snake_state[0][1] = 1;
            snake_state[1][0] = 2;
            snake_state[1][1] = 1;
            snake_state[2][0] = 1;
            snake_state[2][1] = 1;

            apple_state[0][0] = 4;//random.nextInt(5) - 1;
            apple_state[0][1] = 4;//random.nextInt(5) - 1;
        }

        apple_initialization();
        performStateCalculation();
        performStateReformation();
        //mainTextView.setText(performStateReformation());

    } // default setting

    void performStateCalculation() {
        for (int i = 0; i < FIELD; i++) {
            for (int k = 0; k < FIELD; k++) {
                state[i][k] = 0;
            }
        }

        state[apple_state[0][1]][apple_state[0][0]] = 5;
        state[snake_state[0][1]][snake_state[0][0]] = 2;
        state[snake_state[snake_size - 1][1]][snake_state[snake_size - 1][0]] = 3;

        for (int i = 1; i < snake_size - 1; i++) {
            if (snake_state[i][0] > -1) {
                state[snake_state[i][1]][snake_state[i][0]] = 1;
            }
        }

        //state[0][0] = snake_state[0][0];
        //state[0][1] = snake_state[0][1];
        //state[0][0] = MainMenuActivity.screenHeight;
        //state[0][1] = MainMenuActivity.screenWidth;
        //state[0][2] = state[snake_state[0][1]][snake_state[0][0]];
    } // calculate state

    void Recalculate() {
        state[apple_state[0][1]][apple_state[0][0]] = 5;
    } // apple recalculation

    void performStateReformation() { //ss
        String ss = "";
        if(!gameOver) {
            for (int i = 0; i < FIELD; i++) { // i = y-coordinate
                for (int k = 0; k < FIELD; k++) { // k = x-coordinate // (x, y) = (k, i)
                    int snake_match = 0;
                    if(state[i][k] == 5){ss += " " + "\u2B24";
                    imageView[i][k].setImageBitmap(Bitmap.createScaledBitmap(bitmapApple, (int)(screenWidth/FIELD), (int)(screenWidth/FIELD), false));}
                    else if(state[i][k] == 3){ss += " " + "\u25EF";
                        for (int j = 1; j < snake_size; j++) {
                            if (snake_state[j][0] == k && snake_state[j][1] == i) {
                                snake_match = j;
                            }
                        }

                        /*fromL*/
                        if ((snake_state[snake_match - 1][0] != 0 && snake_state[snake_match - 1][0] == snake_state[snake_match][0] + 1) ||
                                (snake_state[snake_match - 1][0] == 0 && snake_state[snake_match][0] == FIELD - 1))
                        {
                            imageView[i][k].setImageBitmap(Bitmap.createScaledBitmap(bitmapTail[1], (int)(screenWidth/FIELD), (int)(screenWidth/FIELD), false));
                        }
                        /*fromR*/
                        else if ((snake_state[snake_match - 1][0] != FIELD - 1 && snake_state[snake_match - 1][0] == snake_state[snake_match][0] - 1) ||
                                (snake_state[snake_match - 1][0] == FIELD - 1 && snake_state[snake_match][0] == 0))
                        {
                            imageView[i][k].setImageBitmap(Bitmap.createScaledBitmap(bitmapTail[3], (int)(screenWidth/FIELD), (int)(screenWidth/FIELD), false));
                        }
                        /*fromU*/
                        else if ((snake_state[snake_match - 1][1] != 0 && snake_state[snake_match - 1][1] == snake_state[snake_match][1] + 1) ||
                                (snake_state[snake_match - 1][1] == 0 && snake_state[snake_match][1] == FIELD - 1))
                        {
                            imageView[i][k].setImageBitmap(Bitmap.createScaledBitmap(bitmapTail[2], (int)(screenWidth/FIELD), (int)(screenWidth/FIELD), false));
                        }
                        /*fromD*/
                        else if ((snake_state[snake_match - 1][1] != FIELD - 1 && snake_state[snake_match - 1][1] == snake_state[snake_match][1] - 1) ||
                                (snake_state[snake_match - 1][1] == FIELD - 1 && snake_state[snake_match][1] == 0))
                        {
                            imageView[i][k].setImageBitmap(Bitmap.createScaledBitmap(bitmapTail[0], (int)(screenWidth/FIELD), (int)(screenWidth/FIELD), false));
                        }
                    }
                    else if(state[i][k] == 2){ss += " " + "\u25EF";
                        for (int j = 1; j < snake_size; j++) {
                            if (snake_state[j][0] == k && snake_state[j][1] == i) {
                                snake_match = j;
                            }
                        }

                        /*toL*/
                        if ((snake_state[snake_match + 1][0] != FIELD - 1 && snake_state[snake_match + 1][0] == snake_state[snake_match][0] - 1) ||
                                        (snake_state[snake_match + 1][0] == FIELD - 1 && snake_state[snake_match][0] == 0))
                        {
                            imageView[i][k].setImageBitmap(Bitmap.createScaledBitmap(bitmapHead[3], (int)(screenWidth/FIELD), (int)(screenWidth/FIELD), false));
                        }
                        /*toR*/
                        else if ((snake_state[snake_match + 1][0] != 0 && snake_state[snake_match + 1][0] == snake_state[snake_match][0] + 1) ||
                                (snake_state[snake_match + 1][0] == 0 && snake_state[snake_match][0] == FIELD - 1))
                        {
                            imageView[i][k].setImageBitmap(Bitmap.createScaledBitmap(bitmapHead[1], (int)(screenWidth/FIELD), (int)(screenWidth/FIELD), false));
                        }
                        /*toU*/
                        else if ((snake_state[snake_match + 1][1] != FIELD - 1 && snake_state[snake_match + 1][1] == snake_state[snake_match][1] - 1) ||
                                (snake_state[snake_match + 1][1] == FIELD - 1 && snake_state[snake_match][1] == 0))
                        {
                            imageView[i][k].setImageBitmap(Bitmap.createScaledBitmap(bitmapHead[0], (int)(screenWidth/FIELD), (int)(screenWidth/FIELD), false));
                        }
                        /*toD*/
                        else if ((snake_state[snake_match + 1][1] != 0 && snake_state[snake_match + 1][1] == snake_state[snake_match][1] + 1) ||
                                (snake_state[snake_match + 1][1] == 0 && snake_state[snake_match][1] == FIELD - 1))
                        {
                            imageView[i][k].setImageBitmap(Bitmap.createScaledBitmap(bitmapHead[2], (int)(screenWidth/FIELD), (int)(screenWidth/FIELD), false));
                        }
                    }
                    else if(state[i][k] == 0){ss += " " + "__";
                        imageView[i][k].setImageBitmap(Bitmap.createScaledBitmap(bitmapEmpty, (int)(screenWidth/FIELD), (int)(screenWidth/FIELD), false));}
                    else if(state[i][k] == 1) {

                        for (int j = 1; j < snake_size; j++) {
                            if (snake_state[j][0] == k && snake_state[j][1] == i) {
                                snake_match = j;
                            }
                        }

                        /*LR*/
                        if (
                                ((snake_state[snake_match + 1][0] != FIELD - 1 && snake_state[snake_match + 1][0] == snake_state[snake_match][0] - 1) ||
                                        (snake_state[snake_match + 1][0] == FIELD - 1 && snake_state[snake_match][0] == 0))
                                        &&
                                        ((snake_state[snake_match - 1][0] != 0 && snake_state[snake_match - 1][0] == snake_state[snake_match][0] + 1) ||
                                                (snake_state[snake_match - 1][0] == 0 && snake_state[snake_match][0] == FIELD - 1))
                        ) {
                            ss += " " + "\u21E2";
                            imageView[i][k].setImageBitmap(Bitmap.createScaledBitmap(bitmapStraight[3], (int)(screenWidth/FIELD), (int)(screenWidth/FIELD), false));
                        }
                        /*RL*/
                        else if (
                                ((snake_state[snake_match + 1][0] != 0 && snake_state[snake_match + 1][0] == snake_state[snake_match][0] + 1) ||
                                        (snake_state[snake_match + 1][0] == 0 && snake_state[snake_match][0] == FIELD - 1))
                                        &&
                                        ((snake_state[snake_match - 1][0] != FIELD - 1 && snake_state[snake_match - 1][0] == snake_state[snake_match][0] - 1) ||
                                                (snake_state[snake_match - 1][0] == FIELD - 1 && snake_state[snake_match][0] == 0))
                        ) {
                            ss += " " + "\u21E0";
                            imageView[i][k].setImageBitmap(Bitmap.createScaledBitmap(bitmapStraight[1], (int)(screenWidth/FIELD), (int)(screenWidth/FIELD), false));
                        }
                        /*UD*/
                        else if (
                                ((snake_state[snake_match + 1][1] != FIELD - 1 && snake_state[snake_match + 1][1] == snake_state[snake_match][1] - 1) ||
                                        (snake_state[snake_match + 1][1] == FIELD - 1 && snake_state[snake_match][1] == 0))
                                        &&
                                        ((snake_state[snake_match - 1][1] != 0 && snake_state[snake_match - 1][1] == snake_state[snake_match][1] + 1) ||
                                                (snake_state[snake_match - 1][1] == 0 && snake_state[snake_match][1] == FIELD - 1))
                        ) {
                            ss += " " + "\u21E3";
                            imageView[i][k].setImageBitmap(Bitmap.createScaledBitmap(bitmapStraight[0], (int)(screenWidth/FIELD), (int)(screenWidth/FIELD), false));
                        }
                        /*DU*/
                        else if (
                                ((snake_state[snake_match + 1][1] != 0 && snake_state[snake_match + 1][1] == snake_state[snake_match][1] + 1) ||
                                        (snake_state[snake_match + 1][1] == 0 && snake_state[snake_match][1] == FIELD - 1))
                                        &&
                                        ((snake_state[snake_match - 1][1] != FIELD - 1 && snake_state[snake_match - 1][1] == snake_state[snake_match][1] - 1) ||
                                                (snake_state[snake_match - 1][1] == FIELD - 1 && snake_state[snake_match][1] == 0))
                        ) {
                            ss += " " + "\u21E1";
                            imageView[i][k].setImageBitmap(Bitmap.createScaledBitmap(bitmapStraight[2], (int)(screenWidth/FIELD), (int)(screenWidth/FIELD), false));
                        }
                        /*UR*/
                        else if (
                                ((snake_state[snake_match + 1][1] != FIELD - 1 && snake_state[snake_match + 1][1] == snake_state[snake_match][1] - 1) ||
                                        (snake_state[snake_match + 1][1] == FIELD - 1 && snake_state[snake_match][1] == 0))
                                        &&
                                        ((snake_state[snake_match - 1][0] != 0 && snake_state[snake_match - 1][0] == snake_state[snake_match][0] + 1) ||
                                                (snake_state[snake_match - 1][0] == 0 && snake_state[snake_match][0] == FIELD - 1))
                        ) {
                            ss += " " + "\u21B3";
                            imageView[i][k].setImageBitmap(Bitmap.createScaledBitmap(bitmapRound[0], (int)(screenWidth/FIELD), (int)(screenWidth/FIELD), false));
                        }
                        /*UL*/
                        else if (
                                ((snake_state[snake_match + 1][1] != FIELD - 1 && snake_state[snake_match + 1][1] == snake_state[snake_match][1] - 1) ||
                                        (snake_state[snake_match + 1][1] == FIELD - 1 && snake_state[snake_match][1] == 0))
                                        &&
                                        ((snake_state[snake_match - 1][0] != FIELD - 1 && snake_state[snake_match - 1][0] == snake_state[snake_match][0] - 1) ||
                                                (snake_state[snake_match - 1][0] == FIELD - 1 && snake_state[snake_match][0] == 0))
                        ) {
                            ss += " " + "\u21B5";
                            imageView[i][k].setImageBitmap(Bitmap.createScaledBitmap(bitmapRound[7], (int)(screenWidth/FIELD), (int)(screenWidth/FIELD), false));
                        }
                        /*DL*/
                        else if (
                                ((snake_state[snake_match + 1][1] != 0 && snake_state[snake_match + 1][1] == snake_state[snake_match][1] + 1) ||
                                        (snake_state[snake_match + 1][1] == 0 && snake_state[snake_match][1] == FIELD - 1))
                                        &&
                                        ((snake_state[snake_match - 1][0] != FIELD - 1 && snake_state[snake_match - 1][0] == snake_state[snake_match][0] - 1) ||
                                                (snake_state[snake_match - 1][0] == FIELD - 1 && snake_state[snake_match][0] == 0))
                        ) {
                            ss += " " + "\u21B0";
                            imageView[i][k].setImageBitmap(Bitmap.createScaledBitmap(bitmapRound[2], (int)(screenWidth/FIELD), (int)(screenWidth/FIELD), false));
                        }
                        /*DR*/
                        else if (
                                ((snake_state[snake_match + 1][1] != 0 && snake_state[snake_match + 1][1] == snake_state[snake_match][1] + 1) ||
                                        (snake_state[snake_match + 1][1] == 0 && snake_state[snake_match][1] == FIELD - 1))
                                        &&
                                        ((snake_state[snake_match - 1][0] != 0 && snake_state[snake_match - 1][0] == snake_state[snake_match][0] + 1) ||
                                                (snake_state[snake_match - 1][0] == 0 && snake_state[snake_match][0] == FIELD - 1))
                        ) {
                            ss += " " + "\u21B1";
                            imageView[i][k].setImageBitmap(Bitmap.createScaledBitmap(bitmapRound[5], (int)(screenWidth/FIELD), (int)(screenWidth/FIELD), false));
                        }
                        /*LU*/
                        else if (
                                ((snake_state[snake_match + 1][0] != FIELD - 1 && snake_state[snake_match + 1][0] == snake_state[snake_match][0] - 1) ||
                                        (snake_state[snake_match + 1][0] == FIELD - 1 && snake_state[snake_match][0] == 0))
                                        &&
                                        ((snake_state[snake_match - 1][1] != FIELD - 1 && snake_state[snake_match - 1][1] == snake_state[snake_match][1] - 1) ||
                                                (snake_state[snake_match - 1][1] == FIELD - 1 && snake_state[snake_match][1] == 0))
                        ) {
                            ss += " " + "\u2B0F";
                            imageView[i][k].setImageBitmap(Bitmap.createScaledBitmap(bitmapRound[3], (int)(screenWidth/FIELD), (int)(screenWidth/FIELD), false));
                        }
                        /*LD*/
                        else if (
                                ((snake_state[snake_match + 1][0] != FIELD - 1 && snake_state[snake_match + 1][0] == snake_state[snake_match][0] - 1) ||
                                        (snake_state[snake_match + 1][0] == FIELD - 1 && snake_state[snake_match][0] == 0))
                                        &&
                                        ((snake_state[snake_match - 1][1] != 0 && snake_state[snake_match - 1][1] == snake_state[snake_match][1] + 1) ||
                                                (snake_state[snake_match - 1][1] == 0 && snake_state[snake_match][1] == FIELD - 1))
                        ) {
                            ss += " " + "\u2B0E";
                            imageView[i][k].setImageBitmap(Bitmap.createScaledBitmap(bitmapRound[6], (int)(screenWidth/FIELD), (int)(screenWidth/FIELD), false));
                        }
                        /*RU*/
                        else if (
                                ((snake_state[snake_match + 1][0] != 0 && snake_state[snake_match + 1][0] == snake_state[snake_match][0] + 1) ||
                                        (snake_state[snake_match + 1][0] == 0 && snake_state[snake_match][0] == FIELD - 1))
                                        &&
                                        ((snake_state[snake_match - 1][1] != FIELD - 1 && snake_state[snake_match - 1][1] == snake_state[snake_match][1] - 1) ||
                                                (snake_state[snake_match - 1][1] == FIELD - 1 && snake_state[snake_match][1] == 0))
                        ) {
                            ss += " " + "\u2B11";
                            imageView[i][k].setImageBitmap(Bitmap.createScaledBitmap(bitmapRound[4], (int)(screenWidth/FIELD), (int)(screenWidth/FIELD), false));
                        }
                        /*RD*/
                        else if (
                                ((snake_state[snake_match + 1][0] != 0 && snake_state[snake_match + 1][0] == snake_state[snake_match][0] + 1) ||
                                        (snake_state[snake_match + 1][0] == 0 && snake_state[snake_match][0] == FIELD - 1))
                                        &&
                                        ((snake_state[snake_match - 1][1] != 0 && snake_state[snake_match - 1][1] == snake_state[snake_match][1] + 1) ||
                                                (snake_state[snake_match - 1][1] == 0 && snake_state[snake_match][1] == FIELD - 1))
                        ) {
                            ss += " " + "\u2B10";
                            imageView[i][k].setImageBitmap(Bitmap.createScaledBitmap(bitmapRound[1], (int)(screenWidth/FIELD), (int)(screenWidth/FIELD), false));
                        }
                    }
                    else {ss += " " + state[i][k];}
                    if (k == FIELD - 1 && i != FIELD - 1) {
                        ss += " \n";
                    }
                }
            }
        }
        TextView textViewScore2 = (TextView) findViewById(R.id.textViewScore2);
        textViewScore2.setText(Integer.toString(snake_size - 3));
        //return ss;
    } //ss

    private void performActions(int startX, int startY, int endX, int endY, int dX, int dY) {
        if(dX >= dY && dX >= MIN_SWIPE_DISTANCE && startX >= endX){onLeftSwipe();}
        if(dX >= dY && dX >= MIN_SWIPE_DISTANCE && startX <= endX){onRightSwipe();}
        if(dX < dY && dY >= MIN_SWIPE_DISTANCE && startY >= endY){onUpSwipe();}
        if(dX < dY && dY >= MIN_SWIPE_DISTANCE && startY <= endY){onDownSwipe();}
    } // swipe recognition

    void onLeftSwipe () {
        if(generationMove != "right") {direction = "left";}

    }

    void onRightSwipe () {
        if(generationMove != "left") {direction = "right";}
    }

    void onUpSwipe () {
        if(generationMove != "down") {direction = "up";}
    }

    void onDownSwipe () {
        if(generationMove != "up") {direction = "down";}
    }

    void apple_initialization() {
        apple_state[0][0] = random.nextInt(FIELD); apple_state[0][1] = random.nextInt(FIELD);
        if(state[apple_state[0][1]][apple_state[0][0]] == 1 || state[apple_state[0][1]][apple_state[0][0]] == 2 || state[apple_state[0][1]][apple_state[0][0]] == 3)
        {apple_initialization();}
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SnakeActivity.this, SelectionMenuActivity.class));
    }

}




