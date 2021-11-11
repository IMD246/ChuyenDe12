package com.example.EnglishBeginner.learn.testing;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.EnglishBeginner.DTO.DEFAULTVALUE;
import com.example.EnglishBeginner.DTO.Question;
import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.learn.learning.LearningEnglishFragment;
import com.example.EnglishBeginner.main_interface.UserInterfaceActivity;

import java.util.List;

public class TestEnglishActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressBar progressBar;
    @SuppressLint("StaticFieldLeak")
    public static Button btnPass, btnSubmit;
    private List<Question> arrayListQuestion;
    private int count = 0;
    private int max = 0;
    public String correctQuestion = null, answer = null;
    private String typeLearn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_english);
        Intent intent = getIntent();
        if (intent != null) {
            arrayListQuestion = (List<Question>) intent.getSerializableExtra("listQuestion");
            typeLearn = intent.getStringExtra("learn");
        }
        if (arrayListQuestion != null && arrayListQuestion.size() > 0) {
            max = arrayListQuestion.size() - 1;
        }
        ImageView imgExit = findViewById(R.id.img_btn_exit);
        btnPass = findViewById(R.id.btn_pass);
        btnSubmit = findViewById(R.id.btn_continute);
        btnSubmit.setOnClickListener(this);
        progressBar = findViewById(R.id.learn_progress_bar);
        //Sử kiện nút trở lại
        imgExit.setOnClickListener(v -> {
            Intent intent1 = new Intent(TestEnglishActivity.this, UserInterfaceActivity.class);
            startActivity(intent1);
        });
        processLearn();
    }

    private void processLearn() {
        int min = 0;
        int randomQuestion = (int) Math.floor(Math.random() * (max - min + 1) + min);
        Question question = arrayListQuestion.get(randomQuestion);
        if (arrayListQuestion.size() < 10) {
            progressBar.setMax(arrayListQuestion.size());
            if (count < arrayListQuestion.size()) {
                transactionFragment(question);
            }
            else
            {
                if (typeLearn.equalsIgnoreCase(DEFAULTVALUE.TEST)) {
                    alertDialogComplete("Hoàn thành bài học");
                }
                else if (typeLearn.equalsIgnoreCase(DEFAULTVALUE.LEARN)) {
                    alertDialogComplete("Chúc bạn làm bài test tốt");
                }
            }
        }
        else {
            progressBar.setMax(10);
            if (count < 10) {
                transactionFragment(question);
            }
            else
            {
                if (typeLearn.equalsIgnoreCase(DEFAULTVALUE.TEST)) {
                    alertDialogComplete("Hoàn thành bài học");
                }
                else if (typeLearn.equalsIgnoreCase(DEFAULTVALUE.LEARN)) {
                    alertDialogComplete("Chúc bạn làm bài test tốt");
                }
            }
        }
    }

    @SuppressLint("ResourceType")
    private void transactionFragment(Question question) {
        FragmentTransaction fragmentTransaction;
        if (count <=0) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
        }
        else
        {
            fragmentTransaction = getSupportFragmentManager().beginTransaction().setCustomAnimations(R.transition.transisionfragmentlearn,R.transition.transisionfragmentlearn);
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("question", question);
        correctQuestion = question.getCorrectAnswer();
        if (typeLearn!=null || typeLearn.trim().length()>0) {
            if (typeLearn.equalsIgnoreCase(DEFAULTVALUE.TEST)) {
                btnPass.setVisibility(View.VISIBLE);
                if (question.getNameTypeQuestion().equalsIgnoreCase(DEFAULTVALUE.IMAGE)) {
                    TestChooseImageFragment testChooseImageFragment = new TestChooseImageFragment();
                    testChooseImageFragment.setArguments(bundle);
                    if (count == 0) {
                        fragmentTransaction.add(R.id.frameLayout_Fragment, testChooseImageFragment).commit();
                    } else {
                        fragmentTransaction.replace(R.id.frameLayout_Fragment, testChooseImageFragment).commit();
                    }
                } else if (question.getNameTypeQuestion().equalsIgnoreCase(DEFAULTVALUE.LISTEN)) {
                    TestListenFragment testListenFragment = new TestListenFragment();
                    testListenFragment.setArguments(bundle);
                    if (count == 0) {
                        fragmentTransaction.add(R.id.frameLayout_Fragment, testListenFragment).commit();
                    } else {
                        fragmentTransaction.replace(R.id.frameLayout_Fragment, testListenFragment).commit();
                    }

                } else if (question.getNameTypeQuestion().equalsIgnoreCase(DEFAULTVALUE.WRITE)) {
                    TestWriteFragment testWriteFragment = new TestWriteFragment();
                    testWriteFragment.setArguments(bundle);
                    if (count == 0) {
                        fragmentTransaction.add(R.id.frameLayout_Fragment, testWriteFragment).commit();
                    } else {
                        fragmentTransaction.replace(R.id.frameLayout_Fragment, testWriteFragment).commit();
                    }
                }
            }
            else if (typeLearn.equalsIgnoreCase(DEFAULTVALUE.LEARN))
            {
                btnPass.setVisibility(View.GONE);
                LearningEnglishFragment learningEnglishFragment = new LearningEnglishFragment();
                learningEnglishFragment.setArguments(bundle);
                if (count == 0) {
                    fragmentTransaction.add(R.id.frameLayout_Fragment, learningEnglishFragment).commit();
                } else {
                    fragmentTransaction.replace(R.id.frameLayout_Fragment, learningEnglishFragment).commit();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_continute) {
            submitResult();
        }
    }
    private void submitResult() {
        if (typeLearn.equalsIgnoreCase(DEFAULTVALUE.TEST)) {
            if (answer == null) {
                alertDialog("Trả lời sai");
            } else {
                if (answer.equalsIgnoreCase(correctQuestion)) {
                    alertDialog("Trả lời đúng");
                } else {
                    alertDialog("Trả lời sai");
                }
                answer = null;
            }
        }
        else if  (typeLearn.equalsIgnoreCase(DEFAULTVALUE.LEARN))
        {
            alertDialog("Đã hoàn thành phần học");
        }
        count++;
        progressBar.setProgress(count,true);
    }
    // Xây dựng một Hộp thoại thông báo
    public void alertDialog(String msg) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Tiếp tục",
                (dialog, id) -> {
                    if (count>0) {
                        new CountDownTimer(2000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                btnPass.setEnabled(false);
                                btnSubmit.setEnabled(false);
                            }

                            public void onFinish() {
                                btnPass.setEnabled(true);
                                btnSubmit.setEnabled(true);
                                processLearn();
                            }
                        }.start();
                    }
                    else
                    {
                        processLearn();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
    public void alertDialogComplete(String msg) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Tiếp tục",
                (dialog, id) -> startActivity(new Intent(TestEnglishActivity.this,UserInterfaceActivity.class)));
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}