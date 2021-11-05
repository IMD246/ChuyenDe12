package com.example.EnglishBeginner.Admin.DAO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.EnglishBeginner.Admin.Adapter.LearnQuestionAdapter;
import com.example.EnglishBeginner.Admin.Adapter.QuestionAdapter;
import com.example.EnglishBeginner.Admin.DTO.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

    public void getDataFromRealTimeToList(QuestionAdapter questionAdapter, LearnQuestionAdapter learnQuestionAdapter) {
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
                if (learnQuestionAdapter != null) {
                    learnQuestionAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Get list question failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //public void getDataFromRealTimeToList(QuestionAdapter questionAdapter, LearnQuestionAdapter learnQuestionAdapter) {
//    databaseReference.addValueEventListener(new ValueEventListener() {
//        @Override
//        public void onDataChange(@NonNull DataSnapshot snapshot) {
//            if (questionList != null) {
//                questionList.clear();
//            }
//            Log.d("Parent",snapshot.getKey());
//            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                databaseReference.child(dataSnapshot.getKey()+"/listquestion").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        Log.d("Parent1",snapshot.getKey());
//                        for (DataSnapshot snapshot1 : snapshot.getChildren())
//                        {
//                            Log.d("child1",snapshot1.getKey());
//                            Question question = snapshot1.getValue(Question.class);
//                            questionList.add(question);
//                        }
//                        if (questionAdapter != null)
//                        {
//                            questionAdapter.notifyDataSetChanged();
//                        }
//                        if (learnQuestionAdapter != null)
//                        {
//                            learnQuestionAdapter.notifyDataSetChanged();
//                        }
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError error) {
//
//        }
//    });
//}
    public void addDataToFireBase(Question question, EditText edtTitle, EditText edtCorrectAnswer) {
        boolean[] check = new boolean[3];
        Arrays.fill(check, true);
        if (question.getTitle().isEmpty() || question.getTitle().length() == 0) {
            check[0] = false;
        } else if (question.getCorrectAnswer().isEmpty() || question.getCorrectAnswer().length() == 0) {
            check[1] = false;
        } else {
            if (questionList.size() > 0) {
                for (Question question1 : questionList) {
                    if (question1.getNameTopic().equalsIgnoreCase(question.getNameTopic()) &&
                            question1.getNameTypeQuestion().equalsIgnoreCase(question.getNameTypeQuestion()) &&
                            question1.getTitle().equalsIgnoreCase(question.getTitle())) {
                        check[2] = false;
                        break;
                    }
                }
            }
        }
        if (!check[0]) {
            edtTitle.setError("Không để trống");
            edtTitle.requestFocus();
        } else if (!check[1]) {
            edtCorrectAnswer.setError("Không để trống");
            edtCorrectAnswer.requestFocus();
        } else if (!check[2]) {
            edtTitle.setError("Trùng dữ liệu , hãy kiểm tra lại dữ liệu");
            edtTitle.requestFocus();
        } else {
            databaseReference.child(String.valueOf(question.getId())).setValue(question).addOnCompleteListener(task -> {
                if (task.isComplete()) {
                    Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
                }
            });
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("id", question.getId());
            hashMap.put("title", question.getTitle());
            hashMap.put("nameTypeQuestion", question.getNameTypeQuestion());
            hashMap.put("correctAnswer", question.getCorrectAnswer());
            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("listtopic/" + question.getIdTopic());
            databaseReference1.child("listquestion/" + question.getId()).updateChildren(hashMap).addOnCompleteListener(task -> {
                if (task.isComplete()) {
                    Toast.makeText(context, "Thêm Question vào Topic thành công", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    public void editDataToFireBase(Question question, EditText edtTitle, EditText edtCorrectAnswer, TextView tvTitle, TextView tvCorrect) {
        boolean[] check = new boolean[3];
        Arrays.fill(check, true);
        if (question.getTitle().isEmpty() || question.getTitle().length() == 0) {
            check[0] = false;
        } else if (question.getCorrectAnswer().isEmpty() || question.getCorrectAnswer().length() == 0) {
            check[1] = false;
        } else {
            if (questionList.size() > 0) {
                for (Question question1 : questionList) {
                    if (question1.getNameTopic().equalsIgnoreCase(question.getNameTopic()) &&
                            question1.getNameTypeQuestion().equalsIgnoreCase(question.getNameTypeQuestion()) &&
                            question1.getTitle().equalsIgnoreCase(question.getTitle())) {
                        check[2] = false;
                        break;
                    }
                }
            }
        }
        if (!check[0]) {
            edtTitle.setError("Không để trống");
            edtTitle.requestFocus();
        } else if (!check[1]) {
            edtCorrectAnswer.setError("Không để trống");
            edtCorrectAnswer.requestFocus();
        } else if (!check[2]) {
            edtTitle.setError("Trùng dữ liệu , hãy kiểm tra lại dữ liệu");
            edtTitle.requestFocus();
        } else {
            tvTitle.setText("Câu hỏi: " + question.getTitle());
            tvCorrect.setText("Câu Trả Lời Chính xác: " + question.getCorrectAnswer());
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("id", question.getId());
            hashMap.put("nameTypeQuestion", question.getNameTypeQuestion());
            hashMap.put("title", question.getTitle());
            hashMap.put("correctAnswer", question.getCorrectAnswer());
            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("listtopic");
            databaseReference1.child(question.getIdTopic() + "/listquestion/" + question.getId()).updateChildren(hashMap).isComplete();
            databaseReference.child(String.valueOf(question.getId())).setValue(question).addOnCompleteListener(task -> {
                if (task.isComplete()) {
                    Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void deleteDataToFire(Question question) {
        databaseReference.child(String.valueOf(question.getId())).removeValue((error, ref) -> Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show());
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("listtopic");
        databaseReference1.child(question.getIdTopic() + "/listquestion/" + question.getId()).removeValue().isComplete();
    }

    public void updateLearnQuestion(int idQuestion, Map<String, Object> map, EditText edtWord, EditText edtExample, EditText edtGrammar) {
        if (Objects.requireNonNull(map.get("example")).toString().isEmpty() || Objects.requireNonNull(map.get("example")).toString().trim().length() == 0) {
            edtExample.setError("Không để trống");
            edtExample.requestFocus();
        } else if (Objects.requireNonNull(map.get("word")).toString().isEmpty() || Objects.requireNonNull(map.get("word")).toString().trim().length() == 0) {
            edtWord.setError("Không để trống");
            edtExample.requestFocus();
        } else if (Objects.requireNonNull(map.get("grammar")).toString().isEmpty() || Objects.requireNonNull(map.get("grammar")).toString().trim().length() == 0) {
            edtGrammar.setError("Không để trống");
            edtGrammar.requestFocus();
        } else {
            databaseReference.child(String.valueOf(idQuestion)).updateChildren(map, (error, ref) -> Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show());
        }
    }
}
