package com.example.bangpt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bangpt.Commu.InfoWriteActivity;
import com.example.bangpt.Commu.InfoWriteViewActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class ExerciseResultList extends AppCompatActivity {
    // 로그에 사용할 TAG 변수
    final private String TAG = getClass().getSimpleName();

    // 사용할 컴포넌트 선언
    ListView listView;

    String userid = "";

    // 리스트뷰에 사용할 제목 배열
    ArrayList<String> titleList = new ArrayList<>();

    // 클릭했을 때 어떤 게시물을 클릭했는지 게시물 번호를 담기 위한 배열
    ArrayList<String> seqList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_result);
        // LoginActivity 에서 넘긴 userid 값 받기
        String userID = getIntent().getStringExtra("userID");
        SharedPreferences settings = getSharedPreferences("Login", 0);
        userid = settings.getString("userID", "");
        // 컴포넌트 초기화
        listView = findViewById(R.id.listView);

        // listView 를 클릭했을 때 이벤트 추가
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // 게시물의 번호와 userid를 가지고 DetailActivity 로 이동
                Intent intent = new Intent(ExerciseResultList.this, ExerciseResult2Activity.class);
                intent.putExtra("num", seqList.get(i));
                intent.putExtra("userid", userID);
                startActivity(intent);
            }
        });

        ExerciseResultList.GetBoard getBoard = new ExerciseResultList.GetBoard();
        getBoard.execute();
    }


    // onResume() 은 해당 액티비티가 화면에 나타날 때 호출됨
    @Override
    protected void onResume() {
        super.onResume();
        // 해당 액티비티가 활성화 될 때, 게시물 리스트를 불러오는 함수를 호출
        ExerciseResultList.GetBoard getBoard = new ExerciseResultList.GetBoard();
        getBoard.execute();
    }


    // 게시물 리스트를 읽어오는 함수
    class GetBoard extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.d(TAG, "onPreExecute");
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "onPostExecute, " + result);

            // 배열들 초기화
            titleList.clear();
            seqList.clear();

            try {
                JSONArray jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String title = jsonObject.optString("timestamp");
                    String seq = jsonObject.optString("_id");

                    // title, seq 값을 변수로 받아서 배열에 추가
                    titleList.add(title);
                    seqList.add(seq);
                }

                // ListView 에서 사용할 arrayAdapter를 생성하고, ListView 와 연결
                ArrayAdapter arrayAdapter = new ArrayAdapter<String>(ExerciseResultList.this, android.R.layout.simple_list_item_1, titleList);
                listView.setAdapter(arrayAdapter);

                // arrayAdapter의 데이터가 변경되었을때 새로고침
                arrayAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        @Override
        protected String doInBackground(String... params) {
            SharedPreferences settings = getSharedPreferences("Login", 0);
            userid = settings.getString("userID", "");

            String server_url = "http://172.20.10.8:821/exercise_result/load_board_title/" + userid;

            URL url;
            String response = "";
            try {
                url = new URL(server_url);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.connect();
                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                } else {
                    response = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }
    }
}
