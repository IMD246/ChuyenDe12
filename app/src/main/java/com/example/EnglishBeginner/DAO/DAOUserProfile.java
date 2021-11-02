package com.example.EnglishBeginner.DAO;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.EnglishBeginner.DTO.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DAOUserProfile {
    private DatabaseReference mDatabaseReference;
    private User user;
    private Context context;

    public DAOUserProfile(Context context) {
        this.context = context;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
        user = new User();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void getDataFromRealTime(String uid)
    {
        mDatabaseReference.child(uid).
                addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    user = snapshot.getValue(User.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void updateProFileUser(HashMap<String,Object>hashMap, String uid) {
        mDatabaseReference.child(uid).updateChildren(hashMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(context, "Cập nhật hồ sơ thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }
}