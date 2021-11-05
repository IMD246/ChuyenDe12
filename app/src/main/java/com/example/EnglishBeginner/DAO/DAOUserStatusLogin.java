package com.example.EnglishBeginner.DAO;

import android.content.Context;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class DAOUserStatusLogin {
    private CollectionReference collectionReference;
    private FirebaseFirestore firestore;
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public DAOUserStatusLogin(Context context) {
        this.context = context;
        firestore = FirebaseFirestore.getInstance();
        collectionReference = firestore.collection("users");
    }

    public void UpdateStatusAccountUser(HashMap<String, Object> hashMap, String uid) {
        if (!(uid.isEmpty())) {
            firestore.collection("users").document(uid).update(hashMap);
        }
    }
}
