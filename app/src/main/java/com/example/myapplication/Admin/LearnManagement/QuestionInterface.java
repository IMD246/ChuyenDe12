package com.example.myapplication.Admin.LearnManagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.myapplication.Admin.LearnManagement.QuestionManagementFragment;
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
}