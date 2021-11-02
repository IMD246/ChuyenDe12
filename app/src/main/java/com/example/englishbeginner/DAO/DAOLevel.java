package com.example.englishbeginner.DAO;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.englishbeginner.Adapter.Level_Adapter;
import com.example.englishbeginner.DTO.Level;
import com.example.englishbeginner.DTO.Topic;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DAOLevel {
    private List<Level> levelList;
    private DatabaseReference databaseReference;
    private List<Topic>topicList = new ArrayList<>();
    public List<Level> getLevelList() {
        return levelList;
    }

    private Context context;

    public DAOLevel(Context context) {
        this.context = context;
        levelList = new ArrayList<>();
        topicList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("listlevel");
    }

    public void setTopicList(List<Topic> topicList) {
        this.topicList = topicList;
    }

    public void getDataFromRealTimeToList(Level_Adapter level_adapter) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (levelList != null) {
                    levelList.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Level level = dataSnapshot.getValue(Level.class);
                    levelList.add(level);
                }
                if (level_adapter != null)
                {
                    level_adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Get list Level failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
