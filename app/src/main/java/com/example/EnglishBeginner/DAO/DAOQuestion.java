package com.example.EnglishBeginner.DAO;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.EnglishBeginner.DTO.Question;
import com.example.EnglishBeginner.DTO.Topic;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DAOQuestion implements Serializable {
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

    public void setContext(Context context) {
        this.context = context;
    }

    public void getDataFromRealTimeToList(Topic topic) {
        databaseReference.orderByChild("idTopic").equalTo(topic.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (questionList != null) {
                    questionList.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Question question = dataSnapshot.getValue(Question.class);
                    questionList.add(question);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Get list question failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
