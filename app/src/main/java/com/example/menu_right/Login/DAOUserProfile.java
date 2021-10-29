package com.example.menu_right.Login;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DAOUserProfile {
    private DatabaseReference mDatabaseReference;
    private ArrayList<User>userArrayList;

    public DAOUserProfile() {
        userArrayList = new ArrayList<>();
    }
    public void updateDataFireBaseToList(FirebaseAuth mFirebaseAuth)
    {
        FirebaseDatabase.getInstance().getReference("users").child(mFirebaseAuth.getUid()).
                addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userArrayList.clear();
                for (DataSnapshot shot : snapshot.getChildren())
                {
                    User user = shot.getValue(User.class);
                    userArrayList.add(user);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public ArrayList<User> getUserArrayList() {
        return userArrayList;
    }
}
