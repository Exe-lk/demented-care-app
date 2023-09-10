package com.example.dementedcare;

import static com.android.volley.toolbox.Volley.newRequestQueue;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class face_results extends AppCompatActivity {
    private Button button;
    private TextView result;

    private String resmsg,userId;

    //Progress bar
    ProgressDialog pd;

    private FirebaseAuth firebaseAuth; // Firebase Authentication instance
    private FirebaseUser currentUser; // Firebase User


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_results);

        button = findViewById(R.id.btn);
        result = findViewById(R.id.result);

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser(); // Get the current user

        if (currentUser != null) {
            // Get the user's unique ID
            userId = currentUser.getUid();
        }

        pd = new ProgressDialog(face_results.this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the new activity to open
                Intent intent = new Intent(face_results.this, Videorecorderactivity.class); // Replace NewActivity.class with the actual activity you want to open

                // Start the new activity
                startActivity(intent);
            }
        });

        postrequest(userId);


    }

//    private void GetPredictionResult(String ReqValue){
//        postrequest(ReqValue);
//    }

    // function for http post request by using volley
    private void postrequest(String ReqValue) {

        //Set title of progress bar
        pd.setTitle("Getting Predictions...");
        pd.show();

        String url = "https://asia-south1-dementedcare.cloudfunctions.net/faceemotion";

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
                    resmsg = response.getString("response");
                    System.out.println(resmsg);
                    Toast.makeText(getApplicationContext(), resmsg, Toast.LENGTH_SHORT).show();
                    result.setText("Patient emotion is : "+resmsg);


                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                resmsg = "sorry network error occors. ";

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
                    jsonObject.put("id", ReqValue);
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
