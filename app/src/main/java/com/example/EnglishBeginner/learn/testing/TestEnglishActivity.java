package com.example.EnglishBeginner.learn.testing;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.EnglishBeginner.DAO.DAOProcessUser;
import com.example.EnglishBeginner.DTO.DEFAULTVALUE;
import com.example.EnglishBeginner.DTO.ProcessTopicItem;
import com.example.EnglishBeginner.DTO.Question;
import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.learn.FinishEnglishFragment;
import com.example.EnglishBeginner.learn.learning.LearningEnglishFragment;
import com.example.EnglishBeginner.main_interface.UserInterfaceActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class TestEnglishActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressBar progressBar;
    @SuppressLint("StaticFieldLeak")
    public static Button btnPass, btnSubmit;
    private List<Question> arrayListQuestion;
    private int count = 0,countcorrect = 0;
    private int max = 0;
    public String correctQuestion = null, answer = null;
    private String typeLearn = null;
    private int idTopic;
    private String userID;
    private DAOProcessUser daoProcessUser;
    private List<ProcessTopicItem>processTopicItemList;
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_english);
        daoProcessUser = new DAOProcessUser(this);
        Intent intent = getIntent();
        if (intent != null) {
            arrayListQuestion = (List<Question>) intent.getSerializableExtra("listQuestion");
            typeLearn = intent.getStringExtra("learn");
            idTopic = intent.getIntExtra("idTopic",-1);
            userID = intent.getStringExtra("userID");
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
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (arrayListQuestion.size() < 10) {
            progressBar.setMax(arrayListQuestion.size());
            if (count < arrayListQuestion.size()) {
                transactionFragment(question);
            }
            else
            {
                if (countcorrect >= arrayListQuestion.size())
                {
                    daoProcessUser.updateProcess(userID,idTopic);
                }
                if (typeLearn.equalsIgnoreCase(DEFAULTVALUE.TEST)) {
                    fragmentTransaction.replace(R.id.frameLayout_Fragment, new FinishEnglishFragment()).commit();
                }
                else if (typeLearn.equalsIgnoreCase(DEFAULTVALUE.LEARN)) {
                    fragmentTransaction.replace(R.id.frameLayout_Fragment, new FinishEnglishFragment()).commit();
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
                if (countcorrect >= 8) {
                    daoProcessUser.updateProcess(userID,idTopic);
                }
                if (typeLearn.equalsIgnoreCase(DEFAULTVALUE.TEST)) {
                    fragmentTransaction.replace(R.id.frameLayout_Fragment, new FinishEnglishFragment()).commit();
                } else if (typeLearn.equalsIgnoreCase(DEFAULTVALUE.LEARN)) {
                    fragmentTransaction.replace(R.id.frameLayout_Fragment, new FinishEnglishFragment()).commit();
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_continute) {
            submitResult();
        }
    }
    private void submitResult() {
        if (count < arrayListQuestion.size() && count<10) {
            if (typeLearn.equalsIgnoreCase(DEFAULTVALUE.TEST)) {
                if (answer == null) {
                    alertDialog("Không chính xác", false);
                } else {
                    if (answer.equalsIgnoreCase(correctQuestion)) {
                        alertDialog("Chính xác", true);
                        countcorrect++;
                    } else {
                        alertDialog("Không chính xác", false);
                    }
                    answer = null;
                }
            } else if (typeLearn.equalsIgnoreCase(DEFAULTVALUE.LEARN)) {
                countcorrect++;
                alertDialog("Đã hoàn thành phần học", true);
            }
        }
        else {
            startActivity(new Intent(TestEnglishActivity.this, UserInterfaceActivity.class));
        }
        count++;
        progressBar.setProgress(count,true);
    }
    // Xây dựng một Hộp thoại thông báo
    @SuppressLint("UseCompatLoadingForDrawables")
    public void alertDialog(String msg, Boolean check) {
        Dialog dialog = new Dialog(TestEnglishActivity.this);
        dialog.setContentView(R.layout.layout_notify_answer_correct);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        window.setAttributes(windowAttributes);
        RelativeLayout relativeLayout = dialog.findViewById(R.id.rltCheckAnswer);
        GifImageView imgResult = dialog.findViewById(R.id.imgResult);
        @SuppressLint("CutPasteId") TextView tvtitle = dialog.findViewById(R.id.id_corect_title);
        Button btnContinute = dialog.findViewById(R.id.btn_continute_notify);
        if (!check) {
            imgResult.setImageResource(R.drawable.incorrect);
            relativeLayout.setBackgroundResource(R.color.red_incorrect);
            tvtitle.setTextColor(Color.parseColor("#ea2b2b"));
            btnContinute.setBackground(getResources().getDrawable(R.drawable.ct_layout_button4, null));
        }
        else
        {
            imgResult.setImageResource(R.drawable.correct);
            relativeLayout.setBackgroundResource(R.color.greenResult);
            tvtitle.setTextColor(Color.parseColor("#58a700"));
            btnContinute.setBackground(getResources().getDrawable(R.drawable.ct_layout_button3, null));
        }
        @SuppressLint("CutPasteId") TextView tvCorrect = dialog.findViewById(R.id.id_corect_title);
        tvCorrect.setText(msg);
        @SuppressLint("CutPasteId") Button btnContinue = dialog.findViewById(R.id.btn_continute_notify);
        btnContinue.setOnClickListener(v -> {
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
            dialog.dismiss();
        });
        dialog.show();
    }
}