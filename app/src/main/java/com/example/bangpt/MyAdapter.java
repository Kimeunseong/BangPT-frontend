package com.example.bangpt;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.fragment.app.Fragment;

import com.example.bangpt.banner.Banner1Fragment;
import com.example.bangpt.banner.Banner2Fragment;
import com.example.bangpt.banner.Banner3Fragment;

public class MyAdapter extends FragmentStateAdapter {

    public int mCount;

    public MyAdapter(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);

        if(index==0) return new Banner1Fragment();
        else if(index==1) return new Banner2Fragment();
        else return new Banner3Fragment();
    }

    @Override
    public int getItemCount() {
        return 2000;
    }

    public int getRealPosition(int position) { return position % mCount; }

}