package com.example.EnglishBeginner.Admin.LearnManagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.EnglishBeginner.Admin.DAO.DAOTopic;
import com.example.EnglishBeginner.Admin.DAO.DAOTypeQuestion;
import com.example.EnglishBeginner.Admin.DTO.Question;
import com.example.EnglishBeginner.R;

public class QuestionInterface extends AppCompatActivity {
    public DAOTypeQuestion daoTypeQuestion;
    public DAOTopic daoTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_interface);
        initUI();
    }
    private void initUI() {
        transactionFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        daoTopic = new DAOTopic(this);
        daoTypeQuestion = new DAOTypeQuestion(this);
        daoTopic.getDataFromRealTimeFirebase(null);
        daoTypeQuestion.getDataFromRealTimeToList(null);
    }

    private void transactionFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fmQuestion,new QuestionManagementFragment()).commit();
    }

    public void goToDetailQuestionFragment(Question question) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DetailQuestionFragment detailQuestionFragment = new DetailQuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_question", question);

        detailQuestionFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fmQuestion, detailQuestionFragment, null);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}