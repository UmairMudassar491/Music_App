package com.example.music_app;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int numOfTables;
    public PagerAdapter(@NonNull FragmentManager fm, int numOfTables) {
        super(fm);
        this.numOfTables =numOfTables;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new News();
            case 1:
                return new Sports();
            case 2:
                return new Music();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTables;
    }
}
