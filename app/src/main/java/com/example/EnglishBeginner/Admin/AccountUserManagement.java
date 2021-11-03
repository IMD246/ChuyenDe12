package com.example.EnglishBeginner.Admin;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EnglishBeginner.Admin.Adapter.UserAccountAdapter;
import com.example.EnglishBeginner.Admin.DAO.DAOUserAccount;
import com.example.EnglishBeginner.Admin.DTO.UserAccount;
import com.example.EnglishBeginner.R;

import java.util.HashMap;

public class AccountUserManagement extends Fragment {

    private RecyclerView rcvUserAccount;
    private androidx.appcompat.widget.SearchView svUserAccount;
    private UserAccountAdapter userAccountAdapter;
    private DAOUserAccount daoUserAccount;
    private View v;

    public AccountUserManagement() {
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
        v = inflater.inflate(R.layout.fragment_account_user_management, container, false);
        initUI(v);
        getDataFromRealTime();
        return v;
    }

    private void initUI(View v) {
        daoUserAccount = new DAOUserAccount(getContext());
        userAccountAdapter = new UserAccountAdapter(getContext());
        rcvUserAccount = v.findViewById(R.id.rcvUserAccount);
        svUserAccount = v.findViewById(R.id.svUserAccount);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvUserAccount.setLayoutManager(linearLayoutManager);
        userAccountAdapter.setUserAccountList(daoUserAccount.getUserAccountList());
        rcvUserAccount.setAdapter(userAccountAdapter);
        userAccountAdapter.setMyDelegationLevel(new UserAccountAdapter.MyDelegationLevel() {
            @Override
            public void blockItem(UserAccount userAccount) {
                alertDialog(userAccount);
            }
        });
        svUserAccount.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                userAccountAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void getDataFromRealTime() {
        daoUserAccount.getDataFromRealTimeToList(userAccountAdapter);
    }

    // Xây dựng một Hộp thoại thông báo
    private void alertDialog(UserAccount userAccount) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("email", userAccount.getEmail());
        hashMap.put("authenticate", userAccount.getAuthenticate());
        if (getContext() != null) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
            if (userAccount.getBlock() == false) {
                builder1.setMessage("Bạn có muốn khóa " + userAccount.getEmail() + " không?");
                userAccount.setBlock(true);
            } else {
                builder1.setMessage("Bạn có muốn mở khóa " + userAccount.getEmail() + " không?");
                userAccount.setBlock(false);
            }
            hashMap.put("isBlock", userAccount.getBlock());
            builder1.setCancelable(true);
            builder1.setPositiveButton(
                    "Có",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            daoUserAccount.UpdateStatusAccountUser(userAccount, userAccountAdapter, hashMap);
                        }
                    });

            builder1.setNegativeButton(
                    "Không",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }
}