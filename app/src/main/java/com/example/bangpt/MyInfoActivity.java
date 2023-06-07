package com.example.bangpt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyInfoActivity extends AppCompatActivity {
    private TextView tv_id, tv_pass, tv_name, tv_member, tv_date;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        String userID = getIntent().getStringExtra("userID");

        tv_id = findViewById(R.id.tv_id);
        tv_pass = findViewById(R.id.tv_pass);
        tv_name = findViewById(R.id.tv_name);
        tv_member = findViewById(R.id.tv_member);
        tv_date = findViewById(R.id.tv_date);

        tv_id.setText(userID);

        myinfo();
    }
    private void myinfo() {
        SharedPreferences settings = getSharedPreferences("Login", 0);
        userid = settings.getString("userID", "");

        String serverUrl = "http://172.20.10.8:821/user/" + userid;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, serverUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String user_id = response.getString("user_id");
                            String password = response.getString("password");
                            String name = response.getString("name");
                            String nickname = response.getString("nickname");
                            String trainer = response.getString("trainer");

                            // Update TextViews with retrieved information
                            tv_id.setText(user_id);
                            tv_pass.setText(password);
                            tv_name.setText(name);
                            tv_member.setText(nickname);
                            tv_date.setText(trainer);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MyInfoActivity.this, "서버 응답을 처리하는 중에 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(MyInfoActivity.this, "서버와 통신하는 중에 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(MyInfoActivity.this);
        queue.add(jsonObjectRequest);
    }
}