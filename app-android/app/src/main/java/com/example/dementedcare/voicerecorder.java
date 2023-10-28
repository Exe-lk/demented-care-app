package com.example.dementedcare;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import static com.android.volley.toolbox.Volley.newRequestQueue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.dementedcare.model.Audio;

import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class voicerecorder extends AppCompatActivity {

    private Button recordButton, playButton;
    private MediaRecorder mediaRecorder;
    private String audioFilePath;
    private boolean isRecording = false;
    private MediaPlayer mediaPlayer;
    private LottieAnimationView lottieAnimationView;
    private Button playPauseButton,playAudioBtn,submitBtn;
    private TextView tvTime,responseStatus;
    private CountDownTimer countDownTimer;
    private long elapsedTimeMillis = 0;

    //player
    private MediaRecorder mRecorder;

    private MediaPlayer mPlayer;

    // string variable is created for storing a file name
    private static String mFileName = null;
    private String responseMsg;

    // constant for audio permission
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voicerecorder);

        lottieAnimationView = findViewById(R.id.lav_playing);
        playPauseButton = findViewById(R.id.playPauseButton);
        playAudioBtn=findViewById(R.id.playBtn);
        submitBtn=findViewById(R.id.submitBtn);
        tvTime = findViewById(R.id.tv_time);
        responseStatus = findViewById(R.id.responseStatusID);
        // Find the LottieAnimationView by its ID
        lottieAnimationView = findViewById(R.id.lav_playing);

        // Set the animation resource to the LottieAnimationView
        lottieAnimationView.setAnimation(R.raw.sound);

        // Start the animation
        lottieAnimationView.playAnimation();

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countDownTimer == null) {
                    // Start the timer when Lottie button is clicked
                    responseStatus.setText("");
                    startTimer();
                    startRecording();
                } else {
                    // Stop the timer when Lottie button is clicked again
                    stopTimer();
                    stopRecording();
                }
            }
        });

        playAudioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // play the recorded audio
                playAudio();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // play the recorded audio
                postrequest();
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

    private void resetTimer() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                long seconds = 0;
                long minutes = 0;
                seconds %= 60;
                elapsedTimeMillis=0;
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


    private void startRecording() {
        // check permission method is used to check
        // that the user has granted permission
        // to record and store the audio.
        if (CheckPermissions()) {


            //initializing filename variable
            // with the path of the recorded audio file.
            mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
            mFileName += "/abcde.ogg";


            //initializing media recorder class
            mRecorder = new MediaRecorder();

            //Sets the audio source to be used for recording.
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

            // set the output format of the audio.
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.OGG);

            // set the audio encoder for recorded audio.
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.OPUS);
            mRecorder.setAudioSamplingRate(24000);
            //set the output file location for recorded audio
            mRecorder.setOutputFile(mFileName);
            try {

                //Prepares the recorder to begin capturing and encoding data.
                mRecorder.prepare();

            } catch (IOException e) {

                Log.e("TAG", "prepare() failed");
            }

            // start the audio recording.
            mRecorder.start();

        } else {

            // if audio recording permissions are
            // not granted by user this method will
            // ask for runtime permission for mic and storage.
            RequestPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // this method is called when user will
        // grant the permission for audio recording.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean CheckPermissions() {
        // this method is used to check permission
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermissions() {
        // this method is used to request the
        // permission for audio recording and storage.
        ActivityCompat.requestPermissions(voicerecorder.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }

    //play the recorded audio
    public void playAudio() {
        resetTimer();

        Uri uriAudio = Uri.fromFile(new File(mFileName).getAbsoluteFile());
        Audio audio =new Audio();
        audio.setPath(uriAudio);

        CloudHandler cloudHandler=new CloudHandler();
        cloudHandler.SaveOnCloud(audio);

        // using media player class for playing recorded audio
        mPlayer = new MediaPlayer();
        try {
            // set the data source which will be our file name
            mPlayer.setDataSource(mFileName);

            //prepare media player
            mPlayer.prepare();

            // start media player.
            mPlayer.start();


        } catch (IOException e) {
            Log.e("TAG", "prepare() failed");
        }
    }

    public void stopRecording() {

        // stop the audio recording.
        mRecorder.stop();

        // release the media recorder object.
        mRecorder.release();
        mRecorder = null;


    }

    public void stopPlaying() {
        // release the media player object
        // and stop playing recorded audio.
        mPlayer.release();
        mPlayer = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        // releasing the media player and the media recorder object
        // and set it to null
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }

    }

    private void postrequest() {

        //Set title of progress bar




        String url = "https://us-central1-dementedcare.cloudfunctions.net/voiceai2";

        RequestQueue queue = newRequestQueue(this);

        // Set a custom retry policy with a longer timeout
        int timeoutMillis = 30000; // 30 seconds
        RetryPolicy retryPolicy = new DefaultRetryPolicy(
                timeoutMillis,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        );

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    responseMsg = response.getString("response");
                    responseStatus.setText(responseMsg);
                    Toast.makeText(getApplicationContext(), responseMsg, Toast.LENGTH_LONG).show();

                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                responseMsg = "sorry network error occors. ";

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", "abcde");
                    String requestBody = jsonObject.toString();
                    return requestBody.getBytes("utf-8");


                } catch (JSONException | UnsupportedEncodingException uee) {



                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");

                return params;
            }
        };
        // below line is to make

        // Set the custom retry policy for the request
        request.setRetryPolicy(retryPolicy);

        // a json object request.
        queue.add(request);

    }

}
