package com.example.EnglishBeginner.Admin.DAO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.EnglishBeginner.Admin.Adapter.AnswerAdapter;
import com.example.EnglishBeginner.Admin.DTO.Answer;
import com.example.EnglishBeginner.Admin.DTO.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DAOAnswer {
    //Fields
    private final DatabaseReference databaseReference;
    private final List<Answer> answerList;
    private Question question;
    private Context context;

    //Constructor
    public DAOAnswer(Context context) {
        this.context = context;
        // lấy đường dẫn của cây dữ liệu firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("listquestion");
        answerList = new ArrayList<>();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Question getQuestion() {
        return question;
    }

    public List<Answer> getAnswerList() {
        return answerList;
    }

    // lấy dữ liệu từ firebase dựa theo id của Question
    public void getDataFromFirebase(int idQuestion, AnswerAdapter answerAdapter) {
        databaseReference.child(idQuestion + "/listanswer").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (answerList != null) {
                    answerList.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Answer answer = dataSnapshot.getValue(Answer.class);
                    assert answerList != null;
                    answerList.add(answer);
                }
                if (answerAdapter != null) {
                    answerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void getAQuestionFromFirebase(int idQuestion) {
        databaseReference.child(String.valueOf(idQuestion)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                question = snapshot.getValue(Question.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // thêm dữ liệu vào firebase
    public void addDataAnswerToFirebaseQuestion(Answer answer1, EditText edtAnswer, int idQuestion) {
        boolean check = true;
        if (answerList.size() > 0) {
            for (Answer answer : answerList) {
                if (answer.getAnswerQuestion().equalsIgnoreCase(answer1.getAnswerQuestion())) {
                    check = false;
                    break;
                }
            }
        }
        if (!check) {
            edtAnswer.setError("Trùng dữ liệu , hãy kiểm tra lại dữ liệu");
            edtAnswer.requestFocus();
        } else if (check)
            {
            databaseReference.child(idQuestion + "/listanswer").child(String.valueOf(answer1.getId())).setValue(answer1).addOnCompleteListener(task -> {
                if (task.isComplete()) {
                    Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    // sửa dữ liệu
    public void editDataToFireBase(Answer answer1, EditText edtAnswer, int idQuestion) {
        boolean check = true;
        if (answerList.size() > 0) {
            for (Answer answer : answerList) {
                if (answer.getAnswerQuestion().equalsIgnoreCase(answer1.getAnswerQuestion())) {
                    check = false;
                    break;
                }
            }
        }
        if (!check) {
            edtAnswer.setError("Trùng dữ liệu , hãy kiểm tra lại dữ liệu");
            edtAnswer.requestFocus();
        } else {
            databaseReference.child(idQuestion + "/listanswer/" + answer1.getId()).setValue(answer1).addOnCompleteListener(task -> {
                if (task.isComplete()) {
                    Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    //xóa dữ liệu
    public void deleteDataToFire(Answer answer, int id) {
        databaseReference.child(id + "/listanswer").child(String.valueOf(answer.getId())).removeValue((error, ref) -> Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show());
    }
}
