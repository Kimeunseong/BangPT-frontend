package com.example.bangpt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.bangpt.Request.CalendarMemoRequest;
import com.example.bangpt.Request.GetDiaryRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CalendarMemoActivity extends AppCompatActivity {
    private ArrayList<String> itemList;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private EditText editText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_memo);

        // 인텐트로 전달한 데이터를 가져옵니다.
        int year = getIntent().getIntExtra("year", 0);
        int month = getIntent().getIntExtra("month", 0);
        int day = getIntent().getIntExtra("day", 0);
        String userID = getIntent().getStringExtra("userID");

        itemList = new ArrayList<>();
        itemList.add("itemList 초기화 위함.");
        itemList.remove("itemList 초기화 위함.");

        // XML에서 ListView, EditText, Button을 찾아 연결합니다.
        listView = findViewById(R.id.my_list2);
        editText = findViewById(R.id.write);
        button = findViewById(R.id.reg_button);

        // ListView에 사용될 어댑터를 생성합니다.
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);

        // ListView에 어댑터를 설정합니다.
        listView.setAdapter(adapter);

        //itemList에 DB에 저장된 값 저장하기
        Response.Listener<String> responseListener=new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    String jsonString = jsonArray.toString();

                    for(int i=0 ; i < jsonArray.length() ; i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String memo = jsonObject.optString("diary_memo");
                        itemList.add(memo);

                        adapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        String date = String.format("%04d", year) + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
        String URL = GetDiaryRequest.GetURL(userID, date);
        GetDiaryRequest getDiaryRequest=new GetDiaryRequest(userID, URL,responseListener);
        RequestQueue queue= Volley.newRequestQueue(CalendarMemoActivity.this);
        queue.add(getDiaryRequest);

        // 등록 버튼 클릭 시 이벤트 리스너를 설정합니다.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // EditText에서 입력한 텍스트를 가져옵니다.
                String inputText = editText.getText().toString();

                // 가져온 텍스트를 itemList에 추가합니다.
                if(inputText.equals("") == false) {
                    itemList.add(inputText);

                    // 어댑터에 변경된 데이터를 알립니다.
                    adapter.notifyDataSetChanged();

                    // EditText의 내용을 초기화합니다.
                    editText.setText("");

                    //DB에 운동일지를 추가합니다.
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    Log.d("CalendarMemoActivity", "메모 추가 성공");
                                } else {
                                    Log.d("CalendarMemoActivity", "메모 추가 실패");
                                    return;
                                }
                            } catch (JSONException e) {
                                Log.d("CalendarMemoActivity", "메모 추가 위한 연동 실패");
                                e.printStackTrace();
                            }

                        }
                    };

                    CalendarMemoRequest calendarMemoRequest = new CalendarMemoRequest(userID, year, month, day, inputText, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(CalendarMemoActivity.this);
                    queue.add(calendarMemoRequest);
                }

            }
        });

    }
}