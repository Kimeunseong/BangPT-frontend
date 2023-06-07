package com.example.bangpt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.bangpt.banner.Banner1Fragment;
import com.google.android.material.navigation.NavigationBarView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import me.relex.circleindicator.CircleIndicator3;

public class MainActivity extends AppCompatActivity {

    String userID;
    String userPass;
    HomeFragment homeFragment;
    CalendarFragment calendarFragment;
    CommunityFragment communityFragment;
    MypageFragment mypageFragment;
    ChallengeFragment challengeFragment;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        userPass = intent.getStringExtra("userPass");

        homeFragment = new HomeFragment();
        calendarFragment = new CalendarFragment();
        communityFragment = new CommunityFragment();
        mypageFragment = new MypageFragment();
        challengeFragment = new ChallengeFragment();


        //textView = findViewById(R.id.text_view);

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://10.0.2.2:821/");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
        /*
                    try {
                        int forTest = conn.getResponseCode(); // 응답 상태 코드 저장
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
        */

                    /*BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    String line;
                    StringBuilder sb = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }

                    final String response = sb.toString();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(response);
                        }
                    });

                }catch(Exception e){
                //    textView.setText("error");
                    e.printStackTrace();
                }
            }
        }).start();*/

        getSupportFragmentManager().beginTransaction().replace(R.id.containers, homeFragment).commit();

        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigationview);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){

                switch(item.getItemId()){
                    case R.id.home: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, homeFragment).commit();

                        return true;
                    }
                    case R.id.mypage: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, mypageFragment).commit();
                        Bundle bundle = new Bundle();
                        bundle.putString("userID", userID);
                        bundle.putString("userPass", userPass);
                        mypageFragment.setArguments(bundle);
                        return true;
                    }
                    case R.id.calendar:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, calendarFragment).commit();
                        Bundle bundle = new Bundle();
                        bundle.putString("userID", userID);
                        calendarFragment.setArguments(bundle);
                        return true;
                    case R.id.challenge:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, challengeFragment).commit();
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("userID", userID);
                        challengeFragment.setArguments(bundle1);
                        return true;
                    case R.id.community:{
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, communityFragment).commit();
                        //Bundle bundle = new Bundle();
                        //bundle.putString("userID", userID);
                        //communityFragment.setArguments(bundle);
                        return true;
                    }
                    default :{
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, homeFragment).commit();
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("userID", userID);
                        homeFragment.setArguments(bundle2);
                        // return true;
                    }
                }
                return false;
            }

        });

    }
}