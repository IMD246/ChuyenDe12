package com.example.EnglishBeginner.Admin.DAO;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.EnglishBeginner.Admin.DTO.Topic;
import com.example.EnglishBeginner.Admin.Adapter.TopicAdapter;
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

    public void getDataFromRealTimeFirebase(TopicAdapter topicAdapter) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (topicList != null) {
                    topicList.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Topic topic = dataSnapshot.getValue(Topic.class);
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
        int s = 1;
        for (int i = 0; i < check.length; i++) {
            check[i] = true;
        }
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
        if (check[0] == false) {
            edtTopic.setError("Không để trống");
            edtTopic.requestFocus();
        } else if (check[1] == false) {
            edtTopic.setError("Trùng dữ liệu , hãy kiểm tra lại dữ liệu");
            edtTopic.requestFocus();
        } else {
            databaseReference.child(String.valueOf(topic.getId())).setValue(topic).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isComplete()) {
                        Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public void addTopicToFireBaseLevel(Topic topic, EditText edtTopic) {
        boolean[] check = new boolean[2];
        int s = 1;
        for (int i = 0; i < check.length; i++) {
            check[i] = true;
        }
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
        if (check[0] == false) {
            edtTopic.setError("Không để trống");
            edtTopic.requestFocus();
        } else if (check[1] == false) {
            edtTopic.setError("Trùng dữ liệu , hãy kiểm tra lại dữ liệu");
            edtTopic.requestFocus();
        } else {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("listlevel");
            databaseReference.child(String.valueOf(topic.getIdLevel()) + "/listtopic").child(String.valueOf(topic.getId())).setValue(topic).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(context, "Thêm Topic vào Level thành công", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public void editDataToFireBase(Topic topic, EditText edtTopic) {
        boolean[] check = new boolean[2];
        int s = 1;
        for (int i = 0; i < check.length; i++) {
            check[i] = true;
        }
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
        if (check[0] == false) {
            edtTopic.setError("Không để trống");
            edtTopic.requestFocus();
        } else if (check[1] == false) {
            edtTopic.setError("Trùng dữ liệu , hãy kiểm tra lại dữ liệu");
            edtTopic.requestFocus();
        } else {
            databaseReference.child(String.valueOf(topic.getId())).setValue(topic).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isComplete()) {
                        Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public void deleteDataToFire(Topic topic) {
        databaseReference.child(String.valueOf(topic.getId())).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
