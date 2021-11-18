package com.example.EnglishBeginner.learn;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EnglishBeginner.Adapter.Level_Adapter;
import com.example.EnglishBeginner.DAO.DAOLevel;
import com.example.EnglishBeginner.DTO.Level;
import com.example.EnglishBeginner.DTO.Topic;
import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.main_interface.UserInterfaceActivity;
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
    private DatabaseReference databaseReference;
    DAOLevel daoLevel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_fragment_learn, container, false);
        setControl();
        getDataFromRealTime();
        return myView;
    }
    //Ánh xạ, xử lí view của list
    private void setControl() {
        daoLevel = new DAOLevel(getContext());
        learnRecyclerView = myView.findViewById(R.id.rcvLevel);
        userInterfaceActivity = (UserInterfaceActivity) getActivity();
        learnRecyclerView_adapter = new Level_Adapter(getContext());
        learnRecyclerView_adapter.setUid(userInterfaceActivity.firebaseUser.getUid());
        learnRecyclerView_adapter.setLevelArrayList(daoLevel.getLevelList());
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
        databaseReference = FirebaseDatabase.getInstance().getReference("listtopic");
        List<Topic>topicList = new ArrayList<>();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (topicList!=null)
                {
                    topicList.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Topic topic = dataSnapshot.getValue(Topic.class);
                    topicList.add(topic);
                }
                learnRecyclerView_adapter.setTopicList(topicList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                learnRecyclerView.setHasFixedSize(true);
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                learnRecyclerView.setAdapter(learnRecyclerView_adapter);
                learnRecyclerView.setLayoutManager(linearLayoutManager);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Get list Topic failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
