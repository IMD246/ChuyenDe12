package com.example.englishbeginner.Admin.DAO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import com.example.englishbeginner.Admin.Adapter.UserAccountAdapter;
import com.example.englishbeginner.Admin.DTO.UserAccount;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DAOUserAccount {
    private final List<UserAccount> userAccountList;
    private Context context;
    private final FirebaseFirestore firestore;

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
    }

    @SuppressLint("NotifyDataSetChanged")
    public void getDataFromRealTimeToList(UserAccountAdapter userAccountAdapter) {
        firestore.collection("users").whereEqualTo("authenticate", "User").addSnapshotListener((value, error) -> {
            if (error != null) {
                return;
            } else {
                assert value != null;
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
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void UpdateStatusAccountUser(UserAccount userAccount, UserAccountAdapter userAccountAdapter) {
        if (!userAccount.getDocumentID().isEmpty()) {
            firestore.collection("users").document(userAccount.getDocumentID()).
                    set(userAccount).addOnCompleteListener(task -> {
                        if (userAccount.getBlock().equals(true)) {
                            Toast.makeText(context, "Đã khóa " + userAccount.getEmail() + " thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Mở khóa " + userAccount.getEmail() + " thành công", Toast.LENGTH_SHORT).show();
                        }
                        userAccountAdapter.notifyDataSetChanged();
                    });
        }
    }
}
