package com.example.bangpt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class UploadStatusActivity extends AppCompatActivity {

    private TextView statusTextView;
    private Button uploadStatusButton;
    String userid;
    private String uploadUrl = "http://172.20.10.8:821/model/model"; // 모델 엔드포인트 URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_status);
        loadingpageActivity loadingDialog = new loadingpageActivity(UploadStatusActivity.this);

        statusTextView = findViewById(R.id.status_textview);
        uploadStatusButton = findViewById(R.id.upload_status_button);

        String userID = getIntent().getStringExtra("userID"); // 추가

        SharedPreferences settings = getSharedPreferences("Login", 0);
        userid = settings.getString("userID", "");


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String statusMessage = extras.getString("status_message");
            statusTextView.setText(statusMessage);
        }

        // 업로드 상태 버튼 클릭 이벤트 처리
        uploadStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 모델 엔드포인트 호출
                requestModelEndpoint();
                loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                loadingDialog.setCancelable(false);
                loadingDialog.show();
            }
        });
    }

    private void requestModelEndpoint() {
        // Volley 라이브러리를 사용하여 POST 요청 전송
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        int timeout = 60000; // 1분(60초)으로 타임아웃 값을 설정
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uploadUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 모델 엔드포인트 요청에 대한 응답 처리
                        // 응답을 받았을 때 수행할 동작을 여기에 작성하세요.
                        Log.d("Model", "Model uploaded successfully");
                        // 화면 전환
                        Intent intent = new Intent(UploadStatusActivity.this, ExerciseResult2Activity.class);
                        startActivity(intent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 모델 엔드포인트 요청에 대한 에러 처리
                        // 에러 발생 시 수행할 동작을 여기에 작성하세요.
                        Log.d("Model", "Model upload failed: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // 요청에 필요한 매개변수가 있을 경우 설정
                Map<String, String> params = new HashMap<>();
                // 필요한 매개변수를 추가하세요.
                params.put("user_id", userid);
                return params;
            }

            @Override
            public RetryPolicy getRetryPolicy() {
                // 요청의 타임아웃 값을 변경
                return new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            }
        };
        requestQueue.add(stringRequest);
    }
}
