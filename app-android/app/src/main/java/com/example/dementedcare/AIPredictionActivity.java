package com.example.dementedcare;



import static com.android.volley.toolbox.Volley.newRequestQueue;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class AIPredictionActivity extends AppCompatActivity {

    private Button aiPredictionButton;

    private String prediction;
    private double probabilityDouble;

    ProgressDialog pd;

    private int age,sex,cp,trtbps,chol,fbs,restecg,thalachh,exng,oldpeak,slp,caa,thall,probability1;

    TextView heartpredictionresultTV,result;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aiprediction);
        aiPredictionButton = findViewById(R.id.btn);
        heartpredictionresultTV = findViewById(R.id.heartpredictionresult);
        result = findViewById(R.id.result);


        pd = new ProgressDialog(AIPredictionActivity.this);

        Intent intent = getIntent();
        age = intent.getIntExtra("age",0);
        sex = intent.getIntExtra("sex",0);
        cp = intent.getIntExtra("cp",0);
        trtbps = intent.getIntExtra("trtbps",0);
        chol = intent.getIntExtra("chol",0);
        fbs = intent.getIntExtra("fbs",0);
        restecg = intent.getIntExtra("restecg",0);
        thalachh = intent.getIntExtra("thalachh",0);
        exng = intent.getIntExtra("exng",0);
        oldpeak = intent.getIntExtra("oldpeak",0);
        slp = intent.getIntExtra("slp",0);
        caa = intent.getIntExtra("caa",0);
        thall = intent.getIntExtra("thall",0);

        postrequest();

        // Add ValueEventListener to listen for data changes
        aiPredictionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to open AIPredictionActivity
                Intent intent = new Intent(AIPredictionActivity.this, PatientHealthDetails.class);
                startActivity(intent);
            }
        });
    }

    private void postrequest() {

        //Set title of progress bar
        pd.setTitle("Getting Predictions...");
        pd.show();

        String url = "https://asia-south1-dementedcare.cloudfunctions.net/heartdisease";

        RequestQueue queue = newRequestQueue(this);

        // Set a custom retry policy with a longer timeout
        int timeoutMillis = 300000; // 300 seconds
        RetryPolicy retryPolicy = new DefaultRetryPolicy(
                timeoutMillis,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        );

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    pd.dismiss();
                    prediction = response.getString("prediction");
                    probabilityDouble = response.getDouble("probability");
                    probability1 = (int) probabilityDouble * 100;
                    Toast.makeText(getApplicationContext(), prediction, Toast.LENGTH_SHORT).show();
                    heartpredictionresultTV.setText(prediction);
                    result.setText(probability1+"%");
//                    System.out.println(resmsg);
//                    Toast.makeText(getApplicationContext(), resmsg, Toast.LENGTH_SHORT).show();
//                    result.setText("Patient emotion is : "+resmsg);


                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                prediction = "sorry network error occors. ";

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
                    jsonObject.put("age",age);
                    jsonObject.put("sex", sex);
                    jsonObject.put("cp", cp);
                    jsonObject.put("trtbps", trtbps);
                    jsonObject.put("chol", chol);
                    jsonObject.put("fbs", fbs);
                    jsonObject.put("restecg", restecg);
                    jsonObject.put("thalachh", thalachh);
                    jsonObject.put("exng", exng);
                    jsonObject.put("oldpeak", oldpeak);
                    jsonObject.put("slp", slp);
                    jsonObject.put("caa", caa);
                    jsonObject.put("thall", thall);

                    String requestBody = jsonObject.toString();
                    return requestBody.getBytes("utf-8");


                } catch (JSONException | UnsupportedEncodingException uee) {

                    pd.dismiss();
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