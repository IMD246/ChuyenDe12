package com.example.EnglishBeginner.learn;

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

import com.example.EnglishBeginner.Adapter.Level_Adapter;
import com.example.EnglishBeginner.DAO.DAOLevel;
import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.learn.learning.LearningEnglishFragment;
import com.example.EnglishBeginner.main_interface.UserInterfaceActivity;

public class LearnFragment extends Fragment {
    private UserInterfaceActivity userInterfaceActivity;
    private View myView;

    //KHAI BÁO THÀNH PHẦN TRONG RECYCLERVIEW LEARN
    RecyclerView learnRecyclerView;
    Level_Adapter learnRecyclerView_adapter;
    DAOLevel daoLevel;
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
        learnRecyclerView_adapter = new Level_Adapter(getContext());
        learnRecyclerView = myView.findViewById(R.id.rcvLevel);
        userInterfaceActivity = (UserInterfaceActivity) getActivity();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        learnRecyclerView.setHasFixedSize(true);
        linearLayoutManager.setOrientation(learnRecyclerView.VERTICAL);
        learnRecyclerView_adapter.setLevelArrayList(daoLevel.getLevelList());
        learnRecyclerView.setAdapter(learnRecyclerView_adapter);
        learnRecyclerView.setLayoutManager(linearLayoutManager);
    }
    private void getDataFromRealTime() {
        daoLevel.getDataFromRealTimeToList(learnRecyclerView_adapter);
    }
    //hàm chuyển sang màn hình học tiếng Anh
    private void onClickGoToScreen() {
        Intent intent = new Intent(getContext(), LearningEnglishFragment.class);
        getContext().startActivity(intent);
    }
}
