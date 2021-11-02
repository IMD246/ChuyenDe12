package com.example.englishbeginner.Admin.DAO;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.englishbeginner.Admin.Adapter.WordToeicIetlsAdapter;
import com.example.englishbeginner.Admin.DTO.Word;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DAOIetls {
    private List<Word> wordList;
    private DatabaseReference databaseReference;
    private Context context;

    public DAOIetls(Context context) {
        wordList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("listietls");
        this.context = context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Word> getWordList() {
        return wordList;
    }

    public void getDataFromRealTimeToList(WordToeicIetlsAdapter wordToeicIetlsAdapter) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (wordList != null) {
                    wordList.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Word word = dataSnapshot.getValue(Word.class);
                    wordList.add(word);
                }
                if (wordToeicIetlsAdapter != null) {
                    wordToeicIetlsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Get list question failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addDataToFireBase(Word word, EditText edtWord,EditText edtMeaning) {
        boolean[] check = new boolean[2];
        int s = 1;
        for (int i = 0; i < check.length; i++) {
            check[i] = true;
        }
        if (word.getWord().isEmpty() || word.getWord().length() == 0) {
            check[0] = false;
        }
        else {
            if (wordList.size() > 0) {
                for (Word word1 : wordList) {
                    if (word.getWord().equalsIgnoreCase(word1.getWord()))
                    {
                        check[1] = false;
                        break;
                    }
                }
            }
        }
        if (check[0] == false) {
            edtWord.setError("Không để trống");
            edtWord.requestFocus();
        }
        else if (check[1] == false) {
            edtWord.setError("Trùng dữ liệu , hãy kiểm tra lại dữ liệu");
            edtWord.requestFocus();
        } else {
            databaseReference.child(String.valueOf(word.getId())).setValue(word).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isComplete()) {
                        edtWord.setText("");
                        edtMeaning.setText("");
                        Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public void editDataToFireBase(Word word, EditText edtWord) {
        boolean[] check = new boolean[2];
        int s = 1;
        for (int i = 0; i < check.length; i++) {
            check[i] = true;
        }
        if (word.getWord().isEmpty() || word.getWord().length() == 0) {
            check[0] = false;
        }
        else {
            if (wordList.size() > 0) {
                for (Word word1 : wordList) {
                    if (word.getWord().equalsIgnoreCase(word1.getWord())&&
                            word.getTypeWord().equalsIgnoreCase(word1.getTypeWord())&&
                            word.getMeaning().equalsIgnoreCase(word.getMeaning()))
                    {
                        check[1] = false;
                        break;
                    }
                }
            }
        }
        if (check[0] == false) {
            edtWord.setError("Không để trống");
            edtWord.requestFocus();
        }
        else if (check[1] == false) {
            edtWord.setError("Trùng dữ liệu , hãy kiểm tra lại dữ liệu");
            edtWord.requestFocus();
        } else {
            databaseReference.child(String.valueOf(word.getId())).setValue(word).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isComplete()) {
                        Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void deleteDataToFire(Word word) {
        databaseReference.child(String.valueOf(word.getId())).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
