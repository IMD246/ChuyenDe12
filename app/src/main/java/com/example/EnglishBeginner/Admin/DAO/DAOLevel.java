package com.example.EnglishBeginner.Admin.DAO;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.EnglishBeginner.Admin.Adapter.LevelAdapter;
import com.example.EnglishBeginner.Admin.DTO.Level;
import com.example.EnglishBeginner.Admin.DTO.Topic;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DAOLevel {
    private List<Level> levelList;
    private DatabaseReference databaseReference;

    public List<Level> getLevelList() {
        return levelList;
    }

    private Context context;

    public DAOLevel(Context context) {
        this.context = context;
        levelList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("listlevel");
    }

    public void getDataFromRealTimeToList(LevelAdapter levelAdapter) {
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
        int s = 1;
        String namelevel = editText.getText().toString().trim();
        for (int i = 0; i < check.length; i++) {
            check[i] = true;
        }
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
        if (check[0] == false) {
            editText.setError("Không để trống");
            editText.requestFocus();
        } else if (check[1] == false) {
            editText.setError("Trùng dữ liệu");
            editText.requestFocus();
        } else {
            if (levelList.size() > 0) {
                s = levelList.get(levelList.size() - 1).getId() + 1;
            }
            databaseReference.child(String.valueOf(level.getId())).setValue(level).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isComplete()) {
                        Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void editDataToFireBase(Level level, EditText edtLevel) {
        boolean[] check = new boolean[2];
        for (int i = 0; i < check.length; i++) {
            check[i] = true;
        }
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
        if (check[0] == false) {
            edtLevel.setError("Không để trống");
            edtLevel.requestFocus();
        } else if (check[1] == false) {
            edtLevel.setError("Trùng dữ liệu");
            edtLevel.requestFocus();
        } else {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("idLevel", level.getId());
            hashMap.put("level", level.getNameLevel());
            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("listtopic");
            databaseReference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d("parent", snapshot.getKey());
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Log.d("child", dataSnapshot.getKey());
                        Topic topic = dataSnapshot.getValue(Topic.class);
                        if (topic.getIdLevel() == level.getId())
                        {
                            databaseReference1.child(dataSnapshot.getKey()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isComplete())
                                    { }
                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            databaseReference.child(String.valueOf(level.getId())).child("listTopic").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Log.d("child", dataSnapshot.getKey());
//                        databaseReference.child(dataSnapshot.getKey()).updateChildren(hashMap);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            databaseReference.child(String.valueOf(level.getId())).setValue(level).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isComplete()) {
                        Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void deleteDataToFire(Level level) {
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("listtopic");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.d("child", dataSnapshot.getKey());
                    Topic topic = dataSnapshot.getValue(Topic.class);
                    if (topic.getIdLevel() == level.getId())
                    {
                        databaseReference1.child(dataSnapshot.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete())
                                { }
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child(String.valueOf(level.getId())).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
