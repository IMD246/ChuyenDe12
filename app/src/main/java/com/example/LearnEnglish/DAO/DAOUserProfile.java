package com.example.LearnEnglish.DAO;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.LearnEnglish.DTO.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DAOUserProfile {
    private DatabaseReference mDatabaseReference;
    private Context context;
    public DAOUserProfile(Context context) {
        this.context = context;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
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
