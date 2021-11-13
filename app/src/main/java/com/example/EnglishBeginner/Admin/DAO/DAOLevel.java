package com.example.EnglishBeginner.Admin.DAO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.EnglishBeginner.Admin.Adapter.LevelAdapter;
import com.example.EnglishBeginner.Admin.DTO.Level;
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

public class DAOLevel {
    private final List<Level> levelList;
    private final DatabaseReference databaseReference;

    public List<Level> getLevelList() {
        return levelList;
    }

    private final Context context;

    public DAOLevel(Context context) {
        this.context = context;
        levelList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("listlevel");
    }

    public void getDataFromRealTimeToList(LevelAdapter levelAdapter) {
        databaseReference.orderByChild("nameLevel").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (levelList != null) {
                    levelList.clear();
                }
                Log.d("parent",snapshot.getKey());
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.d("child",dataSnapshot.getKey());
                    Level level = dataSnapshot.getValue(Level.class);
                    assert levelList != null;
                    levelList.add(level);
                }
                if (levelAdapter != null) {
                    levelAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Get list Level failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addDataToFireBase(Level level, EditText editText) {
        boolean[] check = new boolean[2];
        Arrays.fill(check, true);
        if (level.getNameLevel() == 0) {
            check[0] = false;
        } else {
            if (levelList.size() > 0) {
                for (Level level1 : levelList) {
                    if (level.getNameLevel() == level1.getNameLevel()) {
                        check[1] = false;
                        break;
                    }
                }
            }
        }
        if (!check[0]) {
            editText.setError("Không để trống");
            editText.requestFocus();
        } else if (!check[1]) {
            editText.setError("Trùng dữ liệu");
            editText.requestFocus();
        } else {
            levelList.size();
            databaseReference.child(String.valueOf(level.getId())).setValue(level).addOnCompleteListener(task -> {
                if (task.isComplete()) {
                    Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void editDataToFireBase(Level level, EditText edtLevel) {
        boolean[] check = new boolean[2];
        Arrays.fill(check, true);
        if (level.getNameLevel() == 0) {
            check[0] = false;
        } else {
            for (Level level1 : levelList) {
                if (level.getNameLevel() == level1.getNameLevel()) {
                    check[1] = false;
                    break;
                }
            }
        }
        if (!check[0]) {
            edtLevel.setError("Không để trống");
            edtLevel.requestFocus();
        } else if (!check[1]) {
            edtLevel.setError("Trùng dữ liệu");
            edtLevel.requestFocus();
        } else {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("idLevel", level.getId());
            hashMap.put("level", level.getNameLevel());
            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("listtopic");
            databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Log.d("child", dataSnapshot.getKey());
                        Topic topic = dataSnapshot.getValue(Topic.class);
                        assert topic != null;
                        if (topic.getIdLevel() == level.getId())
                        {
                            databaseReference1.child(Objects.requireNonNull(dataSnapshot.getKey())).updateChildren(hashMap).isComplete();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            databaseReference.child(String.valueOf(level.getId())).setValue(level).addOnCompleteListener(task -> {
                if (task.isComplete()) {
                    Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public void deleteDataToFire(Level level) {
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("listtopic");
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.d("child", dataSnapshot.getKey());
                    Topic topic = dataSnapshot.getValue(Topic.class);
                    assert topic != null;
                    if (topic.getIdLevel() == level.getId())
                    {
                        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("listquestion");
                        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot1 : snapshot.getChildren())
                                {
                                    Question question = dataSnapshot1.getValue(Question.class);
                                    assert question != null;
                                    if (question.getIdTopic() == topic.getId())
                                    {
                                        databaseReference2.child(Objects.requireNonNull(dataSnapshot1.getKey())).removeValue().isComplete();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("listProcessUser");
                        databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot1 : snapshot.getChildren())
                                {
                                    databaseReference3.child(dataSnapshot1.getKey()+"/listTopic/"+topic.getId()).removeValue().isComplete();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) { }
                        });
                        databaseReference1.child(Objects.requireNonNull(dataSnapshot.getKey())).removeValue().isComplete();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child(String.valueOf(level.getId())).removeValue((error, ref) -> Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show());
    }
}
