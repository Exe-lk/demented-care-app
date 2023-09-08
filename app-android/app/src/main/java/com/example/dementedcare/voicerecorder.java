package com.example.dementedcare;

import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import java.io.IOException;
import com.airbnb.lottie.LottieAnimationView;

import android.os.CountDownTimer;
import android.widget.TextView;

public class voicerecorder extends AppCompatActivity {

    private Button recordButton, playButton;
    private MediaRecorder mediaRecorder;
    private String audioFilePath;
    private boolean isRecording = false;
    private MediaPlayer mediaPlayer;
    private LottieAnimationView lottieAnimationView;
    private Button playPauseButton;
    private TextView tvTime;
    private CountDownTimer countDownTimer;
    private long elapsedTimeMillis = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voicerecorder);

        lottieAnimationView = findViewById(R.id.lav_playing);
        playPauseButton = findViewById(R.id.playPauseButton);
        tvTime = findViewById(R.id.tv_time);

        // Find the LottieAnimationView by its ID
        lottieAnimationView = findViewById(R.id.lav_playing);

        // Set the animation resource to the LottieAnimationView
        lottieAnimationView.setAnimation(R.raw.sound);

        // Start the animation
        lottieAnimationView.playAnimation();

        lottieAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countDownTimer == null) {
                    // Start the timer when Lottie button is clicked
                    startTimer();
                } else {
                    // Stop the timer when Lottie button is clicked again
                    stopTimer();
                }
            }
        });
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(Long.MAX_VALUE, 1000) { // Update every 1 second
            @Override
            public void onTick(long millisUntilFinished) {
                elapsedTimeMillis += 1000; // Increment elapsed time by 1 second
                updateTimerText();
            }

            @Override
            public void onFinish() {
                // Timer finished (you can add any necessary logic here)
            }
        }.start();
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    private void updateTimerText() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                long seconds = elapsedTimeMillis / 1000;
                long minutes = seconds / 60;
                seconds %= 60;

                String timeString = String.format("%02d:%02d", minutes, seconds);
                tvTime.setText(timeString);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer(); // Stop the timer when the activity is destroyed
    }
    public void playPauseAnimation(View view) {
        // Add your logic here to play or pause the Lottie animation
    }

}
