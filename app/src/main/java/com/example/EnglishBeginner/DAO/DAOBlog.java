package com.example.EnglishBeginner.DAO;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.EnglishBeginner.Adapter.BlogAdapter;
import com.example.EnglishBeginner.DTO.Blog;
import com.example.EnglishBeginner.DTO.DEFAULTVALUE;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DAOBlog {
    private List<Blog> blogList;
    private DatabaseReference databaseReferenceBlog, databaseReferenceFavoriteBlog;
    private FirebaseUser firebaseUser;
    private Context context;

    public List<Blog> getBlogList() {
        return blogList;
    }

    public DAOBlog(Context context) {
        this.context = context;
        blogList = new ArrayList<>();
        databaseReferenceBlog = FirebaseDatabase.getInstance().getReference("listblog");
        databaseReferenceFavoriteBlog = FirebaseDatabase.getInstance().getReference("listFavorite");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void getDataFromRealTimeFirebase(BlogAdapter blogAdapter) {
        databaseReferenceBlog.orderByChild("like").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (blogList != null) {
                    blogList.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Blog blog = dataSnapshot.getValue(Blog.class);
                    if (blog.isCheckApply()) {
                        blogList.add(blog);
                    }
                }
                if (blogAdapter != null) {
                    blogAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Get list Topic failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addBlog(Blog blog) {
        databaseReferenceBlog.child(String.valueOf(blog.getId())).setValue(blog).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                DEFAULTVALUE.alertDialogMessage("Thông báo", "Bài viết của bạn đang được kiểm duyệt", context);
            }
        });
    }
}
