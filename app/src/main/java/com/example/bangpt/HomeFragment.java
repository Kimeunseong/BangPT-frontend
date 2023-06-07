package com.example.bangpt;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import me.relex.circleindicator.CircleIndicator2;
import me.relex.circleindicator.CircleIndicator3;

public class HomeFragment extends Fragment {
    private ViewPager2 mPager;
    private CircleIndicator3 mIndicator;
    private int num_page = 3;
    TextView name1;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //set name
        name1 = view.findViewById(R.id.name1);

        Button btn_squat = view.findViewById(R.id.btn_squat);
        btn_squat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Exercise1Activity.class);
                startActivity(intent);
            }
        });

        // ViewPager2
        mPager = view.findViewById(R.id.viewpager);
        // Adapter
        MyAdapter pagerAdapter = new MyAdapter(getActivity(), num_page);
        mPager.setAdapter(pagerAdapter);
        // CircleIndicator2
        mIndicator = view.findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        mIndicator.createIndicators(num_page, 0);
        // ViewPager2 Setting
        mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mPager.setCurrentItem(1002); // 시작 지점
        mPager.setOffscreenPageLimit(3); // 최대 이미지 수

        // OnPageChangeCallback
        mPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {
                    mPager.setCurrentItem(position);
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mIndicator.animatePageSelected(position % num_page);
            }
        });

        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 인텐트에서 값을 받아옵니다.
        String userID = getActivity().getIntent().getStringExtra("userID");

        // TextView에 값을 표시합니다.
        name1.setText(userID);
    }
}