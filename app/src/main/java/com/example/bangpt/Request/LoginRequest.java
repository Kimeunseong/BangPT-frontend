package com.example.bangpt.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

// URL에 Post방식으로 파라미터들을 전송하는 역할을 수행
// 로그인 정보
public class LoginRequest extends StringRequest {

    //서버 URL 설정 (PHP 파일 연동)
    final static  private String URL="http://172.20.10.8:821/user/login";
    private Map<String,String> map;

    public LoginRequest(String userID, String userPassword, Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);

        map=new HashMap<>();
        map.put("userID",userID);
        map.put("userPassword",userPassword);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }

}
