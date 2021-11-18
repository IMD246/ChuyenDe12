package com.example.EnglishBeginner.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.EnglishBeginner.DAO.DAOUserProfile;
import com.example.EnglishBeginner.DTO.DEFAULTVALUE;
import com.example.EnglishBeginner.DTO.HashPass;
import com.example.EnglishBeginner.DTO.User;
import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.main_interface.UserInterfaceActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class HelpActivity extends AppCompatActivity {
    private ListView lvHelp;
    private SearchView svHelp;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> stringArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_activity);
        setControl();
        svHelp.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                lvHelp.clearFocus();
                if (arrayList.contains(query)){
                    stringArrayAdapter.getFilter().filter(query);
                }else{
                    Toast.makeText(HelpActivity.this, "Not found", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                stringArrayAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void setControl() {
        lvHelp = findViewById(R.id.lvHelp);
        svHelp = findViewById(R.id.seach_view_help);
        arrayList = new ArrayList<>();
        arrayList.add("Câu hỏi chung");arrayList.add("Diễn đàn");arrayList.add("Tài khoản");
        stringArrayAdapter = new ArrayAdapter<>(this,R.layout.layout_item_help, arrayList);
        lvHelp.setAdapter(stringArrayAdapter);
    }

}