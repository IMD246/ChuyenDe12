package com.example.EnglishBeginner.learn.testing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EnglishBeginner.Adapter.UserChooseEnglishAdapter;
import com.example.EnglishBeginner.Adapter.UserHadChooseEnglishAdapter;
import com.example.EnglishBeginner.DAO.DAOAnswer;
import com.example.EnglishBeginner.DTO.Answer;
import com.example.EnglishBeginner.DTO.Question;
import com.example.EnglishBeginner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class TestSelectionEnglishFragment extends Fragment {
    //khai báo
    private View myView;

    private ImageView imgSpeak;
    private TextView tvQuestion;
    private RecyclerView rv_listForUserToChoose, rv_listThatUserHadChoose;
    private ArrayList<String> listUserHadChoose;
    private UserChooseEnglishAdapter userChooseEnglishAdapter;
    private UserHadChooseEnglishAdapter userHadChooseEnglishAdapter;
    private Question question;
    private DAOAnswer daoAnswer;
    private TestEnglishActivity testEnglishActivity;
    private DatabaseReference databaseReference;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_fragment_test_selection_english, container, false);
        setControl();
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            question = (Question) bundle.get("question");
            tvQuestion.setText(question.getTitle());
            getDataAnswer(question.getId());
        }
        setAdapter();
        return myView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void NotifyData(){
        userHadChooseEnglishAdapter.notifyDataSetChanged();
        userChooseEnglishAdapter.notifyDataSetChanged();
    }
    private void setAdapter(){
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);// Tạo layout manager
        layoutManager.canScrollHorizontally();
        layoutManager.canScrollVertically();
        rv_listForUserToChoose.setLayoutManager(layoutManager);// Gán layout manager cho recyclerview
        //set adapter for list user had choose
        RecyclerView.LayoutManager layoutManager2 = new GridLayoutManager(getContext(), 3);// Tạo layout manager
        layoutManager2.canScrollHorizontally();
        layoutManager2.canScrollVertically();
        rv_listThatUserHadChoose.setLayoutManager(layoutManager2);// Gán layout manager cho recyclerview
    }
    //Ánh xạ, khởi tạo,...
    private void setControl() {
        testEnglishActivity =(TestEnglishActivity) getActivity();
        daoAnswer = new DAOAnswer(testEnglishActivity.getBaseContext());
        listUserHadChoose = new ArrayList<>();
        //ánh xạ các view
        imgSpeak = myView.findViewById(R.id.img_listen);
        tvQuestion = myView.findViewById(R.id.tv_question);
        rv_listForUserToChoose = myView.findViewById(R.id.recycle_view_button_answer);
        rv_listThatUserHadChoose = myView.findViewById(R.id.recycle_view_show_answer);
    }
    private void getDataAnswer(int idQuestion)
    {
        databaseReference = FirebaseDatabase.getInstance().getReference("listquestion");
        databaseReference.child(idQuestion+"/listanswer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String answerString = "";
                List<Answer> list = new ArrayList<>();
                ArrayList<String> arrayList = new ArrayList<>();
                if (list != null)
                {
                    list.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Answer answer = dataSnapshot.getValue(Answer.class);
                    list.add(answer);
                }
                for (Answer temp: list) {
                    answerString = answerString + temp.getAnswerQuestion()+ " ";
                }
                String [] temp = answerString.split(" ");
                for (String item: temp) {
                    arrayList.add(item);
                }
                if (userChooseEnglishAdapter!=null)
                {
                    userChooseEnglishAdapter.notifyDataSetChanged();
                }
                userChooseEnglishAdapter = new UserChooseEnglishAdapter(getContext(),listUserHadChoose,arrayList);
                userHadChooseEnglishAdapter = new UserHadChooseEnglishAdapter(getContext(), listUserHadChoose, arrayList);
                //gán adapter cho Recyclerview.
                rv_listForUserToChoose.setAdapter(userChooseEnglishAdapter);
                rv_listThatUserHadChoose.setAdapter(userHadChooseEnglishAdapter);//gán adapter cho Recyclerview.
                userHadChooseEnglishAdapter.setNotifyData(new UserHadChooseEnglishAdapter.notifyData() {
                    @Override
                    public void notifyDataChange() {
                        NotifyData();
                    }
                });
                userChooseEnglishAdapter.setNotifyData(new UserChooseEnglishAdapter.notifyData() {
                    @Override
                    public void notifyDataChange() {
                        NotifyData();
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}