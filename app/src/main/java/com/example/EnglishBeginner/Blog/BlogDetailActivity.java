package com.example.EnglishBeginner.Blog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.EnglishBeginner.Adapter.CommentAdapter;
import com.example.EnglishBeginner.DTO.Blog;
import com.example.EnglishBeginner.DTO.Comment;
import com.example.EnglishBeginner.DTO.Like;
import com.example.EnglishBeginner.DTO.SubComment;
import com.example.EnglishBeginner.DTO.User;
import com.example.EnglishBeginner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class BlogDetailActivity extends AppCompatActivity {
    private ImageView imgAvatarUserBlog, imgContentBlog, imgLikeBlog, imgCommentBlog, imgMyAvatar, imgFavorite;
    private TextView tvUserBlog, tvDateBlog, tvContentBlog, tvLikeBlog, tvCommentBlog, tvViewBlog;
    private EditText edtInputComment;
    private Button btnPost, btnCancle;
    private LinearLayout layoutButton;

    private RecyclerView recyclerViewComment;
    private CommentAdapter adapter;
    private ArrayList<Comment> arrayList;

    private DatabaseReference databaseReferenceBlog, databaseReferenceUser, databaseReferenceComment, databaseReferenceLike, getDatabaseReferenceFavoriteBlog;
    private FirebaseUser firebaseUser;
    private Comment comment;

    private String fullName, imageUser;

    private Bundle bundle;
    private Blog blog;

    private Like like;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);
        //Get data
        bundle = getIntent().getExtras();//get bundle
        if (bundle != null) {
            String idBlog = bundle.getString("id_blog");//get ib_blog
            //lấy dữ liệu từ firebase cho blog
            blog = new Blog();
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
                        blog.setDayOfPost(hashMap.get("dayOfPost").toString());
                        tvContentBlog.setText(hashMap.get("content").toString());
                        blog.setContent(hashMap.get("content").toString());
                        if (!(hashMap.get("urlImage").toString().trim().isEmpty())) {
                            Glide.with(BlogDetailActivity.this).load(hashMap.get("urlImage").toString()).into(imgContentBlog);
                            blog.setUrlImage(hashMap.get("urlImage").toString());
                        } else {
                            imgContentBlog.setVisibility(View.GONE);
                        }
                        tvLikeBlog.setText(String.valueOf(hashMap.get("like")));
                        blog.setLike(Integer.parseInt(hashMap.get("like").toString()));
                        tvCommentBlog.setText(String.valueOf(hashMap.get("comment")));
                        blog.setComment(Integer.parseInt(hashMap.get("comment").toString()));
                        tvViewBlog.setText(String.valueOf(hashMap.get("view")));
                        blog.setIdUser(String.valueOf(hashMap.get("idUser")));
                        blog.setCheckApply(Boolean.parseBoolean(String.valueOf(hashMap.get("checkApply"))));
                        blog.setNameUser(String.valueOf(hashMap.get("nameUser")));
                        blog.setTitle(String.valueOf(hashMap.get("title")));
                        blog.setId(Integer.parseInt(idBlog));
                    }
                }
            });

            //lấy dữ liệu từ firebase cho user
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

            //lấy dữ liệu cho curent user
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            databaseReferenceUser.child(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    if (task.getResult().exists()) {
                        for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                            hashMap.put(dataSnapshot.getKey(), dataSnapshot.getValue());
                        }
                        if (String.valueOf(hashMap.get("fullname")).trim().isEmpty()) {
                            fullName = "Unknown";
                        } else {
                            fullName = String.valueOf(hashMap.get("fullname"));
                        }
                        imageUser = String.valueOf(hashMap.get("imageUser"));
                        if (!(hashMap.get("imageUser").toString().trim().isEmpty())) {
                            Glide.with(BlogDetailActivity.this).load(hashMap.get("imageUser").toString()).into(imgMyAvatar);
                        }
                    }
                }
            });
        }

        //Lấy dữ liệu cho like
        databaseReferenceLike = FirebaseDatabase.getInstance().getReference("listLike");
        databaseReferenceLike.child(bundle.getString("id_blog")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvLikeBlog.setText(String.valueOf(snapshot.getChildrenCount()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReferenceLike.child(bundle.getString("id_blog")+"/"+firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    imgLikeBlog.setSelected(true);
                }else {
                    imgLikeBlog.setSelected(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Ánh xạ
        setControl();

        //Hiển thị layout button khi người dùng viết thêm bình luận
        edtInputComment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                layoutButton.setVisibility(View.VISIBLE);
            }
        });

        //Sự kiện khi ấn like blog
        imgLikeBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Like like = new Like();
                if (imgLikeBlog.isSelected()) {
                    imgLikeBlog.setSelected(false);
                    databaseReferenceLike.child(bundle.getString("id_blog")+"/"+firebaseUser.getUid()).removeValue().isSuccessful();
                } else {
                    imgLikeBlog.setSelected(true);
                    like.setIdBlog(Integer.parseInt(bundle.getString("id_blog")));
                    like.setIdUser(firebaseUser.getUid());
                    databaseReferenceLike.child(bundle.get("id_blog").toString()+"/"+firebaseUser.getUid()).setValue(like).isSuccessful();
                }
            }
        });

        //Sự kiện khi ấn comment:
        imgCommentBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtInputComment.requestFocus();
            }
        });

        //Sự kiện khi ấn đăng
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contentComment = edtInputComment.getText().toString();
                if (contentComment.trim().isEmpty()) {
                    edtInputComment.setError("Bạn phải nhập bình luận trước!");
                    edtInputComment.requestFocus();
                } else {
                    comment = new Comment();
                    if (arrayList.size() > 0) {
                        comment.setId(arrayList.get(arrayList.size() - 1).getId() + 1);
                    } else {
                        comment.setId(1);
                    }
                    comment.setContent(contentComment);
                    comment.setIdBlog(bundle.getString("id_blog"));
                    comment.setNameUser(fullName);
                    comment.setUrlImage(imageUser);
                    comment.setDayOfPost(getDateTime());
                    databaseReferenceComment.child(String.valueOf(comment.getId())).setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(BlogDetailActivity.this, "Thêm bình luận thành công!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                edtInputComment.setText("");
            }
        });

        //Sự kiện khi ấn nút hủy
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtInputComment.setText("");
                layoutButton.setVisibility(View.GONE);
            }
        });

        //sự kiện ấn yêu thích
        getDatabaseReferenceFavoriteBlog = FirebaseDatabase.getInstance().getReference("listFavorite");
        getDatabaseReferenceFavoriteBlog.child(firebaseUser.getUid()+"/"+bundle.get("id_blog").toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    imgFavorite.setSelected(true);
                }else {
                    imgFavorite.setSelected(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgFavorite.isSelected()){
                    imgFavorite.setSelected(false);
                    getDatabaseReferenceFavoriteBlog.child(firebaseUser.getUid()+"/"+bundle.get("id_blog").toString()).removeValue().isSuccessful();
                }else {
                    imgFavorite.setSelected(true);
                    getDatabaseReferenceFavoriteBlog.child(firebaseUser.getUid()+"/"+bundle.get("id_blog").toString()).setValue(blog).isSuccessful();
                }
            }
        });
    }

    private String getDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss a, dd/MM/yyyy");
        String currentDateTime = simpleDateFormat.format(Calendar.getInstance().getTime());
        return currentDateTime;
    }

    private void setControl() {
        imgAvatarUserBlog = findViewById(R.id.blog_img_avatar);
        imgContentBlog = findViewById(R.id.blog_img_thumnail);
        imgLikeBlog = findViewById(R.id.img_like);
        imgCommentBlog = findViewById(R.id.img_comment);
        imgMyAvatar = findViewById(R.id.img_myavatar);
        imgFavorite = findViewById(R.id.img_add_favorite);
        tvUserBlog = findViewById(R.id.blog_txt_userName);
        tvDateBlog = findViewById(R.id.blog_txt_dayOfPost);
        tvContentBlog = findViewById(R.id.blog_txt_content);
        tvLikeBlog = findViewById(R.id.tv_like);
        tvCommentBlog = findViewById(R.id.blog_txt_comment);
        tvViewBlog = findViewById(R.id.blog_txt_view);
        edtInputComment = findViewById(R.id.edt_input_comment);
        btnCancle = findViewById(R.id.btn_cancle);
        btnPost = findViewById(R.id.btn_post);
        layoutButton = findViewById(R.id.layout_button);

        recyclerViewComment = findViewById(R.id.rcv_comment_blog);
        LinearLayoutManager manager = new LinearLayoutManager(BlogDetailActivity.this);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewComment.setLayoutManager(manager);
        adapter = new CommentAdapter(this);
        databaseReferenceComment = FirebaseDatabase.getInstance().getReference("listcomment");
        arrayList = new ArrayList<>();
        databaseReferenceComment.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Comment cmt = dataSnapshot.getValue(Comment.class);
                    if (cmt.getIdBlog().equalsIgnoreCase(bundle.getString("id_blog"))){
                        arrayList.add(cmt);
                    }
                }
                adapter.setListComments(arrayList);
                recyclerViewComment.setAdapter(adapter);
                tvCommentBlog.setText(String.valueOf(arrayList.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BlogDetailActivity.this, "get data Erro!", Toast.LENGTH_SHORT).show();
            }
        });

        //get du lieu cho view
        DatabaseReference databaseReferenceView = FirebaseDatabase.getInstance().getReference("listView");
        databaseReferenceView.child(String.valueOf(bundle.getString("id_blog"))).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvViewBlog.setText(String.valueOf(snapshot.getChildrenCount()));
                blog.setView(Integer.parseInt(String.valueOf(snapshot.getChildrenCount())));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}