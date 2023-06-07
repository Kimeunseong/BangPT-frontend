package com.example.bangpt.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.example.bangpt.VolleyMultipartRequest;

import java.util.HashMap;
import java.util.Map;

public class GetSplitVideoRequest extends VolleyMultipartRequest {
    static private int video_num;
    static public String GetURL(int videoNum){
        video_num = videoNum;
        String URL="http://172.20.10.8:821/model/get_video/" + video_num;
        return URL;
    }
    private Map<String,String> map;

    public GetSplitVideoRequest(String video_num, String URL, Response.Listener<NetworkResponse>listener){

        super(Request.Method.GET,URL,listener,null);

        map=new HashMap<>();
        map.put("video_num",video_num);
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}