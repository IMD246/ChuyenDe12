package com.example.menu_right.DAO;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.menu_right.Adapter.Topic_Adapter;
import com.example.menu_right.DTO.Topic;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DAOTopic {
    private List<Topic> topicList;
    private DatabaseReference databaseReference;
    private Context context;
    public List<Topic> getTopicList() {
        return topicList;
    }
    private DAOQuestion daoQuestion;
    public DAOTopic(Context context) {
        this.context = context;
        topicList = new ArrayList<>();
        daoQuestion = new DAOQuestion(context);
        databaseReference = FirebaseDatabase.getInstance().getReference("listtopic");
    }
    public void getDataFromRealTimeFirebase()
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (topicList!=null)
                {
                    topicList.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Topic topic = dataSnapshot.getValue(Topic.class);
                    topicList.add(topic);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Get list Topic failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
