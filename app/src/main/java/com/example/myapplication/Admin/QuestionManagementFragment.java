package com.example.myapplication.Admin;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.myapplication.Admin.LearnManagement.Adapter.LevelAdapter;
import com.example.myapplication.Admin.LearnManagement.Adapter.LevelSpinnerAdapter;
import com.example.myapplication.Admin.LearnManagement.Adapter.QuestionAdapter;
import com.example.myapplication.Admin.LearnManagement.Adapter.TopicSpinnerAdapter;
import com.example.myapplication.Admin.LearnManagement.Adapter.TypeQuestionSpinnerAdapter;
import com.example.myapplication.Admin.LearnManagement.DAO.DAOQuestion;
import com.example.myapplication.Admin.LearnManagement.DAO.DAOTopic;
import com.example.myapplication.Admin.LearnManagement.DAO.DAOTypeQuestion;
import com.example.myapplication.Admin.LearnManagement.DTO.Level;
import com.example.myapplication.Admin.LearnManagement.DTO.Question;
import com.example.myapplication.Admin.LearnManagement.DTO.TypeQuestion;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class QuestionManagementFragment extends Fragment implements View.OnClickListener{

    private RecyclerView rcvQuestion;
    private DAOQuestion daoQuestion;
    private DAOTypeQuestion daoTypeQuestion;
    private DAOTopic daoTopic;
    private Context context;
    private View v;
    private ImageView imgAdd;
    private SearchView svQuestion;
    private List<String>listQuestion;
    private AutoCompleteTextView atcTopic,atcTypeQuestion,atcQuestion;
    private QuestionAdapter questionAdapter;
    public QuestionManagementFragment(Context context) {
        this.context = context;
        daoTopic = new DAOTopic(context);
        daoQuestion = new DAOQuestion(context);
        daoTypeQuestion = new DAOTypeQuestion(context);
        questionAdapter = new QuestionAdapter(context);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = LayoutInflater.from(context).inflate(R.layout.fragment_question_management,container,false);
        initUI();
        getDataFromRealTime();
        return v;
    }
    private void initUI() {
        atcTopic = v.findViewById(R.id.atcQuestion_Topic);
        imgAdd = v.findViewById(R.id.imgAddQuestion);
        imgAdd.setOnClickListener(this);
        atcTypeQuestion = v.findViewById(R.id.atcQuestion_TypeQuestion);
        atcQuestion = v.findViewById(R.id.atcQuestion);
        rcvQuestion = v.findViewById(R.id.rcvQuestion);
        atcTopic.setAdapter(new TopicSpinnerAdapter(context,R.layout.listoptionitem,R.id.tvOptionItem,daoTopic.getTopicList()));
        atcTypeQuestion.setAdapter(new TypeQuestionSpinnerAdapter(context,R.layout.listoptionitem,
                R.id.tvOptionItem,daoTypeQuestion.getTypeQuestionList()));
        atcQuestion.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,listQuestion));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvQuestion.setLayoutManager(linearLayoutManager);
        questionAdapter.setQuestionList(daoQuestion.getQuestionList());
        rcvQuestion.setAdapter(questionAdapter);
        questionAdapter.setMydelegation(new QuestionAdapter.Mydelegation() {
            @Override
            public void deleteItem(Question question) {

            }

            @Override
            public void editItem(Question question) {

            }
        });
        atcQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (atcQuestion.getText().toString().isEmpty())
                {
                    questionAdapter.setQuestionList(daoQuestion.getQuestionList());
                }
                else {
                    questionAdapter.getFilter().filter(atcQuestion.getText().toString());
                }
            }
        });

    }
    private void getDataFromRealTime()
    {
        daoQuestion.getDataFromRealTimeToList(questionAdapter);
        listQuestion = new ArrayList<>();
        if (daoQuestion.getQuestionList().size()>0)
        {
            for (Question question : daoQuestion.getQuestionList())
            {
                listQuestion.add(question.getTitle());
            }
        }
        daoTopic.getDataFromRealTimeFirebase(null);
        daoTypeQuestion.getDataFromRealTimeToList(null);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imgAddQuestion:addData();
        }
    }

    private void addData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("listquestion");
        Question question = new Question(1,1,1,"... Topic","Family","Write","A");
        databaseReference.child(String.valueOf(1)).setValue(question).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }
}