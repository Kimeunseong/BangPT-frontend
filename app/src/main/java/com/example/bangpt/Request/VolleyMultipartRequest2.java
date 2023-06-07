package com.example.bangpt.Request;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class VolleyMultipartRequest2 extends Request<NetworkResponse> {
    private final String boundary = "volleyMultipartBoundary";
    private final String lineEnd = "\r\n";
    private final String twoHyphens = "--";

    private Response.Listener<NetworkResponse> mListener;
    private Response.ErrorListener mErrorListener;
    private Map<String, String> mHeaders;

    public VolleyMultipartRequest2(
            int method,
            String url,
            Response.Listener<NetworkResponse> listener,
            Response.ErrorListener errorListener
    ) {
        super(method, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
        this.mHeaders = new HashMap<>();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders;
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data;boundary=" + boundary;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // 파일 파트 데이터 작성
        try {
            Map<String, DataPart> params = getByteData();
            if (params != null && params.size() > 0) {
                for (Map.Entry<String, DataPart> entry : params.entrySet()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder
                            .append(twoHyphens)
                            .append(boundary)
                            .append(lineEnd)
                            .append("Content-Disposition: form-data; name=\"")
                            .append(entry.getKey())
                            .append("\"; filename=\"")
                            .append(entry.getValue().getFileName())
                            .append("\"")
                            .append(lineEnd)
                            .append("Content-Type: ")
                            .append(entry.getValue().getMimeType())
                            .append(lineEnd)
                            .append(lineEnd);
                    outputStream.write(stringBuilder.toString().getBytes());
                    outputStream.write(entry.getValue().getContent());
                    outputStream.write(lineEnd.getBytes());
                }
            }
            outputStream.write((twoHyphens + boundary + twoHyphens + lineEnd).getBytes());
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(
                response,
                HttpHeaderParser.parseCacheHeaders(response)
        );
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        mErrorListener.onErrorResponse(error);
    }

    protected Map<String, DataPart> getByteData() throws AuthFailureError {
        return null;
    }

    public static class DataPart {
        private String fileName;
        private byte[] content;
        private String mimeType;

        public DataPart(String fileName, byte[] content) {
            this.fileName = fileName;
            this.content = content;
            this.mimeType = "video/*";
        }

        public String getFileName() {
            return fileName;
        }

        public byte[] getContent() {
            return content;
        }

        public String getMimeType() {
            return mimeType;
        }
    }
}