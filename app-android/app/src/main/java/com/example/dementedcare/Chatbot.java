package com.example.dementedcare;

import static com.android.volley.toolbox.Volley.newRequestQueue;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Chatbot extends AppCompatActivity {

    private RecyclerView chatRV;
    private final String BOT_KEY = "bot";
    private final String USER_KEY = "user";
    private ArrayList<ChatModel> chatsModelArrayList;
    private ChatRVAdapter chatRVAdapter;

    private EditText userMsgEdt;
    private ImageButton sendMsgFAB;

    //Progress bar
    ProgressDialog pd;

    private String resmsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        chatRV = findViewById(R.id.chatRV);
        userMsgEdt = findViewById(R.id.idEditMessage);
        sendMsgFAB = findViewById(R.id.idFABSend);

        chatsModelArrayList = new ArrayList<>();
        chatRVAdapter = new ChatRVAdapter(chatsModelArrayList, this);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        chatRV.setLayoutManager(manager);
        chatRV.setAdapter(chatRVAdapter);

        pd = new ProgressDialog(Chatbot.this);

        sendMsgFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = userMsgEdt.getText().toString().trim();
                if (message.isEmpty()) {
                    Toast.makeText(Chatbot.this, "Please enter your message", Toast.LENGTH_SHORT).show();
                } else {
                    postrequest(message);
//                    getResponse(message);
                    userMsgEdt.setText("");
                }
            }
        });
    }

    // function for http post request by using volley
    private void postrequest(String ReqValue) {

        //Set title of progress bar
        pd.setTitle("Sending msg...");
        pd.show();

        chatsModelArrayList.add(new ChatModel(ReqValue, USER_KEY));
        chatRVAdapter.notifyDataSetChanged();

        String url = "https://noted-mink-legally.ngrok-free.app/answer";

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
                    pd.dismiss();
                    resmsg = response.getString("answer");
                    System.out.println(resmsg);
                    Toast.makeText(getApplicationContext(), resmsg, Toast.LENGTH_SHORT).show();

                    chatsModelArrayList.add(new ChatModel(resmsg, BOT_KEY));
                    chatRVAdapter.notifyDataSetChanged();


                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
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
                    jsonObject.put("query", ReqValue);
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