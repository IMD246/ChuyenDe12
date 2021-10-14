package com.example.myapplication.Admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.myapplication.R;

public class LearnManagementFragment extends Fragment implements View.OnClickListener{

    private RelativeLayout rlt1, rlt2, rlt3, rlt4, rlt5, rlt6;

    public LearnManagementFragment() {

    }
    private void setControl(View v) {
        rlt1 = v.findViewById(R.id.rlt1);
        rlt1.setOnClickListener(this);
        rlt2 = v.findViewById(R.id.rlt2);
        rlt2.setOnClickListener(this);
        rlt3 = v.findViewById(R.id.rlt3);
        rlt3.setOnClickListener(this);
        rlt4 = v.findViewById(R.id.rlt4);
        rlt4.setOnClickListener(this);
        rlt5 = v.findViewById(R.id.rlt5);
        rlt5.setOnClickListener(this);
        rlt6 = v.findViewById(R.id.rlt6);
        rlt6.setOnClickListener(this);
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
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.rlt1:
                startActivity(new Intent(getContext(),levelmanagement.class));
                break;
            case R.id.rlt2:
                Toast.makeText(getContext(), "2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rlt3:
                Toast.makeText(getContext(), "3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rlt4:
                Toast.makeText(getContext(), "4", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rlt5:
                Toast.makeText(getContext(), "5", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rlt6:
                Toast.makeText(getContext(), "6", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}