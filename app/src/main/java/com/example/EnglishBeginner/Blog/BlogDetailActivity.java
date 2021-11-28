package com.example.EnglishBeginner.Blog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.EnglishBeginner.Adapter.CommentAdapter;
import com.example.EnglishBeginner.DTO.Blog;
import com.example.EnglishBeginner.DTO.User;
import com.example.EnglishBeginner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class BlogDetailActivity extends AppCompatActivity {
    private ImageView imgAvatarUserBlog, imgContentBlog, imgLikeBlog, imgCommentBlog, imgMyAvatar;
    private TextView tvUserBlog, tvDateBlog, tvContentBlog, tvLikeBlog, tvCommentBlog, tvViewBlog;
    private Button btnPost, btnCancle;

    private RecyclerView recyclerViewComment;
    private CommentAdapter adapter;
    private ArrayList<CommentAdapter> arrayList;

    private DatabaseReference databaseReferenceBlog, databaseReferenceUser;
    private Blog blog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);
        //Ánh xạ
        setControl();
        blog = new Blog();
        //Lấy dữ liệu
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
            String idBlog = bundle.getString("id_blog");
            databaseReferenceBlog = FirebaseDatabase.getInstance().getReference("listblog");
            databaseReferenceBlog.child(idBlog).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    if (task.getResult().exists()) {
                        for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                            hashMap.put(dataSnapshot.getKey(), dataSnapshot.getValue());
                        }
                        tvDateBlog.setText(hashMap.get("dayOfPost").toString());
                        tvContentBlog.setText(hashMap.get("content").toString());
                        if (!(hashMap.get("urlImage").toString().trim().isEmpty())) {
                            Glide.with(BlogDetailActivity.this).load(hashMap.get("urlImage").toString()).into(imgContentBlog);
                        } else {
                            imgContentBlog.setVisibility(View.GONE);
                        }
                        tvLikeBlog.setText(String.valueOf(hashMap.get("like")));
                        tvCommentBlog.setText(String.valueOf(hashMap.get("comment")));
                        tvViewBlog.setText(String.valueOf(hashMap.get("view")));
                    }
                }
            });

            databaseReferenceUser = FirebaseDatabase.getInstance().getReference("users");
            String idUser = bundle.getString("id_user");
            databaseReferenceUser.child(idUser).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    if (task.getResult().exists()) {
                        for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                            hashMap.put(dataSnapshot.getKey(), dataSnapshot.getValue());
                        }
                        String urlImage = hashMap.get("imageUser").toString();
                        String fullName = hashMap.get("fullname").toString();
                        if (fullName.trim().isEmpty()) {
                            tvUserBlog.setText("Unknown");
                        } else {
                            tvUserBlog.setText(hashMap.get("fullname").toString());
                        }
                        if (!(urlImage.trim().isEmpty())) {
                            Glide.with(BlogDetailActivity.this).load(urlImage).into(imgAvatarUserBlog);
                        } else {
                            imgAvatarUserBlog.setVisibility(View.GONE);
                        }
                    }
                }
            });
        }

    }

    private void setControl() {
        imgAvatarUserBlog = findViewById(R.id.blog_img_avatar);
        imgContentBlog = findViewById(R.id.blog_img_thumnail);
        imgLikeBlog = findViewById(R.id.img_like);
        imgCommentBlog = findViewById(R.id.img_comment);
        imgMyAvatar = findViewById(R.id.img_myavatar);
        tvUserBlog = findViewById(R.id.blog_txt_userName);
        tvDateBlog = findViewById(R.id.blog_txt_dayOfPost);
        tvContentBlog = findViewById(R.id.blog_txt_content);
        tvLikeBlog = findViewById(R.id.tv_like);
        tvCommentBlog = findViewById(R.id.blog_txt_comment);
        tvViewBlog = findViewById(R.id.blog_txt_view);

        recyclerViewComment = findViewById(R.id.rcv_comment_blog);
        adapter = new CommentAdapter(this);

    }
}