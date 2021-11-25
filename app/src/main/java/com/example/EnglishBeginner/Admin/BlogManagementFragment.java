package com.example.EnglishBeginner.Admin;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EnglishBeginner.Admin.Adapter.BlogAdapter;
import com.example.EnglishBeginner.Admin.DAO.DAOBlog;
import com.example.EnglishBeginner.Admin.DTO.Blog;
import com.example.EnglishBeginner.Admin.DTO.User;
import com.example.EnglishBeginner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BlogManagementFragment extends Fragment {

    View v;
    BlogAdapter blogAdapter;
    DAOBlog daoBlog;
    AutoCompleteTextView svBlog;

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
        v = inflater.inflate(R.layout.fragment_blogmanagement, container, false);
        initUI();
        getDataFirebase();
        return v;
    }
    private void getDataFirebase() {
        daoBlog.getDataFromRealTimeFirebase(blogAdapter);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        List<String> list = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    list.add(user.getEmail());
                }
                svBlog.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initUI() {
        daoBlog = new DAOBlog(getContext());
        svBlog = v.findViewById(R.id.svBlog);
        RecyclerView recyclerView = v.findViewById(R.id.rcvBlog);
        blogAdapter = new BlogAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        blogAdapter.setBlogList(daoBlog.getBlogList());
        recyclerView.setAdapter(blogAdapter);
        blogAdapter.setMyDelegationLevel(new BlogAdapter.MyDelegationLevel() {
            @Override
            public void applyBlog(Blog blog) {
                alertDialog(blog, 1,"Bạn có muốn duyệt bài viết này?");
            }

            @Override
            public void notApplyBlog(Blog blog) {
                alertDialog(blog, 2,"Bạn có muốn xóa bài viết này?");
            }
        });
    }
    // Xây dựng một Hộp thoại thông báo
    public void alertDialog(Blog blog,int choice,String msg) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setTitle("Thông báo");
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Có",
                (dialog, id) -> {
                    if (choice == 1) {
                        blog.setCheckApply(true);
                        daoBlog.applyBlog(blog);
                    }
                    else
                    {
                        daoBlog.notApplyBlog(blog);
                    }
                });
        builder1.setNegativeButton(
                "Không",
                (dialog, id) -> dialog.cancel());
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}