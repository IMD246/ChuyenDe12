package com.example.EnglishBeginner.Admin.DAO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.EnglishBeginner.Admin.Adapter.QuestionAdapter;
import com.example.EnglishBeginner.Admin.DTO.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DAOQuestion {
    private final List<Question> questionList;
    private final DatabaseReference databaseReference;
    private Context context;

    public DAOQuestion(Context context) {
        questionList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("listquestion");
        this.context = context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void getDataFromRealTimeToList(QuestionAdapter questionAdapter) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (questionList != null) {
                    questionList.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Question question = dataSnapshot.getValue(Question.class);
                    assert questionList != null;
                    questionList.add(question);
                }
                if (questionAdapter != null) {
                    questionAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Get list question failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @SuppressLint("SetTextI18n")
    public void editDataToFireBase(Question question, EditText edtTitle,EditText edtWord, EditText edtCorrectAnswer, TextView tvTitle, TextView tvCorrect) {
        boolean[] check = new boolean[4];
        Arrays.fill(check, true);
        if (question.getTitle().trim().isEmpty()) {
            check[0] = false;
        } else if (question.getCorrectAnswer().trim().isEmpty()) {
            check[1] = false;
        } else if (question.getWord().trim().isEmpty()) {
            check[2] = false;
        } else {
            if (questionList.size() > 0) {
                for (Question question1 :questionList) {
                    if (question1.getNameTopic().equalsIgnoreCase(question.getNameTopic()) &&
                            question1.getTitle().equalsIgnoreCase(question.getTitle())
                            && question1.getWord().equalsIgnoreCase(question.getWord())) {
                        check[3] = false;
                        break;
                    }
                }
            }
        }
        if (!check[0]) {
            edtTitle.setError("Không bỏ trống");
            edtTitle.requestFocus();
        } else if (!check[1]) {
            edtCorrectAnswer.setError("Không để trống");
            edtCorrectAnswer.requestFocus();
        }
        else if (!check[2]) {
            edtWord.setError("Không bỏ trống");
            edtWord.requestFocus();
        }
        else if (!check[3]) {
            edtTitle.setError("Trùng dữ liệu , hãy kiểm tra lại dữ liệu");
            edtTitle.requestFocus();
        }else {
            tvTitle.setText("Câu hỏi: " + question.getTitle());
            tvCorrect.setText("Câu Trả Lời Chính xác: " + question.getCorrectAnswer());
            databaseReference.child(String.valueOf(question.getId())).setValue(question).addOnCompleteListener(task -> {
                if (task.isComplete()) {
                    Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void deleteDataToFire(Question question) {
        databaseReference.child(String.valueOf(question.getId())).removeValue((error, ref) -> Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show());
    }
}
