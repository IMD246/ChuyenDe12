package com.example.myapplication.Admin.LearnManagement.DAO;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.Admin.LearnManagement.Adapter.LevelAdapter;
import com.example.myapplication.Admin.LearnManagement.Adapter.QuestionAdapter;
import com.example.myapplication.Admin.LearnManagement.DTO.Level;
import com.example.myapplication.Admin.LearnManagement.DTO.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DAOQuestion {
    private List<Question> questionList;
    private DatabaseReference databaseReference;
    private Context context;

    public DAOQuestion(Context context) {
        questionList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("listquestion");
        this.context = context;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void getDataFromRealTimeToList(QuestionAdapter questionAdapter) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (questionList != null) {
                    questionList.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Question question = dataSnapshot.getValue(Question.class);
                    questionList.add(question);
                }
                if (questionAdapter != null)
                {
                    questionAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Get list question failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
