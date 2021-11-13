package com.example.EnglishBeginner.DAO;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.EnglishBeginner.Adapter.ProcessTopic_Adapter;
import com.example.EnglishBeginner.DTO.ProcessTopicItem;
import com.example.EnglishBeginner.DTO.Topic;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DAOProcessUser {
    private DatabaseReference databaseReference;
    private Context context;
    public static String path;
    public DAOProcessUser(Context context) {
        this.context = context;
        databaseReference = FirebaseDatabase.getInstance().getReference("listProcessUser");
    }
    public void getDataFromRealTimeFirebase(String uid, int idTopic, List<ProcessTopicItem>processTopicItemList, ProcessTopic_Adapter processTopic_adapter, TextView tvTitle,TextView tvLevel)
    {
        databaseReference.child(uid).child("listTopic/"+idTopic+"/listProcess").
                addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (processTopicItemList!=null)
                {
                    processTopicItemList.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    ProcessTopicItem processTopicItem = dataSnapshot.getValue(ProcessTopicItem.class);
                    processTopicItemList.add(processTopicItem);
                }
                if (processTopic_adapter!=null)
                {
                    processTopic_adapter.notifyDataSetChanged();
                }
                if (processTopicItemList.get(processTopicItemList.size()-1).getProgress()==2)
                {
                    tvLevel.setText("Level: Huyền thoại");
                    tvTitle.setText("Bạn đã thông thạo kỹ năng này!");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Get list Process Topic failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
