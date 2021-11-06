package com.example.EnglishBeginner.Admin.DAO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.EnglishBeginner.Admin.Adapter.TopicAdapter;
import com.example.EnglishBeginner.Admin.DTO.Question;
import com.example.EnglishBeginner.Admin.DTO.Topic;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class DAOTopic {
    private final List<Topic> topicList;
    private final DatabaseReference databaseReference;
    private final Context context;

    public List<Topic> getTopicList() {
        return topicList;
    }

    public DAOTopic(Context context) {
        this.context = context;
        topicList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("listtopic");
    }

    public void getDataFromRealTimeFirebase(TopicAdapter topicAdapter) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (topicList != null) {
                    topicList.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Topic topic = dataSnapshot.getValue(Topic.class);
                    assert topicList != null;
                    topicList.add(topic);
                }
                if (topicAdapter != null) {
                    topicAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Get list Topic failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addDataToFireBase(Topic topic, EditText edtTopic) {
        boolean[] check = new boolean[2];
        Arrays.fill(check, true);
        if (topic.getNameTopic().isEmpty() || topic.getNameTopic().length() == 0) {
            check[0] = false;
        } else {
            if (topicList.size() > 0) {
                for (Topic topic1 : topicList) {
                    if (topic.getNameTopic().equalsIgnoreCase(topic1.getNameTopic()) && topic1.getLevel() == topic.getLevel()) {
                        check[1] = false;
                        break;
                    }
                }
            }
        }
        if (!check[0]) {
            edtTopic.setError("Không để trống");
            edtTopic.requestFocus();
        } else if (!check[1]) {
            edtTopic.setError("Trùng dữ liệu , hãy kiểm tra lại dữ liệu");
            edtTopic.requestFocus();
        } else {
            databaseReference.child(String.valueOf(topic.getId())).setValue(topic).addOnCompleteListener(task -> {
                if (task.isComplete()) {
                    Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void editDataToFireBase(Topic topic, EditText edtTopic) {
        boolean[] check = new boolean[2];
        Arrays.fill(check, true);
        if (topic.getNameTopic().isEmpty() || topic.getNameTopic().length() == 0) {
            check[0] = false;
        } else {
            if (topicList.size() > 0) {
                for (Topic topic1 : topicList) {
                    if ((topic.getNameTopic().equalsIgnoreCase(topic1.getNameTopic()))
                            && topic.getIdLevel() == topic1.getIdLevel()) {
                        check[1] = false;
                        break;
                    }
                }
            }
        }
        if (!check[0]) {
            edtTopic.setError("Không để trống");
            edtTopic.requestFocus();
        } else if (!check[1]) {
            edtTopic.setError("Trùng dữ liệu , hãy kiểm tra lại dữ liệu");
            edtTopic.requestFocus();
        } else {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("idTopic", topic.getId());
            hashMap.put("nameTopic", topic.getNameTopic());
            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("listquestion");
            databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d("parent", snapshot.getKey());
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Log.d("child", dataSnapshot.getKey());
                        Question question = dataSnapshot.getValue(Question.class);
                        assert question != null;
                        if (question.getIdTopic() == topic.getId()) {
                            databaseReference1.child(Objects.requireNonNull(dataSnapshot.getKey())).updateChildren(hashMap).isComplete();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        databaseReference.child(String.valueOf(topic.getId())).setValue(topic).addOnCompleteListener(task -> {
            if (task.isComplete()) {
                Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void deleteDataToFire(Topic topic) {
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("listquestion");
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("parent", snapshot.getKey());
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.d("child", dataSnapshot.getKey());
                    Question question = dataSnapshot.getValue(Question.class);
                    assert question != null;
                    if (question.getIdTopic() == topic.getId()) {
                        databaseReference1.child(Objects.requireNonNull(dataSnapshot.getKey())).removeValue().isComplete();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child(String.valueOf(topic.getId())).removeValue((error, ref) -> Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show());
    }
}
