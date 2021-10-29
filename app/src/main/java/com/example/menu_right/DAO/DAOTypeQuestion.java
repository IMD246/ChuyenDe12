package com.example.menu_right.DAO;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.menu_right.DTO.TypeQuestion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DAOTypeQuestion {
    private List<TypeQuestion> typeQuestionList;
    private DatabaseReference databaseReference;
    private Context context;


    public List<TypeQuestion> getTypeQuestionList() {
        return typeQuestionList;
    }

    public DAOTypeQuestion(Context context) {
        this.context = context;
        typeQuestionList = new ArrayList<TypeQuestion>();
        databaseReference = FirebaseDatabase.getInstance().getReference("listtypequestion");
    }

//    public void getDataFromRealTimeToList(TypeQuestionAdapter typeQuestionAdapter) {
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (typeQuestionList != null) {
//                    typeQuestionList.clear();
//                }
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                        TypeQuestion typeQuestion = dataSnapshot.getValue(TypeQuestion.class);
//                        typeQuestionList.add(typeQuestion);
//                    }
//                if (typeQuestionAdapter != null) {
//                    typeQuestionAdapter.notifyDataSetChanged();
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(context, "Get list Type Question failed", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    public void addDataToFireBase(EditText editText) {
        boolean[] check = new boolean[2];
        int s = 1;
        String nameTypeQuestion = editText.getText().toString().trim();
        for (int i = 0; i < check.length; i++) {
            check[i] = true;
        }
        if (nameTypeQuestion.isEmpty() || nameTypeQuestion.length() == 0) {
            check[0] = false;
        } else {
            if (typeQuestionList.size() > 0)
            {
                for (TypeQuestion typeQuestion1 : typeQuestionList) {
                    if (nameTypeQuestion.equalsIgnoreCase(typeQuestion1.getTypeQuestionName())) {
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
            if (typeQuestionList.size() > 0)
            {
                s = typeQuestionList.get(typeQuestionList.size() - 1).getId() + 1;
            }
            TypeQuestion typeQuestion = new TypeQuestion(s, editText.getText().toString());
            databaseReference.child(String.valueOf(typeQuestion.getId())).setValue(typeQuestion).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isComplete()) {
                        Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public void editDataToFireBase(TypeQuestion typeQuestion, EditText editText) {
        boolean[] check = new boolean[2];
        for (int i = 0; i < check.length; i++) {
            check[i] = true;
        }
        String nameTypeQuestion = editText.getText().toString();
        if (nameTypeQuestion.isEmpty() || nameTypeQuestion.length() == 0) {
            check[0] = false;
        } else {
            for (TypeQuestion typeQuestion1 : typeQuestionList) {
                if (nameTypeQuestion.equalsIgnoreCase(typeQuestion1.getTypeQuestionName())) {
                    check[1] = false;
                    break;
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
            TypeQuestion typeQuestion2 = new TypeQuestion(typeQuestion.getId(),editText.getText().toString());
            databaseReference.child(String.valueOf(typeQuestion2.getId())).setValue(typeQuestion2).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isComplete()) {
                        Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public void deleteDataToFire(TypeQuestion typeQuestion) {
        databaseReference.child(String.valueOf(typeQuestion.getId())).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
