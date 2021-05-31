package com.inhatc.study_project.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.inhatc.study_project.R;
import com.inhatc.study_project.data.AppDatabase;
import com.inhatc.study_project.data.Dday;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddDday extends AppCompatActivity {
    private EditText edt_DdayName;
    private Button btnOK;
    private DatePicker datePicker;
    private int selectYear, selectMon, selectDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddday);

        btnOK = (Button) findViewById(R.id.btnOK);
        edt_DdayName = (EditText) findViewById(R.id.edt_DdayName);
        datePicker = (DatePicker) findViewById(R.id.DdayPicker);

        Intent intent = getIntent();
        selectYear = intent.getIntExtra("selectYear", 0);
        selectMon = intent.getIntExtra("selectMonth", 0);
        selectDay = intent.getIntExtra("selectDay", 0);
        datePicker.updateDate(selectYear, selectMon, selectDay);

        AppDatabase db = AppDatabase.getAppDatabase(this);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_DdayName.getText().toString().matches("")) {
                    Toast.makeText(AddDday.this, "디데이명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(() -> {
                    try {
                        Dday newDday = new Dday();
                        newDday.setDdayName(edt_DdayName.getText().toString());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        Date inputDate = sdf.parse(String.format("%04d/%02d/%02d", datePicker.getYear(), datePicker.getMonth()+1, datePicker.getDayOfMonth()));
                        newDday.setDdayDate(inputDate);

                        db.ddayDao().insert(newDday);
                    } catch (ParseException pEx) {
                        Log.d("AddDday", pEx.toString());
                    }
                }).start();
                finish();
            }
        });
    }
}