package com.example.EnglishBeginner.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.EnglishBeginner.R;

public class EditStudyModeActivity extends AppCompatActivity{
    private static int Curent_Mode = 1;
    private static int check = 0;
    private LinearLayout Basic, Medium, Hard, SuperHard;
    private CheckBox cbRemind;
    private Button btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_study_mode);

        Basic = findViewById(R.id.item_basic);
        Medium = findViewById(R.id.item_medium);
        Hard = findViewById(R.id.item_hard);
        SuperHard = findViewById(R.id.item_superhard);
        cbRemind = findViewById(R.id.cb_edit_study_mode);
        btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditStudyModeActivity.this.finish();
            }
        });
        if (check == 1){
            cbRemind.setChecked(true);
        }else {
            cbRemind.setChecked(false);
        }

        switch (Curent_Mode){
            case 1:
                Basic.setSelected(true);
                Medium.setSelected(false);
                Hard.setSelected(false);
                SuperHard.setSelected(false);
                break;
            case 2:
                Basic.setSelected(false);
                Medium.setSelected(true);
                Hard.setSelected(false);
                SuperHard.setSelected(false);
                break;
            case 3:
                Basic.setSelected(false);
                Medium.setSelected(false);
                Hard.setSelected(true);
                SuperHard.setSelected(false);
                break;
            case 4:
                Basic.setSelected(false);
                Medium.setSelected(false);
                Hard.setSelected(false);
                SuperHard.setSelected(true);
                break;
        }

        Basic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Basic.setSelected(true);
                Medium.setSelected(false);
                Hard.setSelected(false);
                SuperHard.setSelected(false);
                Curent_Mode = 1;
                if (Basic.isSelected()){
                    Toast.makeText(EditStudyModeActivity.this, "Chế độ huấn luyện: Cơ Bản - 30xp một ngày.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Medium.setSelected(true);
                Basic.setSelected(false);
                Hard.setSelected(false);
                SuperHard.setSelected(false);
                Curent_Mode = 2;
                if (Medium.isSelected()){
                    Toast.makeText(EditStudyModeActivity.this, "Chế độ huấn luyện: Vừa - 50xp một ngày.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hard.setSelected(true);
                Basic.setSelected(false);
                Medium.setSelected(false);
                SuperHard.setSelected(false);
                Curent_Mode = 3;
                if (Hard.isSelected()){
                    Toast.makeText(EditStudyModeActivity.this, "Chế độ huấn luyện: Khó - 80xp một ngày.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        SuperHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SuperHard.setSelected(true);
                Basic.setSelected(false);
                Medium.setSelected(false);
                Hard.setSelected(false);
                Curent_Mode = 4;
                if (SuperHard.isSelected()){
                    Toast.makeText(EditStudyModeActivity.this, "Chế độ huấn luyện: Cực Khó - 1000xp một ngày.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cbRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbRemind.isChecked()){
                    check = 1;
                    Toast.makeText(EditStudyModeActivity.this, "Đã bật nhắc nhở luyện tập!", Toast.LENGTH_SHORT).show();
                }else {
                    check = 0;
                    Toast.makeText(EditStudyModeActivity.this, "Đã tắt nhắc nhở luyện tập!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}