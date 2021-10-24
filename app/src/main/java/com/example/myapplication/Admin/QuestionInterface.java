package com.example.myapplication.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.myapplication.Admin.LearnManagement.Adapter.QuestionAdapter;
import com.example.myapplication.Admin.LearnManagement.DTO.Question;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

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