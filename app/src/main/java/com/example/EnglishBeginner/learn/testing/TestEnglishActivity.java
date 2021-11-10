package com.example.EnglishBeginner.learn.testing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.EnglishBeginner.DTO.Question;
import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.main_interface.UserInterfaceActivity;

import java.util.ArrayList;
import java.util.List;

public class TestEnglishActivity extends AppCompatActivity {
    private ImageView imgExit;
    private ProgressBar progressBar;
    private Button btnPass, btnSubmit;
    private List<Question> arrayListQuestion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_english);
        Intent intent = getIntent();
        if (intent!=null)
        {
            arrayListQuestion = (List<Question>) intent.getSerializableExtra("listQuestion");
        }
        imgExit = findViewById(R.id.img_btn_exit);
        btnPass = findViewById(R.id.btn_pass);
        btnSubmit = findViewById(R.id.btn_continute);
        progressBar = findViewById(R.id.progress_bar);
        //Sử kiện nút trở lại
        imgExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestEnglishActivity.this, UserInterfaceActivity.class);
                startActivity(intent);
            }
        });
    }
}