package com.example.menu_right.DAO;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.menu_right.DTO.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void getDataFromRealTime(FirebaseUser firebaseUser)
    {
        mDatabaseReference.child(firebaseUser.getUid()).
                addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    user = snapshot.getValue(User.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void updateProFileUser(HashMap<String,Object>hashMap, FirebaseUser firebaseUser) {
        mDatabaseReference.child(firebaseUser.getUid()).updateChildren(hashMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(context, "Cập nhật hồ sơ thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
