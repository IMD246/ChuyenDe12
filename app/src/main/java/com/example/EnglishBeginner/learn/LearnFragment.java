package com.example.EnglishBeginner.learn;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EnglishBeginner.Adapter.Level_Adapter;
import com.example.EnglishBeginner.DAO.DAOLevel;
import com.example.EnglishBeginner.DAO.DAOTopic;
import com.example.EnglishBeginner.DTO.Level;
import com.example.EnglishBeginner.DTO.Topic;
import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.main_interface.UserInterfaceActivity;

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
        getDataFromRealTime();
    }

    //Ánh xạ, xử lí view của list
    private void setControl() {
        daoLevel = new DAOLevel(getContext());
        daoTopic = new DAOTopic(getContext());
        userInterfaceActivity = (UserInterfaceActivity) getActivity();
        learnRecyclerView_adapter = new Level_Adapter(getContext());
        learnRecyclerView_adapter.setUid(userInterfaceActivity.firebaseUser.getUid());
        learnRecyclerView_adapter.setTopicList(daoTopic.getTopicList());
        learnRecyclerView = myView.findViewById(R.id.rcvLevel);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        learnRecyclerView.setHasFixedSize(true);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        learnRecyclerView_adapter.setLevelArrayList(daoLevel.getLevelList());
        learnRecyclerView.setAdapter(learnRecyclerView_adapter);
        learnRecyclerView.setLayoutManager(linearLayoutManager);
        learnRecyclerView_adapter.setInterface_learn(new Level_Adapter.Interface_Learn() {
            @Override
            public void onClickItemLearn(Level level) {

            }
            @Override
            public void createAlertDialog(Topic topic) {
                userInterfaceActivity.alertDialogTopic(topic);
            }
        });
    }
    private void getDataFromRealTime() {
        daoLevel.getDataFromRealTimeToList(learnRecyclerView_adapter);
        daoTopic.getDataFromRealTimeFirebase();
    }
}
