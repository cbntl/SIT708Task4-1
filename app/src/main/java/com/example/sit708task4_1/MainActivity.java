package com.example.sit708task4_1;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView countDownTimer, activityText;
    EditText inputWorkoutDuration, inputRestDuration, inputRounds;
    ImageView imageIcon;
    ProgressBar progressBar;
    Button startButton, stopButton;
    CountDownTimer workoutTimer, restTimer;

    boolean workoutCountDown = false, restCountDown = false;


    int i = 0;

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageIcon = findViewById(R.id.imageView);
        activityText = findViewById(R.id.activityTextView);
        countDownTimer = findViewById(R.id.countDownTime);
        inputWorkoutDuration = findViewById(R.id.inputWorkoutDuration);
        inputRestDuration = findViewById(R.id.inputRestDuration);
        inputRounds = findViewById(R.id.inputRounds);
        progressBar = findViewById(R.id.progressBar);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);



        startButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                MediaPlayer.create(MainActivity.this, R.raw.beep).start();

                long workoutDuration = Long.parseLong(inputWorkoutDuration.getText().toString()) * 1000;
                long restDuration = Long.parseLong(inputRestDuration.getText().toString()) * 1000;
                long rounds = Long.parseLong(inputRounds.getText().toString());


                workoutTimer = new CountDownTimer(workoutDuration + 25, 1000) {
                    @Override
                    public void onTick(long mSecondsLeft) {
                        long minutes = (mSecondsLeft / 60000) % 60;
                        long seconds = (mSecondsLeft / 1000) % 60;


                        startButton.setClickable(false);
                        startButton.setBackgroundColor(Color.GRAY);
                        startButton.setTextColor(Color.BLACK);
                        startButton.setText("START");
                        stopButton.setClickable(true);
                        stopButton.setBackgroundColor(Color.parseColor("#A52A2A"));
                        stopButton.setTextColor(Color.WHITE);


                        workoutCountDown = true;


                        imageIcon.setImageResource(R.drawable.workout);
                        activityText.setText("WORKOUT");

                        countDownTimer.setText(String.format("%02d:%02d", minutes, seconds));


                        int progress = (int) (100 - (mSecondsLeft * 100) / (workoutDuration));
                        progressBar.setProgress(progress);


                    }

                    @Override
                    public void onFinish() {

                        workoutCountDown = false;
                        MediaPlayer.create(MainActivity.this, R.raw.beep).start();
                        imageIcon.setImageResource(R.drawable.rest);
                        activityText.setText("REST");


                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        intent.setAction(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);



                        restTimer = new CountDownTimer(restDuration + 50, 1000) {
                            @Override
                            public void onTick(long mSecLeft) {
                                long minutes = (mSecLeft/60000)%60;
                                long seconds = (mSecLeft/1000)%60;

                                restCountDown = true;

                                countDownTimer.setText(String.format("%02d:%02d", minutes, seconds));

                                int progress = (int)(100-(mSecLeft*100)/(restDuration));
                                progressBar.setProgress(progress);

                            }

                            @Override
                            public void onFinish() {

                                i++;
                                MediaPlayer.create(MainActivity.this, R.raw.beep).start();
                                if (i < rounds) {
                                    workoutTimer.start();

                                } else {

                                    i = 0;
                                    restCountDown = false;
                                    startButton.setClickable(true);
                                    startButton.setBackgroundColor(Color.parseColor("#228B22"));
                                    startButton.setTextColor(Color.WHITE);
                                }
                            }
                        };
                        restTimer.start();
                    }
                };
                workoutTimer.start();
            }

        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(workoutCountDown == true) {
                    workoutTimer.cancel();
                    workoutCountDown = false;

                } else if (restCountDown == true) {
                    restTimer.cancel();
                    restCountDown = false;
                }
                startButton.setClickable(true);
                startButton.setBackgroundColor(Color.parseColor("#228B22"));
                startButton.setTextColor(Color.WHITE);
                if(progressBar.getProgress() > 0 ) {
                    startButton.setText("RESTART");
                }
                stopButton.setBackgroundColor(Color.GRAY);
                stopButton.setTextColor(Color.BLACK);
                stopButton.setClickable(false);
            }
        });


    }




}