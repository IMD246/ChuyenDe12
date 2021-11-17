package com.example.EnglishBeginner.DAO;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.EnglishBeginner.Adapter.TestChooseImageItem_Adapter;
import com.example.EnglishBeginner.Adapter.UserChooseEnglishAdapter;
import com.example.EnglishBeginner.Adapter.UserHadChooseEnglishAdapter;
import com.example.EnglishBeginner.DTO.Answer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DAOAnswer {
    //Fields
    private DatabaseReference databaseReference;
    private List<Answer> answerList;
    private Context context;
    //Constructor
    public DAOAnswer(Context context) {
        this.context = context;
        // lấy đường dẫn của cây dữ liệu firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("listquestion");
        answerList = new ArrayList<>();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Answer> getAnswerList() {
        return answerList;
    }
    // lấy dữ liệu từ firebase dựa theo id của Question
    public void getDataFromFirebase(int idQuestion, TestChooseImageItem_Adapter testChooseImageItem_adapter) {
        databaseReference.child(idQuestion+"/listanswer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (answerList != null)
                {
                    answerList.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Answer answer = dataSnapshot.getValue(Answer.class);
                    answerList.add(answer);
                }
                if (testChooseImageItem_adapter!=null)
                {
                    testChooseImageItem_adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
