package com.example.bangpt.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CheckIDRequest extends StringRequest {
    //서버 url 설정
    final static  private String URL="http://172.20.10.8:821/user/chk";
    private Map<String,String> map;

    public CheckIDRequest(String userID, Response.Listener<String>listener){
        super(Request.Method.POST,URL,listener,null);

        map=new HashMap<>();
        map.put("userID",userID);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
