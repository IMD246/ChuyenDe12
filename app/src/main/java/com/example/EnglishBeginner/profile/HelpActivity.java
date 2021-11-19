package com.example.EnglishBeginner.profile;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.EnglishBeginner.R;

import java.util.ArrayList;


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
        arrayList.add("Câu hỏi chung");
        arrayList.add("Diễn đàn");
        arrayList.add("Tài khoản");
        stringArrayAdapter = new ArrayAdapter<>(this,R.layout.layout_item_help,R.id.tvHelp,arrayList);
        lvHelp.setAdapter(stringArrayAdapter);
    }

}