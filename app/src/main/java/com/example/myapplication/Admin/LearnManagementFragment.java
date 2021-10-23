package com.example.myapplication.Admin;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Admin.LearnManagement.DTO.LearnItem;
import com.example.myapplication.Admin.LearnManagement.Adapter.LearnItemAdapter;
import com.example.myapplication.DEFAULTVALUE;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class LearnManagementFragment extends Fragment{

    private RecyclerView rcvLearnItem;
    private List<LearnItem>learnItemList;
    private LearnItemAdapter learnItemAdapter;
    private AdminInterface adminInterface;

    public LearnManagementFragment() {

    }
    private void setControl(View v) {
        rcvLearnItem = v.findViewById(R.id.rcvLearnItem);
    }
    private List<LearnItem> setData()
    {
        learnItemList = new ArrayList<>();
        learnItemList.add(new LearnItem(DEFAULTVALUE.LEVEL,R.drawable.level));
        learnItemList.add(new LearnItem(DEFAULTVALUE.TYPEQUESTION,R.drawable.type));
        learnItemList.add(new LearnItem(DEFAULTVALUE.TOPIC,R.drawable.topic));
        learnItemList.add(new LearnItem(DEFAULTVALUE.QUESTION,R.drawable.question));
        learnItemList.add(new LearnItem(DEFAULTVALUE.ANSWER,R.drawable.answer));
        learnItemList.add(new LearnItem(DEFAULTVALUE.LEARNTOPIC, R.drawable.learn));
        return learnItemList;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learn_management,container,false);
        setControl(view);
        adminInterface = (AdminInterface) getContext();
        learnItemAdapter = new LearnItemAdapter(getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        rcvLearnItem.setLayoutManager(gridLayoutManager);
        learnItemAdapter.setLearnItemList(setData());
        rcvLearnItem.setAdapter(learnItemAdapter);
        learnItemAdapter.setDelegation(new LearnItemAdapter.MyLearnItemInterface() {
            @Override
            public void Transaction(LearnItem learn) {
                adminInterface.Transaction(learn);
            }
        });
        return view;
    }
}