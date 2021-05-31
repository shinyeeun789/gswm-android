package com.inhatc.study_project.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.inhatc.study_project.AlarmReceiver;
import com.inhatc.study_project.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {
    public static Activity _Main_Activity;
    Context context;

    private FragmentStatistics fragmentStatistics = new FragmentStatistics();
    private FragmentCalendar fragmentCalendar = new FragmentCalendar();
    private FragmentHome fragmentHome = new FragmentHome();
    private FragmentPlace fragmentPlace = new FragmentPlace();
    private FragmentReward fragmentReward = new FragmentReward();

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            initView();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {  }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getBaseContext();
        checkPermissions();

        setContentView(R.layout.activity_main);
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) { // 마시멜로(안드로이드 6.0) 이상 권한 체크
            TedPermission.with(context)
                    .setPermissionListener(permissionlistener)
                    //.setRationaleMessage("앱을 이용하기 위해서는 접근 권한이 필요합니다")
                    .setDeniedMessage("앱에서 요구하는 권한설정이 필요합니다...\n [설정] > [권한] 에서 사용으로 활성화해주세요.")
                    .setPermissions(new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                    }).check();
        } else {
            initView();
        }
    }

    private void initView() {
        // 프래그먼트 트랜잭션(프래그먼트 백 스택 관리, 프래그먼트 전환 애니메이션 설정 등) 시작

        // add(): 추가, remove(): 삭제, replace(): 전환
        // commit()은 activity가 state 저장하기 전에 이루어져야 함. 저장된 후 불리면 exception 발생
        // commitAllowingStateLoss(): 프래그먼트가 state 저장과 관련없게 작동되도록 사용
        replaceFragment(fragmentHome);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());   // 메뉴 클릭에 대한 이벤트 처리
        bottomNavigationView.setSelectedItemId(R.id.page_home);     // 선택된 아이템 지정
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch(menuItem.getItemId()) {
                case R.id.page_home:
                    replaceFragment(fragmentHome);
                    break;
                case R.id.page_calendar:
                    replaceFragment(fragmentCalendar);
                    break;
                case R.id.page_statistics:
                    replaceFragment(fragmentStatistics);
                    break;
                case R.id.page_place:
                    replaceFragment(fragmentPlace);
                    break;
                case R.id.page_gift:
                    replaceFragment(fragmentReward);
                    break;
            }
            return true;
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment).commitAllowingStateLoss();
    }
}