package com.example.bangpt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bangpt.Request.GetSplitVideoRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExerciseResultActivity extends AppCompatActivity {

    private TextView tv_timestamp, tv_bestrep, tv_worsttrep, tv_total_reps, tv_reps1, tv_reps2, tv_reps3, tv_reps4, tv_reps5, tv_feedback;
    String userid;
    int num;

    VideoView best_videoView, worst_videoView;
    private String best_url, worst_url, best_video_path, worst_video_path;
    private int best_video_num, worst_video_num;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_result2);
        int[] array = new int[2];

        userid = getIntent().getStringExtra("userID");
        num = getIntent().getIntExtra("num", 0);

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

        array = myresult();
        best_video_num = array[0];
        worst_video_num = array[1];

        webView = findViewById(R.id.videoView1);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://172.20.10.8:821/video/video_show1"); // 웹 페이지 주소를 여기에 입력.

        webView = findViewById(R.id.videoView2);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://172.20.10.8:821/video/video_show2"); // 웹 페이지 주소를 여기에 입력.

//        best_videoView = findViewById(R.id.videoView1);
//        worst_videoView = findViewById(R.id.videoView2);

        // 비디오 컨트롤 가능하게(일시정지, 재시작 등)
        MediaController mc1 = new MediaController(this);
        MediaController mc2 = new MediaController(this);


        best_videoView.setMediaController(mc1);
        worst_videoView.setMediaController(mc2);

        best_url = GetSplitVideoRequest.GetURL(best_video_num);
        worst_url = GetSplitVideoRequest.GetURL(worst_video_num);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET,best_url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {

                            //이건 sd카드
                            String fileName = "video" + best_video_num + ".mp4"; // 저장할 파일 이름

                            // 외부 저장소 경로 가져오기
                            File externalDir = Environment.getExternalStorageDirectory();
                            File outputFile = new File(externalDir, fileName);
                            FileOutputStream outputStream = new FileOutputStream(outputFile);
                            outputStream.write(response.data);
                            outputStream.close();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 에러 처리 코드를 작성합니다.
                        error.printStackTrace();
                    }
                }
        );
        VolleyMultipartRequest volleyMultipartRequest2 = new VolleyMultipartRequest(Request.Method.GET,worst_url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            // 에뮬레이터 내부 저장소에 동영상 파일을 저장합니다.
                            String fileName = "video"+worst_video_num +".mp4"; // 저장할 파일 이름
                            FileOutputStream outputStream2 = openFileOutput(fileName, Context.MODE_PRIVATE);
                            outputStream2.write(response.data);
                            outputStream2.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 에러 처리 코드를 작성합니다.
                        error.printStackTrace();
                    }
                }
        );
        requestQueue.add(volleyMultipartRequest);
        requestQueue.add(volleyMultipartRequest2);

        //sd카드
        String fileName = "video" + best_video_num + ".mp4"; // 저장할 파일 이름

        // 외부 저장소 경로 가져오기
        File externalDir = Environment.getExternalStorageDirectory();
        File outputFile = new File(externalDir, fileName);
        best_videoView.setVideoURI(Uri.fromFile(outputFile));
        worst_videoView.setVideoURI(Uri.fromFile(outputFile));
        best_videoView.start();
        worst_videoView.start();
    }

    private int[] myresult() {
        SharedPreferences settings = getSharedPreferences("Login", 0);
        userid = settings.getString("userID", "");
        int[]array = new int[2];

        String serverUrl = "http://172.20.10.8:82/exercise_result/latest/" + userid;

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
                            int worst_rep = response.getInt("worst_reps");
                            String feedback = response.getString("feedback");
                            array[0] = best_rep + 1;
                            array[1] = worst_rep + 1;

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
                            tv_bestrep.setText(String.valueOf(best_rep));
                            tv_worsttrep.setText(String.valueOf(worst_rep));
                            tv_feedback.setText(feedback);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ExerciseResultActivity.this, "서버 응답을 처리하는 중에 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(ExerciseResultActivity.this, "서버와 통신하는 중에 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(ExerciseResultActivity.this);
        queue.add(jsonObjectRequest);

        return array;
    }
}