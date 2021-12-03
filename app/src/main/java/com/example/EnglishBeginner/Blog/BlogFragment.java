package com.example.EnglishBeginner.Blog;

import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EnglishBeginner.Adapter.BlogAdapter;
import com.example.EnglishBeginner.Adapter.MenuAdapter;
import com.example.EnglishBeginner.DAO.DAOBlog;
import com.example.EnglishBeginner.DTO.Blog;
import com.example.EnglishBeginner.DTO.DEFAULTVALUE;
import com.example.EnglishBeginner.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.Query;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BlogFragment extends Fragment {
    private View view;
    RecyclerView recyclerView_blog;
    RecyclerView recyclerView_menu;
    ArrayList<String> listMenu;
    private FloatingActionButton btnAddBlog;
    private DAOBlog daoBlog;
    private BlogAdapter blogAdapter;
    private MenuAdapter menuAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_blog, container, false);
        setControl();
        setAdapterForMenu();
        setAdapter();
        getDataRealTime();
        return view;
    }

    private void getDataRealTime() {
        daoBlog.getDataFromRealTimeFirebase(blogAdapter);
    }

    private void setAdapterForMenu() {
        listMenu = new ArrayList<>();
        listMenu.add(DEFAULTVALUE.MOSTFAVORITE);
        listMenu.add(DEFAULTVALUE.NEW);
        listMenu.add(DEFAULTVALUE.YOURFAVORITE);
        menuAdapter = new MenuAdapter(getContext(), listMenu, recyclerView_menu);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
        recyclerView_menu.setAdapter(menuAdapter);
        recyclerView_menu.setLayoutManager(manager);
        menuAdapter.setSetAdapterOfListBlog(typeBlog -> setDataFollowOption(typeBlog));
    }

    private void setDataFollowOption(String typeBlog) {
        List<Blog> blogList = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("listblog");

        if (typeBlog.equalsIgnoreCase(DEFAULTVALUE.MOSTFAVORITE))
        {
            databaseReference.orderByChild("like").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    blogList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Blog blog = dataSnapshot.getValue(Blog.class);
                        if (blog.isCheckApply()) {
                            blogList.add(blog);
                        }
                    }
                    setAdapterc2(blogList);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else if (typeBlog.equalsIgnoreCase(DEFAULTVALUE.NEW)) {
            databaseReference.orderByChild("dayOfPost").limitToFirst(10000).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    blogList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Blog blog = dataSnapshot.getValue(Blog.class);
                        if (blog.isCheckApply()) {
                            blogList.add(blog);
                        }
                    }
                    setAdapterc2(blogList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else
        {
            blogList.clear();
            Log.d("test nek", "setDataFollowOption: sinh sim");
            DatabaseReference databaseReferenceFavoriteBlog = FirebaseDatabase.getInstance().getReference("listFavorite");
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            databaseReferenceFavoriteBlog.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (blogList != null) {
                        blogList.clear();
                    }
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Blog blog = dataSnapshot.getValue(Blog.class);
                        blogList.add(blog);
                    }

                    if (blogAdapter != null) {
                        blogAdapter.notifyDataSetChanged();
                    }
                    setAdapterc2(blogList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        }
    }

    private void setControl() {
        daoBlog = new DAOBlog(getContext());
        blogAdapter = new BlogAdapter(getContext());
        recyclerView_blog = view.findViewById(R.id.rv_blog);
        btnAddBlog = view.findViewById(R.id.btnAddBlog);
        btnAddBlog.setOnClickListener(view -> {
            startActivity(new Intent(getContext(), AddBlogActivity.class));
        });
        recyclerView_menu = view.findViewById(R.id.rv_menu);
    }

    private void setAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        blogAdapter.setListBlog(daoBlog.getBlogList());
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView_blog.setAdapter(blogAdapter);
        recyclerView_blog.setLayoutManager(manager);
        blogAdapter.notifyDataSetChanged();
    }

    private void setAdapterc2(List<Blog> newListBlog) {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        BlogAdapter blogAdapter = new BlogAdapter(getContext());
        blogAdapter.setListBlog(newListBlog);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView_blog.setAdapter(blogAdapter);
        recyclerView_blog.setLayoutManager(manager);
        blogAdapter.notifyDataSetChanged();
    }
}