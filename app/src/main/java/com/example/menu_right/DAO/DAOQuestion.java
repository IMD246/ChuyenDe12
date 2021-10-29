package com.example.menu_right.DAO;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.menu_right.DTO.Question;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DAOQuestion {
    private List<Question> questionList;
    private DatabaseReference databaseReference;
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

//    public void getDataFromRealTimeToList(QuestionAdapter questionAdapter, LearnQuestionAdapter learnQuestionAdapter) {
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (questionList != null) {
//                    questionList.clear();
//                }
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    Question question = dataSnapshot.getValue(Question.class);
//                    questionList.add(question);
//                }
//                if (questionAdapter != null)
//                {
//                    questionAdapter.notifyDataSetChanged();
//                }
//                if (learnQuestionAdapter != null)
//                {
//                    learnQuestionAdapter.notifyDataSetChanged();
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(context, "Get list question failed", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
    public void addDataToFireBase(Question question, EditText edtTitle, EditText edtCorrectAnswer) {
        boolean[] check = new boolean[3];
        int s=1;
        for (int i = 0; i < check.length; i++) {
            check[i] = true;
        }
        if (question.getTitle().isEmpty() || question.getTitle().length() == 0) {
            check[0] = false;
        }
        else if (question.getCorrectAnswer().isEmpty() || question.getCorrectAnswer().length() == 0) {
            check[1] = false;
        }
        else {
            if (questionList.size() > 0)
            {
                for (Question question1 : questionList) {
                    if (question1.getNameTopic().equalsIgnoreCase(question.getNameTopic())&&
                            question1.getNameTypeQuestion().equalsIgnoreCase(question.getNameTypeQuestion())&&
                            question1.getTitle().equalsIgnoreCase(question.getTitle()))
                    {
                        check[2] = false;
                        break;
                    }
                }
            }
        }
        if (check[0] == false) {
            edtTitle.setError("Không để trống");
            edtTitle.requestFocus();
        }
        else if (check[1] == false) {
            edtCorrectAnswer.setError("Không để trống");
            edtCorrectAnswer.requestFocus();
        }
        else if (check[2] == false) {
            edtTitle.setError("Trùng dữ liệu , hãy kiểm tra lại dữ liệu");
            edtTitle.requestFocus();
        } else {
            databaseReference.child(String.valueOf(question.getId())).setValue(question).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isComplete()) {
                        Toast.makeText(context,"Thêm thành công", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public void editDataToFireBase(Question question, EditText edtTitle, EditText edtCorrectAnswer, TextView tvTitle,TextView tvCorrect) {
        boolean[] check = new boolean[3];
        int s=1;
        for (int i = 0; i < check.length; i++) {
            check[i] = true;
        }
        if (question.getTitle().isEmpty() || question.getTitle().length() == 0) {
            check[0] = false;
        }
        else if (question.getCorrectAnswer().isEmpty() || question.getCorrectAnswer().length() == 0) {
            check[1] = false;
        }
        else {
            if (questionList.size() > 0)
            {
                for (Question question1 : questionList) {
                    if (question1.getNameTopic().equalsIgnoreCase(question.getNameTopic())&&
                            question1.getNameTypeQuestion().equalsIgnoreCase(question.getNameTypeQuestion())&&
                            question1.getTitle().equalsIgnoreCase(question.getTitle()))
                    {
                        check[2] = false;
                        break;
                    }
                }
            }
        }
        if (check[0] == false) {
            edtTitle.setError("Không để trống");
            edtTitle.requestFocus();
        }
        else if (check[1] == false) {
            edtCorrectAnswer.setError("Không để trống");
            edtCorrectAnswer.requestFocus();
        }
        else if (check[2] == false) {
            edtTitle.setError("Trùng dữ liệu , hãy kiểm tra lại dữ liệu");
            edtTitle.requestFocus();
        } else {
            tvTitle.setText("Câu hỏi: "+question.getTitle());
            tvCorrect.setText("Câu Trả Lời Chính xác: "+question.getCorrectAnswer());
            databaseReference.child(String.valueOf(question.getId())).setValue(question).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isComplete()) {
                        Toast.makeText(context,"Sửa thành công", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public void deleteDataToFire(Question question) {
        databaseReference.child(String.valueOf(question.getId())).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateLearnQuestion(int idQuestion,Map<String, Object> map, EditText edtWord, EditText edtExample, EditText edtGrammar) {
        boolean check[] = new boolean[3];
        for (int i=0;i<check.length;i++)
        {
            check[i] = true;
        }
        if (map.get("example").toString().isEmpty() ||map.get("example").toString().trim().length()==0)
        {
            edtExample.setError("Không để trống");
            edtExample.requestFocus();
        }
        else if (map.get("word").toString().isEmpty() ||map.get("word").toString().trim().length()==0)
        {
            edtWord.setError("Không để trống");
            edtExample.requestFocus();
        }
        else if (map.get("grammar").toString().isEmpty() ||map.get("grammar").toString().trim().length()==0)
        {
            edtGrammar.setError("Không để trống");
            edtGrammar.requestFocus();
        }
        else
        {
            databaseReference.child(String.valueOf(idQuestion)).updateChildren(map, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
