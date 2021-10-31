package com.example.myapplication.Admin.LearnManagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.Admin.DAO.DAOTopic;
import com.example.myapplication.Admin.DAO.DAOTypeQuestion;
import com.example.myapplication.Admin.DTO.Question;
import com.example.myapplication.R;

public class QuestionInterface extends AppCompatActivity {
    private String TAGQuestionManagement = QuestionManagementFragment.class.getName();
    public DAOTypeQuestion daoTypeQuestion;
    public boolean flag = false;
    public DAOTopic daoTopic;
    public Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_interface);
        Log.d("test", "oncreate ");
//        initUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        Log.d("test", "onStart ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("test", "onStop ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("test", "onDestroy ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        daoTypeQuestion = new DAOTypeQuestion(this);
        daoTypeQuestion.getDataFromRealTimeToList(null);
        daoTopic = new DAOTopic(this);
        daoTopic.getDataFromRealTimeFirebase(null);
        initUI();
        Log.d("test", "onResume ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("test", "onpause ");
    }

    private void initUI() {
        transactionFragment();
    }

    private void transactionFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        QuestionManagementFragment questionManagementFragmen = getSupportFragmentManager().popBackStack;
        if (getSupportFragmentManager().findFragmentByTag(TAGQuestionManagement) == null) {
            QuestionManagementFragment questionManagementFragment = new QuestionManagementFragment();

            fragmentTransaction.replace(R.id.fmQuestion, questionManagementFragment, TAGQuestionManagement).addToBackStack(null).commit();
        } else {
            QuestionManagementFragment questionManagementFragment = (QuestionManagementFragment) getSupportFragmentManager().findFragmentByTag(TAGQuestionManagement);
            fragmentTransaction.replace(R.id.fmQuestion, questionManagementFragment).commit();
        }
    }

    public void goToDetailQuestionFragment(Question question) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DetailQuestionFragment detailQuestionFragment = new DetailQuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_question", question);

        detailQuestionFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fmQuestion, detailQuestionFragment, null);
        fragmentTransaction.commit();
    }

    public void openFileChoose()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data !=null && data.getData()!=null)
        {
            uri = data.getData();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (flag) {
            transactionFragment();
            flag = false;
        }
        else
        {
            finish();
        }
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}