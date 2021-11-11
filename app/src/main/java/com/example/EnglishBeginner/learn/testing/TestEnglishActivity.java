package com.example.EnglishBeginner.learn.testing;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.EnglishBeginner.DTO.DEFAULTVALUE;
import com.example.EnglishBeginner.DTO.Question;
import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.main_interface.UserInterfaceActivity;
import com.google.android.gms.dynamic.IFragmentWrapper;

import java.util.List;

public class TestEnglishActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imgExit;
    private ProgressBar progressBar;
    public static Button btnPass, btnSubmit;
    private List<Question> arrayListQuestion;
    private int count = 0;
    private int countCorrect = 0;
    private int min = 0;
    private int max = 0;
    private int randomQuestion = 0;
    public String correctQuestion = null, answer = null;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_english);
        Intent intent = getIntent();
        if (intent != null) {
            arrayListQuestion = (List<Question>) intent.getSerializableExtra("listQuestion");
        }
        if (arrayListQuestion != null && arrayListQuestion.size() > 0) {
            max = arrayListQuestion.size() - 1;
        }
        processLearn();
        imgExit = findViewById(R.id.img_btn_exit);
        btnPass = findViewById(R.id.btn_pass);
        btnSubmit = findViewById(R.id.btn_continute);
        btnSubmit.setOnClickListener(this);
        progressBar = findViewById(R.id.progress_bar);
        frameLayout = findViewById(R.id.frameLayout_Fragment);
        //Sử kiện nút trở lại
        imgExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestEnglishActivity.this, UserInterfaceActivity.class);
                startActivity(intent);
            }
        });
    }

    private void processLearn() {
        if (arrayListQuestion.size() < 10) {
            if (count <= arrayListQuestion.size()) {
                randomQuestion = (int) Math.floor(Math.random() * (max - min + 1) + min);
                Question question = arrayListQuestion.get(randomQuestion);
                transactionFragment(question);
            }
        } else {
            if (count <= 10) {
                randomQuestion = (int) Math.floor(Math.random() * (max - min + 1) + min);
                Question question = arrayListQuestion.get(randomQuestion);
                transactionFragment(question);
            }
        }
    }

    private void transactionFragment(Question question) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable("question", question);
        correctQuestion = question.getCorrectAnswer();
        if (question.getNameTypeQuestion().equalsIgnoreCase(DEFAULTVALUE.IMAGE)) {
            TestChooseImageFragment testChooseImageFragment = new TestChooseImageFragment();
            testChooseImageFragment.setArguments(bundle);
            if (count == 0) {
                fragmentTransaction.add(R.id.frameLayout_Fragment, testChooseImageFragment).commit();
            }
            else
            {
                fragmentTransaction.replace(R.id.frameLayout_Fragment, testChooseImageFragment).commit();
            }
        } else if (question.getNameTypeQuestion().equalsIgnoreCase(DEFAULTVALUE.LISTEN)) {
            TestListenFragment testListenFragment = new TestListenFragment();
            testListenFragment.setArguments(bundle);
            if (count == 0) {
                fragmentTransaction.add(R.id.frameLayout_Fragment, testListenFragment).commit();
            }
            else
            {
                fragmentTransaction.replace(R.id.frameLayout_Fragment, testListenFragment).commit();
            }

        } else if (question.getNameTypeQuestion().equalsIgnoreCase(DEFAULTVALUE.WRITE)) {
            TestWriteFragment testWriteFragment = new TestWriteFragment();
            testWriteFragment.setArguments(bundle);
            if (count == 0) {
                fragmentTransaction.add(R.id.frameLayout_Fragment, testWriteFragment).commit();
            }
            else
            {
                fragmentTransaction.replace(R.id.frameLayout_Fragment, testWriteFragment).commit();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_continute:
                submitResult();
                break;
        }
    }

    private void submitResult() {
        if (answer == null)
        {
            Log.d("test", "Sai");
        }
        else {
            if (answer.equalsIgnoreCase(correctQuestion)) {
                Log.d("test", "Đúng");
            } else {
                Log.d("test", "Sai");
            }
        }
    }
}