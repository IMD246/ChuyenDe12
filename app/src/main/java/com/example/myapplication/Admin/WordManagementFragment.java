package com.example.myapplication.Admin;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Admin.Adapter.LearnItemAdapter;
import com.example.myapplication.Admin.DTO.LearnItem;
import com.example.myapplication.DEFAULTVALUE;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class WordManagementFragment extends Fragment {

    private RecyclerView rcvWordItem;
    private List<LearnItem>learnItemList;
    private LearnItemAdapter learnItemAdapter;
    private AdminInterface adminInterface;
    public WordManagementFragment() {
        // Required empty public constructor
    }
    private List<LearnItem> setData()
    {
        learnItemList = new ArrayList<>();
        learnItemList.add(new LearnItem(DEFAULTVALUE.TOEIC,R.drawable.level));
        learnItemList.add(new LearnItem(DEFAULTVALUE.IETLS,R.drawable.type));
        return learnItemList;
    }
    private void setControl(View v) {
        rcvWordItem = v.findViewById(R.id.rcvWordItem);
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
        View view = inflater.inflate(R.layout.word_management_fragment,container,false);
        setControl(view);
        adminInterface = (AdminInterface) getContext();
        learnItemAdapter = new LearnItemAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(rcvWordItem.VERTICAL);
        rcvWordItem.setLayoutManager(linearLayoutManager);
        learnItemAdapter.setLearnItemList(setData());
        rcvWordItem.setAdapter(learnItemAdapter);
        learnItemAdapter.setDelegation(new LearnItemAdapter.MyLearnItemInterface() {
            @Override
            public void Transaction(LearnItem learn) {
                adminInterface.Transaction(learn);
            }
        });
        return view;
    }
}