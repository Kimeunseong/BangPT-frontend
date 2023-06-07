package com.example.bangpt.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CalendarMemoRequest extends StringRequest {
    //서버 URL을 설정 (php파일을 연동함)
    final static private String URL = "http://172.20.10.8:821/diary/new";
    private Map<String, String> map;

    //생성자
    public CalendarMemoRequest(String diaryID, int year, int month, int day, String inputText, Response.Listener<String> listener){
        super(Request.Method.POST, URL, listener, null);

        //0000-00-00의 형태로 만들기 위함.
        String Year = String.format("%04d", year);
        String Month = String.format("%02d", month);
        String Day = String.format("%02d", day);

        String date = Year + "-" + Month + "-" + Day;

        map = new HashMap<>();
        map.put("diaryID", diaryID);
        map.put("diaryDate", date);
        map.put("diaryNum", "0");
        map.put("diaryMemo", inputText);

    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
