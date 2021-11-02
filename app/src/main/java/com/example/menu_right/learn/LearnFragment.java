package com.example.menu_right.learn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menu_right.Adapter.Level_Adapter;
import com.example.menu_right.DAO.DAOLevel;
import com.example.menu_right.DAO.DAOTopic;
import com.example.menu_right.DTO.Level;
import com.example.menu_right.DTO.Topic;
import com.example.menu_right.R;
import com.example.menu_right.main_interface.UserInterfaceActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LearnFragment extends Fragment {
    private UserInterfaceActivity userInterfaceActivity;
    private View myView;

    //KHAI BÁO THÀNH PHẦN TRONG RECYCLERVIEW LEARN
    RecyclerView learnRecyclerView;
    Level_Adapter learnRecyclerView_adapter;
    DAOLevel daoLevel;
    DAOTopic daoTopic;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_fragment_learn, container, false);
        setControl();
        getDataFromRealTime();
        return myView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //Ánh xạ, xử lí view của list
    private void setControl() {
        daoLevel = new DAOLevel(getContext());
        daoTopic = new DAOTopic(getContext());
        learnRecyclerView_adapter = new Level_Adapter(getContext());
        learnRecyclerView = myView.findViewById(R.id.rcvLevel);
        userInterfaceActivity = (UserInterfaceActivity) getActivity();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        learnRecyclerView_adapter.setLevelArrayList(daoLevel.getLevelList());
        learnRecyclerView.setAdapter(learnRecyclerView_adapter);
        linearLayoutManager.setOrientation(learnRecyclerView.VERTICAL);
        learnRecyclerView.setLayoutManager(linearLayoutManager);

    }
    private void getDataFromRealTime() {
        daoLevel.getDataFromRealTimeToList(learnRecyclerView_adapter);
//        daoTopic.getDataFromRealTimeFirebase();
    }
    //hàm chuyển sang màn hình học tiếng Anh
    private void onClickGoToScreen() {
        Intent intent = new Intent(getContext(), LearningEnglishActivity.class);
        getContext().startActivity(intent);
    }
}
