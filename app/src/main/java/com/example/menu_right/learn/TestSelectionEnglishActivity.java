package com.example.menu_right.learn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.menu_right.R;

public class TestSelectionEnglishActivity extends AppCompatActivity {
    //khai báo
    private Button btnBack, btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_selection_english);
        setControl();
        setEvent();
    }
    //xử lí màn hình learn
    private void setEvent() {


        //Sử kiện nút trở lại
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestSelectionEnglishActivity.this.finish();
            }
        });
    }

    //Ánh xạ, khởi gán
    private void setControl() {
        //Ánh xạ
        btnBack = findViewById(R.id.btn_back_test);
        btnContinue = findViewById(R.id.btn_continute_test);
    }
}