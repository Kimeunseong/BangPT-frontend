package com.example.bangpt.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class GetDiaryRequest extends StringRequest {
    static private String diaryID, diaryDate;
    static public String GetURL(String ID, String Date){
        diaryID = ID;
        diaryDate = Date;
        String URL="http://172.20.10.8:821/diary/get/" + diaryID + "/" + diaryDate;
        return URL;
    }

    private Map<String,String> map;

    public GetDiaryRequest(String diaryID, String URL, Response.Listener<String>listener){

        super(Request.Method.GET,URL,listener,null);

        map=new HashMap<>();
        map.put("diaryID",diaryID);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}