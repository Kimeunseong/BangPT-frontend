package com.example.bangpt.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ChallengeRequest extends StringRequest{
    //서버 URL을 설정 (php파일을 연동함)
    final static private String URL = "http://172.20.10.8:821/participants/new";
    private Map<String, String> map;

    //생성자
    public ChallengeRequest(String participantId, Response.Listener<String> listener){
        super(Request.Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("participantId", participantId);
        map.put("participantSuccess", "false");
        // 랭킹이 의미 없는 경우 -1 저장
        map.put("participantRanking", "-1");
        // 챌린지 도중 필요한 내용 저장하기 위함.
        map.put("participantNote", "");

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
