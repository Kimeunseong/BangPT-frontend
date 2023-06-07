package com.example.bangpt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

//얘가 실질적 동영상 분석 후 바로 결과 보여주는 activity
public class ExerciseResult2Activity extends AppCompatActivity {

    private TextView tv_timestamp, tv_bestrep, tv_worsttrep, tv_total_reps, tv_reps1, tv_reps2, tv_reps3, tv_reps4, tv_reps5, tv_feedback;
    String userid;
    int num;
    private Button back;

//    private VideoView videoView;
    private WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_result2);

        webView = findViewById(R.id.videoView1);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://172.20.10.8:821/video/video_show1"); // 웹 페이지 주소를 여기에 입력.

        webView = findViewById(R.id.videoView2);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://172.20.10.8:821/video/video_show2"); // 웹 페이지 주소를 여기에 입력.

////        videoView = findViewById(R.id.videoView1);
//        // 비디오 파일의 경로를 가져옵니다.
//        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.best_video;
//        // 비디오뷰에 비디오 파일을 설정합니다.
//        videoView.setVideoURI(Uri.parse(videoPath));
//        // 미디어 컨트롤러를 생성하여 비디오뷰에 연결합니다.
//        MediaController mediaController = new MediaController(this);
//        videoView.setMediaController(mediaController);
//        // 비디오뷰를 포커스하고 재생합니다.
//        videoView.requestFocus();
//        videoView.start();
//
////        videoView = findViewById(R.id.videoView2);
//        // 비디오 파일의 경로를 가져옴.
//        String videoPath2 = "android.resource://" + getPackageName() + "/" + R.raw.worst_video;
//
//        // 비디오뷰에 비디오 파일을 설정합.
//        videoView.setVideoURI(Uri.parse(videoPath2));
//        // 미디어 컨트롤러를 생성하여 비디오뷰에 연결합.
//        videoView.setMediaController(mediaController);
//        // 비디오뷰를 포커스하고 재생.
//        videoView.requestFocus();
//        videoView.start();





        userid = getIntent().getStringExtra("userID");
        num = getIntent().getIntExtra("num", 0);

        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            //버튼을 누르면 메인 화면으로
            public void onClick(View view){
                Intent intent=new Intent(ExerciseResult2Activity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        tv_timestamp = findViewById(R.id.tv_timestamp);
        tv_bestrep = findViewById(R.id.tv_bestrep);
        tv_worsttrep = findViewById(R.id.tv_worsttrep);
        tv_total_reps = findViewById(R.id.tv_total_reps);
        tv_reps1 = findViewById(R.id.tv_reps1);
        tv_reps2 = findViewById(R.id.tv_reps2);
        tv_reps3 = findViewById(R.id.tv_reps3);
        tv_reps4 = findViewById(R.id.tv_reps4);
        tv_reps5 = findViewById(R.id.tv_reps5);
        tv_feedback = findViewById(R.id.tv_feedback);

        //tv_id.setText(userID);
        myresult();
    }

    private void myresult() {
        SharedPreferences settings = getSharedPreferences("Login", 0);
        userid = settings.getString("userID", "");

        String serverUrl = "http://172.20.10.8:821/exercise_result/latest/" + userid;

        JSONObject params = new JSONObject();
        try {
            params.put("user_id", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, serverUrl, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String timestamp = response.getString("timestamp");
                            int best_rep = response.getInt("best_reps");
                            int fbest_rep = best_rep + 1;
                            int worst_rep = response.getInt("worst_reps");
                            int fworst_rep = worst_rep + 1;
                            String feedback = response.getString("feedback");

                            double score_100 = response.getDouble("score_100_total");
                            String formattedScore = String.format("%.1f", score_100);
                            tv_total_reps.setText(formattedScore);

                            double reps1 = response.getDouble("Reps1");
                            String formattedReps1 = String.format("%.1f", reps1);
                            tv_reps1.setText(formattedReps1);

                            double reps2 = response.getDouble("Reps2");
                            String formattedReps2 = String.format("%.1f", reps2);
                            tv_reps2.setText(formattedReps2);

                            double reps3 = response.getDouble("Reps3");
                            String formattedReps3 = String.format("%.1f", reps3);
                            tv_reps3.setText(formattedReps3);

                            double reps4 = response.getDouble("Reps4");
                            String formattedReps4 = String.format("%.1f", reps4);
                            tv_reps4.setText(formattedReps4);

                            double reps5 = response.getDouble("Reps5");
                            String formattedReps5 = String.format("%.1f", reps5);
                            tv_reps5.setText(formattedReps5);

                            tv_timestamp.setText(timestamp);
                            tv_bestrep.setText(String.valueOf(fbest_rep));
                            tv_worsttrep.setText(String.valueOf(fworst_rep));
                            tv_feedback.setText(feedback);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ExerciseResult2Activity.this, "서버 응답을 처리하는 중에 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(ExerciseResult2Activity.this, "서버와 통신하는 중에 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(ExerciseResult2Activity.this);
        queue.add(jsonObjectRequest);
    }

}