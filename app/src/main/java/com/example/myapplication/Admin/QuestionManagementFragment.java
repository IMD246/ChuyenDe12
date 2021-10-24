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

import com.example.myapplication.Admin.LearnManagement.Adapter.QuestionAdapter;
import com.example.myapplication.Admin.LearnManagement.Adapter.TopicSpinnerAdapter;
import com.example.myapplication.Admin.LearnManagement.Adapter.TypeQuestionSpinnerAdapter;
import com.example.myapplication.Admin.LearnManagement.DAO.DAOQuestion;
import com.example.myapplication.Admin.LearnManagement.DAO.DAOTopic;
import com.example.myapplication.Admin.LearnManagement.DAO.DAOTypeQuestion;
import com.example.myapplication.Admin.LearnManagement.DTO.Question;
import com.example.myapplication.DEFAULTVALUE;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


public class QuestionManagementFragment extends Fragment implements View.OnClickListener{

    private RecyclerView rcvQuestion;
    private DAOQuestion daoQuestion;
    private DAOTypeQuestion daoTypeQuestion;
    private DAOTopic daoTopic;
    private ImageView imgAdd;
    private SearchView svQuestion;
    private List<String>listQuestion;
    private AutoCompleteTextView atcTopic,atcTypeQuestion;
    String topic = DEFAULTVALUE.TOPIC,typeQuestion = DEFAULTVALUE.TYPEQUESTION;
    private QuestionAdapter questionAdapter;
    public QuestionManagementFragment() {
        daoTopic = new DAOTopic(getContext());
        daoQuestion = new DAOQuestion(getContext());
        daoTypeQuestion = new DAOTypeQuestion(getContext());
        questionAdapter = new QuestionAdapter(getContext());
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_question_management,container,false);
        initUI(v);
        getDataFromRealTime();
        return v;
    }
    private void initUI(View v) {
        rcvQuestion = v.findViewById(R.id.rcvQuestion);
        atcTopic = v.findViewById(R.id.atcQuestion_Topic);
        svQuestion = v.findViewById(R.id.svQuestion);
        imgAdd = v.findViewById(R.id.imgAddQuestion);
        imgAdd.setOnClickListener(this);
        atcTypeQuestion = v.findViewById(R.id.atcQuestion_TypeQuestion);
        atcTypeQuestion.setAdapter(new TypeQuestionSpinnerAdapter(getContext(),R.layout.listoptionitem,
                R.id.tvOptionItem,daoTypeQuestion.getTypeQuestionList()));
        atcTopic.setAdapter(new TopicSpinnerAdapter(getContext(),R.layout.listoptionitem,R.id.tvOptionItem,daoTopic.getTopicList()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(rcvQuestion.VERTICAL);
        rcvQuestion.setLayoutManager(linearLayoutManager);
        questionAdapter.setQuestionList(daoQuestion.getQuestionList());
        rcvQuestion.setAdapter(questionAdapter);
        questionAdapter.setMyDelegationLevel(new QuestionAdapter.MyDelegationLevel() {
            @Override
            public void editItem(Question question) {
            }

            @Override
            public void deleteItem(Question question) {

            }
        });
        svQuestion.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                questionAdapter.getFilter().filter(newText);
                return false;
            }
        });
        atcTypeQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                typeQuestion = atcTypeQuestion.getText().toString();
                questionAdapter.setListDependOnTopicAndTypeQuestion(topic,typeQuestion);
            }
        });
        atcTopic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                topic = atcTopic.getText().toString();
                questionAdapter.setListDependOnTopicAndTypeQuestion(topic,typeQuestion);
            }
        });
    }
    private void getDataFromRealTime()
    {
        daoQuestion.getDataFromRealTimeToList(questionAdapter);
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
                Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }
}