package com.example.myapplication.Admin.LearnManagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.android.installreferrer.BuildConfig;
import com.example.myapplication.Admin.LearnManagement.DTO.Question;
import com.example.myapplication.R;

public class QuestionInterface extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_interface);
        initUI();
    }

    private void initUI() {
        transactionFragment();
    }
    private void transactionFragment()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fmQuestion,new QuestionManagementFragment()).commit();
    }
    public void goToDetailQuestionFragment(Question question)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DetailQuestionFragment detailQuestionFragment = new DetailQuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_question",question);

        detailQuestionFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fmQuestion,detailQuestionFragment,null);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}