package com.example.EnglishBeginner.learn.testing;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EnglishBeginner.DAO.DAOAnswer;
import com.example.EnglishBeginner.DTO.Answer;
import com.example.EnglishBeginner.DTO.Question;
import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.learn.testing.source.UserChooseEnglishAdapter;
import com.example.EnglishBeginner.learn.testing.source.UserHadChooseEnglishAdapter;

import java.util.ArrayList;

public class TestSelectionEnglishFragment extends Fragment {
    //khai báo
    private View myView;

    private ImageView imgSpeak;
    private TextView tvQuestion;
    private RecyclerView rv_listForUserToChoose, rv_listThatUserHadChoose;
    private ArrayList<String> listForUserToChoose;
    private ArrayList<String> listUserHadChoose;
    UserChooseEnglishAdapter userChooseEnglishAdapter;
    UserHadChooseEnglishAdapter UserHadChooseEnglishAdapter;
    private Question question;
    private DAOAnswer daoAnswer;
    private TestEnglishActivity testEnglishActivity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_fragment_test_selection_english, container, false);
        setControl();
        setEvent();

        Bundle bundle = getArguments();

        setControl();
        if (bundle!=null)
        {
            question = (Question) bundle.get("question");
            getDataAnswer(question.getId());
            tvQuestion.setText(question.getTitle());

        }
        getDataAnswer(question.getId());
        Log.d("ubnax", "onCreateView: "+question.getId());

        return myView;
    }

    //xử lí màn hình learn
    private void setEvent() {



    }
    private void NotifyData(){
        userChooseEnglishAdapter.notifyDataSetChanged();
        UserHadChooseEnglishAdapter.notifyDataSetChanged();
    }
    private void setAdapter(){

        listUserHadChoose = new ArrayList<>();
        listForUserToChoose = new ArrayList<>();
        String answerString = "";
        if(daoAnswer.getAnswerList().size()>0){
            for (Answer temp:daoAnswer.getAnswerList()) {
                answerString += temp.getAnswerQuestion()+" ";
            }
            changeStringToArrayList(answerString,listForUserToChoose);
        }
        Log.d("asd1p2e", "setAdapter: "+daoAnswer.getAnswerList());

        //
        userChooseEnglishAdapter = new UserChooseEnglishAdapter(getContext(), listForUserToChoose, listUserHadChoose);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);// Tạo layout manager
        layoutManager.canScrollHorizontally();
        layoutManager.canScrollVertically();
        rv_listForUserToChoose.setLayoutManager(layoutManager);// Gán layout manager cho recyclerview
        rv_listForUserToChoose.setAdapter(userChooseEnglishAdapter);//gán adapter cho Recyclerview.
        //set adapter for list user had choose
        userChooseEnglishAdapter.setNotifyData(new UserChooseEnglishAdapter.notifyData() {
            @Override
            public void notifyDataChange() {
                NotifyData();
            }
        });

        UserHadChooseEnglishAdapter = new UserHadChooseEnglishAdapter(getContext(), listUserHadChoose, listForUserToChoose);
        RecyclerView.LayoutManager layoutManager2 = new GridLayoutManager(getContext(), 3);// Tạo layout manager
        layoutManager2.canScrollHorizontally();
        layoutManager2.canScrollVertically();
        rv_listThatUserHadChoose.setLayoutManager(layoutManager2);// Gán layout manager cho recyclerview
        rv_listThatUserHadChoose.setAdapter(UserHadChooseEnglishAdapter);//gán adapter cho Recyclerview.
        UserHadChooseEnglishAdapter.setNotifyData(new UserHadChooseEnglishAdapter.notifyData() {
            @Override
            public void notifyDataChange() {
                NotifyData();
            }
        });
    }
    //Ánh xạ, khởi tạo,...
    private void setControl() {
        testEnglishActivity =(TestEnglishActivity) getActivity();
        //ánh xạ các view
        daoAnswer = new DAOAnswer(getContext());
        imgSpeak = myView.findViewById(R.id.img_listen);
        tvQuestion = myView.findViewById(R.id.tv_question);
        rv_listForUserToChoose = myView.findViewById(R.id.recycle_view_button_answer);
        rv_listThatUserHadChoose = myView.findViewById(R.id.recycle_view_show_answer);
        setAdapter();

    }
    private void changeStringToArrayList(String text,ArrayList<String> listResult){
        String [] temp = text.split(" ");
        for (String item: temp) {
            listResult.add(item);
        }
    }
    private void getDataAnswer(int idQuestion)
    {
        daoAnswer.getDataFromFirebase(idQuestion,null);
    }
}