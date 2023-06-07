package com.example.bangpt.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetDatesRequest extends StringRequest {
    static private String diaryID, diaryYear, diaryMonth;
    static public String GetURL(String ID, String Year, String Month){
        diaryID = ID;
        diaryYear = Year;
        diaryMonth = Month;
        String URL="http://172.20.10.8:821/diary/get/" + diaryID + "/" + diaryYear + "/" + diaryMonth;
        return URL;
    }

    //서버 url 설정
    //final static  private String URL="http://10.0.2.2:821/diary/" + diaryID + "/" + diaryDate;
    private Map<String,String> map;

    public GetDatesRequest(String diaryID, String URL, Response.Listener<String>listener){

        super(Request.Method.GET,URL,listener,null);

        map=new HashMap<>();
        map.put("diaryID",diaryID);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}