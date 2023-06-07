package com.example.bangpt.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class JoinRequest extends StringRequest {

    //서버 URL을 설정 (php파일을 연동함)
    final static private String URL = "http://172.20.10.8:821/user/join";
    private Map<String, String> map;

    //생성자
    public JoinRequest(String userID, String userPassword, String userName, String userNick,Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID", userID);
        map.put("userPassword", userPassword);
        map.put("userName", userName);
        map.put("userNick", userNick);
        map.put("trainer", "일반 회원");

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
