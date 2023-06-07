package com.example.bangpt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.bson.types.Binary;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class MyChallActivity extends AppCompatActivity {
    private TextView countTextView;
    private TextView badge1NameTextView;
    private TextView badge1DescTextView;
    private TextView badge2NameTextView;
    private TextView badge2DescTextView;
    private TextView badge3NameTextView;
    private TextView badge3DescTextView;
    private TextView badge4NameTextView;
    private TextView badge4DescTextView;
    private TextView badge5NameTextView;
    private TextView badge5DescTextView;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private ImageView imageView5;
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_chall);
        // userID 설정 (예: "60192243")
        String userID = getIntent().getStringExtra("userID");

        countTextView = findViewById(R.id.count);
        badge1NameTextView = findViewById(R.id.badge1_name);
        badge1DescTextView = findViewById(R.id.badge1_desc);
        badge2NameTextView = findViewById(R.id.badge2_name);
        badge2DescTextView = findViewById(R.id.badge2_desc);
        badge3NameTextView = findViewById(R.id.badge3_name);
        badge3DescTextView = findViewById(R.id.badge3_desc);
        badge4NameTextView = findViewById(R.id.badge4_name);
        badge4DescTextView = findViewById(R.id.badge4_desc);
        badge5NameTextView = findViewById(R.id.badge5_name);
        badge5DescTextView = findViewById(R.id.badge5_desc);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);



        fetchBadgeInfo();

    }

    private void fetchBadgeInfo() {
        SharedPreferences settings = getSharedPreferences("Login", 0);
        userid = settings.getString("userID", "");

        String serverUrl = "http://172.20.10.8:821/userbadge/badges/" + userid;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, serverUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray badges = response.getJSONArray("badges");
                            int badgeCount = badges.length();
                            countTextView.setText(String.valueOf(badgeCount));

                            for (int i = 0; i < badgeCount; i++) {
                                JSONObject badge = badges.getJSONObject(i);
                                int badgeNum = badge.getInt("badge_num");
                                String badgeName = badge.getString("badge_name");
                                String badgeDesc = badge.getString("badge_desc");
                                String badgeImageUrl = badge.getString("badge_image_url");

                                // Load image from URL and display in ImageView
                                ImageView imageView = null;
                                switch (badgeNum) {
                                    case 1:
                                        imageView = imageView1;
                                        break;
                                    case 2:
                                        imageView = imageView2;
                                        break;
                                    case 3:
                                        imageView = imageView3;
                                        break;
                                    case 4:
                                        imageView = imageView4;
                                        break;
                                    case 5:
                                        imageView = imageView5;
                                        break;
                                }
                                if (imageView != null) {
                                    loadImageFromUrl(badgeImageUrl, imageView);
                                }

                                // Set badge name and description in respective TextViews
                                TextView badgeNameTextView;
                                TextView badgeDescTextView;
                                switch (badgeNum) {
                                    case 1:
                                        badgeNameTextView = badge1NameTextView;
                                        badgeDescTextView = badge1DescTextView;
                                        break;
                                    case 2:
                                        badgeNameTextView = badge2NameTextView;
                                        badgeDescTextView = badge2DescTextView;
                                        break;
                                    case 3:
                                        badgeNameTextView = badge3NameTextView;
                                        badgeDescTextView = badge3DescTextView;
                                        break;
                                    case 4:
                                        badgeNameTextView = badge4NameTextView;
                                        badgeDescTextView = badge4DescTextView;
                                        break;
                                    case 5:
                                        badgeNameTextView = badge5NameTextView;
                                        badgeDescTextView = badge5DescTextView;
                                        break;
                                    default:
                                        continue;  // Skip if badge_num doesn't match any existing TextViews
                                }

                                badgeNameTextView.setText(badgeName);
                                badgeDescTextView.setText(badgeDesc);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MyChallActivity.this, "서버 응답을 처리하는 중에 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(MyChallActivity.this, "서버와 통신하는 중에 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(MyChallActivity.this);
        queue.add(jsonObjectRequest);
    }

    private void loadImageFromUrl(String imageUrl, final ImageView imageView) {
        ImageRequest imageRequest = new ImageRequest(imageUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                    }
                },
                0,
                0,
                ImageView.ScaleType.CENTER_INSIDE,
                Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(MyChallActivity.this, "이미지를 로드하는 중에 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(MyChallActivity.this);
        queue.add(imageRequest);
    }
}
