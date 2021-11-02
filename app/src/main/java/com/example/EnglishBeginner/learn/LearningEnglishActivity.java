package com.example.EnglishBeginner.learn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.EnglishBeginner.R;

public class LearningEnglishActivity extends AppCompatActivity {
    //khai báo
    private Button btnBack, btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_english);
        setControl();
        setEvent();
    }

    //xử lí màn hình learn
    private void setEvent() {


        //Sử kiện nút trở lại
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LearningEnglishActivity.this.finish();
            }
        });
    }

    //Ánh xạ, khởi gán
    private void setControl() {
        btnBack = findViewById(R.id.btn_back_learn);
        btnContinue = findViewById(R.id.btn_continute_learn);
    }
}