package com.example.EnglishBeginner.DAO;

import android.content.Context;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.EnglishBeginner.Adapter.ProcessTopic_Adapter;
import com.example.EnglishBeginner.DTO.ProcessTopicItem;
import com.example.EnglishBeginner.R;
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
    private List<ProcessTopicItem> processTopicItem1;

    public DAOProcessUser(Context context) {
        this.context = context;
        processTopicItem1 = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("listProcessUser");
    }

    public void getDataFromRealTimeFirebase(String uid, int idTopic, List<ProcessTopicItem> processTopicItemList,
                                            ProcessTopic_Adapter processTopic_adapter, TextView tvTitle,
                                            TextView tvLevel, Button btnTest, RelativeLayout relativeLayout) {
        databaseReference.child(uid).child("listTopic/" + idTopic + "/listProcess").
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (processTopicItemList != null) {
                            processTopicItemList.clear();
                        }
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            ProcessTopicItem processTopicItem = dataSnapshot.getValue(ProcessTopicItem.class);
                            processTopicItemList.add(processTopicItem);
                        }
                        if (processTopic_adapter != null) {
                            processTopic_adapter.notifyDataSetChanged();
                        }
                        if (processTopicItemList.get(processTopicItemList.size() - 1).getProgress() == 2) {
                            if (tvLevel != null && tvTitle != null) {
                                tvLevel.setText("Level: Huyền thoại");
                                tvTitle.setText("Bạn đã thông thạo kỹ năng này!");
                                btnTest.setText("Luyện tập");
                                relativeLayout.setBackgroundResource(R.drawable.ct_layout_popup_dialog1);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Get list Process Topic failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void updateProcess(String uid, int idTopic) {
        databaseReference.child(uid).child("listTopic/" + idTopic + "/listProcess").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (processTopicItem1 != null) {
                            processTopicItem1.clear();
                        }
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            ProcessTopicItem processTopicItem = dataSnapshot.getValue(ProcessTopicItem.class);
                            processTopicItem1.add(processTopicItem);
                        }
                        ProcessTopicItem processTopicItem = new ProcessTopicItem();
                        for (int i = 0; i < processTopicItem1.size(); i++) {
                            if (processTopicItem1.get(i).getProgress() < 2) {
                                processTopicItem.setProcess(processTopicItem1.get(i).getProcess());
                                processTopicItem.setIdTopic(processTopicItem1.get(i).getIdTopic());
                                processTopicItem.setProgress(processTopicItem1.get(i).getProgress());
                                break;
                            }
                        }
                        if (processTopicItem.getProcess()>0 && processTopicItem.getIdTopic()>0) {
                            if (processTopicItem.getProgress() < 2) {
                                databaseReference.child(uid).child("listTopic/" + idTopic + "/listProcess/" + processTopicItem.getProcess() + "/progress").setValue(processTopicItem.getProgress() + 1).addOnSuccessListener(unused -> {
                                });
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Get list Process Topic failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
