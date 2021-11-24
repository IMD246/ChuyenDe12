package com.example.EnglishBeginner.DAO;

import android.content.Context;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.EnglishBeginner.Adapter.WordToeicIetlsAdapter;
import com.example.EnglishBeginner.DTO.DEFAULTVALUE;
import com.example.EnglishBeginner.DTO.Question;
import com.example.EnglishBeginner.DTO.Word;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DAOWord {
    private List<Word> wordList;
    private DatabaseReference databaseReference;
    private Context context;

    public DAOWord(Context context) {
        wordList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("listquestion");
        this.context = context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Word> getWordList() {
        return wordList;
    }

    public void getDataFromRealTimeToList(WordToeicIetlsAdapter wordToeicIetlsAdapter, String categoryWord) {
        databaseReference.orderByChild("categoryWord").equalTo(categoryWord).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (wordList != null) {
                    wordList.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Question question = dataSnapshot.getValue(Question.class);
                    Word word = new Word();
                    word.setWord(question.getWord());
                    word.setTypeWord(question.getTypeWord());
                    word.setMeaning(question.getWordMeaning());
                    wordList.add(word);
                }
                if (wordToeicIetlsAdapter != null) {
                    wordToeicIetlsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
