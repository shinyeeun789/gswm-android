package com.inhatc.study_project.ui;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.inhatc.study_project.R;

import java.util.ArrayList;

public class FragmentStatistics extends Fragment {
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private Fragment fmDay, fmWeek, fmMonth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        fmDay = new FragmentStatDay();
        fmWeek = new FragmentStatWeek();
        fmMonth = new FragmentStatMonth();
        tabLayout = (TabLayout) view.findViewById(R.id.statTabLayout);

       // 스와이프할 뷰페이저 정의
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        mViewPager.setAdapter(new PagerAdapter(getChildFragmentManager()));
        mViewPager.setCurrentItem(0);

        // addOnPageChangeListener : 뷰페이저의 페이지가 변경될 때 알려주는 리스너
        // TabLayoutOnPageChangeListener : 뷰 페이저의 페이지가 변경될 때 TabLayout에도 알려주도록
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        // addOnTabSelectedListener : Tab이 선택되었을 때 알려주는 리스너
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            // 탭이 선택되었을 때 호출
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());           // 뷰페이저의 페이지를 선택된 탭의 포지션에 맞추어 바꾸어주도록 설정
            }
            // 탭이 선택되지 않았을 때 호출
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            // 탭이 다시 선택되었을 때 호출
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        return view;
    }

    private class PagerAdapter extends FragmentPagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
            getItem(0);
        }

        // 탭의 position에 해당하는 Fragment 반환
        public Fragment getItem(int position) {
            if (position == 0) {
                return fmDay;
            } else if (position == 1) {
                return fmWeek;
            } else {
                return fmMonth;
            }
        }

        // page의 개수 반환
        public int getCount() {
            return 3;
        }
    }
}
