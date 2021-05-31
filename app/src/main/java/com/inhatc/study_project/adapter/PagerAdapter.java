package com.inhatc.study_project.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.inhatc.study_project.ui.FragmentStatDay;
import com.inhatc.study_project.ui.FragmentStatMonth;
import com.inhatc.study_project.ui.FragmentStatWeek;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends FragmentStatePagerAdapter {
    List<Fragment> fragmentList = new ArrayList<>();

    public PagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList.add(new FragmentStatDay());
        fragmentList.add(new FragmentStatWeek());
        fragmentList.add(new FragmentStatMonth());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
