package com.example.myapplication.User.LearnWord.history;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.User.LearnWord.history.source.HistorySqliteDataHelper;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HistoryScreen extends AppCompatActivity {

    ArrayList<HistoryItem> listItem2 = new ArrayList<>();
    HistorySqliteDataHelper historySqliteDataHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_screen);


        historySqliteDataHelper = new HistorySqliteDataHelper(getBaseContext());

        setControl();
        AddItem();


    }
    private void setControl() {
        RecyclerView rcl=(RecyclerView)findViewById(R.id.history_fragment_rcl);
        // tao ra mot doi tuong adapter
        HistoryAdapter adapter = new HistoryAdapter(getBaseContext(),listItem2);
        //manager de custom hien thi len recycle view
        LinearLayoutManager manager = new GridLayoutManager(getBaseContext(),1);

        //set cac gia tri len recycler
        rcl.setItemAnimator(new DefaultItemAnimator());// Gán hiệu ứng cho Recyclerview
        rcl.setAdapter(adapter);
        rcl.setLayoutManager(manager);
    }


    private void AddItem() {

        DateFormat df = new SimpleDateFormat(" d MMM yyyy, HH:mm");
        String nowTime = df.format(Calendar.getInstance().getTime());
        historySqliteDataHelper.fetchData(listItem2);


    }

}
