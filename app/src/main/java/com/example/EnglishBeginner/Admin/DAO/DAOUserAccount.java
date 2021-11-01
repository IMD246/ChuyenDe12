package com.example.EnglishBeginner.Admin.DAO;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.EnglishBeginner.Admin.Adapter.LevelAdapter;
import com.example.EnglishBeginner.Admin.Adapter.UserAccountAdapter;
import com.example.EnglishBeginner.Admin.DTO.Level;
import com.example.EnglishBeginner.Admin.DTO.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DAOUserAccount {
    private List<UserAccount> userAccountList;
    private Context context;
    private CollectionReference collectionReference;
    private DocumentReference documentReference;
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
        firestore.collection("users").whereEqualTo("authenticate", "User").orderBy("email").addSnapshotListener(new EventListener<QuerySnapshot>() {
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

    public void UpdateStatusAccountUser(UserAccount userAccount) {
    }

//    public void editDataToFireBase(Level level, EditText edtLevel) {
//        boolean[] check = new boolean[2];
//        for (int i = 0; i < check.length; i++) {
//            check[i] = true;
//        }
//        if (level.getNameLevel()==0) {
//            check[0] = false;
//        } else {
//            for (Level level1 : levelList) {
//                if (level.getNameLevel() == level1.getNameLevel()) {
//                    check[1] = false;
//                    break;
//                }
//            }
//        }
//        if (check[0] == false) {
//            edtLevel.setError("Không để trống");
//            edtLevel.requestFocus();
//        } else if (check[1] == false) {
//            edtLevel.setError("Trùng dữ liệu");
//            edtLevel.requestFocus();
//        } else {
//            databaseReference.child(String.valueOf(level.getId())).setValue(level).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isComplete()) {
//                        Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }
//    }
}
