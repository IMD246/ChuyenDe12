package com.example.myapplication.Admin.LearnManagement.DAO;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.myapplication.Admin.LearnManagement.DTO.Answer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DAOAnswer {
    private DatabaseReference databaseReference;
    private List<Answer>answerList;
    private Context context;
    public DAOAnswer(Context context) {
        this.context = context;
        databaseReference = FirebaseDatabase.getInstance().getReference("listquestion");
        answerList = new ArrayList<>();
    }

    public List<Answer> getAnswerList() {
        return answerList;
    }

    public void getDataFromFirebase(int idQuestion)
    {
        databaseReference.child(String.valueOf(idQuestion)).child("listanswer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Answer answer = dataSnapshot.getValue(Answer.class);
                    answerList.add(answer);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
