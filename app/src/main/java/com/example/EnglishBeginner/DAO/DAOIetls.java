package com.example.EnglishBeginner.DAO;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.EnglishBeginner.Adapter.WordToeicIetlsAdapter;
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

                    wordList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Word word = dataSnapshot.getValue(Word.class);
                    wordList.add(word);
                    Log.e("firebase", word.getWord() );
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
}
