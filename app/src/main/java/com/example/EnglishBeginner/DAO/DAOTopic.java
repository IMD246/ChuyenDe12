package com.example.EnglishBeginner.DAO;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.EnglishBeginner.DTO.Topic;
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
    public DAOTopic(Context context) {
        this.context = context;
        topicList = new ArrayList<>();
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
