package com.example.EnglishBeginner.Admin.DAO;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.EnglishBeginner.Admin.Adapter.BlogAdapter;
import com.example.EnglishBeginner.Admin.DTO.Blog;
import com.example.EnglishBeginner.Admin.DTO.DEFAULTVALUE;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DAOBlog {
    private List<Blog> blogList;
    private DatabaseReference databaseReference;
    private Context context;

    public List<Blog> getBlogList() {
        return blogList;
    }

    public DAOBlog(Context context) {
        this.context = context;
        blogList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("listblog");
    }

    public void getDataFromRealTimeFirebase(BlogAdapter blogAdapter) {
        databaseReference.addValueEventListener(new ValueEventListener() {
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Get list Topic failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void applyBlog(Blog blog) {
        databaseReference.child(blog.getId() + "/checkApply").setValue(blog.isCheckApply()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                DEFAULTVALUE.alertDialogMessage("Thông báo", "Duyệt thành công!", context);
            }
        });
    }

    public void notApplyBlog(Blog blog) {
        databaseReference.child(String.valueOf(blog.getId())).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                DEFAULTVALUE.alertDialogMessage("Thông báo", "Đã xóa bài viết!", context);
            }
        });
    }
}
