package com.example.englishbeginner.Admin.DAO;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.englishbeginner.Admin.Adapter.UserAccountAdapter;
import com.example.englishbeginner.Admin.DTO.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DAOUserAccount {
    private List<UserAccount> userAccountList;
    private Context context;
    private CollectionReference collectionReference;
    private FirebaseFirestore firestore;

    public List<UserAccount> getUserAccountList() {
        return userAccountList;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public DAOUserAccount(Context context) {
        this.context = context;
        userAccountList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        collectionReference = firestore.collection("users");
    }

    public void getDataFromRealTimeToList(UserAccountAdapter userAccountAdapter) {
        firestore.collection("users").whereEqualTo("authenticate", "User").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                } else {
                    for (DocumentChange documentChange : value.getDocumentChanges()) {
                        if (documentChange.getType() == DocumentChange.Type.ADDED) {
                            UserAccount userAccount = documentChange.getDocument().toObject(UserAccount.class);
                            userAccount.setDocumentID(documentChange.getDocument().getId());
                            userAccountList.add(userAccount);
                        }
                    }
                }
                if (userAccountAdapter!=null)
                {
                    userAccountAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void UpdateStatusAccountUser(UserAccount userAccount,UserAccountAdapter userAccountAdapter) {
        if (!userAccount.getDocumentID().isEmpty()) {
            firestore.collection("users").document(userAccount.getDocumentID()).
                    set(userAccount).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (userAccount.getBlock().equals(true)) {
                        Toast.makeText(context, "Đã khóa " + userAccount.getEmail() + " thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Mở khóa " + userAccount.getEmail() + " thành công", Toast.LENGTH_SHORT).show();
                    }
                    userAccountAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}
