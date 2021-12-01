package com.example.EnglishBeginner.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.security.AppUriAuthenticationPolicy;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.EnglishBeginner.Blog.BlogDetailActivity;
import com.example.EnglishBeginner.DTO.Blog;
import com.example.EnglishBeginner.DTO.Comment;
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
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private Context context;
    private List<Comment> listComments;
    private List<SubComment> listSubComments;
    private FirebaseUser firebaseUser;

    public CommentAdapter(Context context) {
        this.context = context;
    }

    public void setListComments(List<Comment> listComments) {
        this.listComments = listComments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_recycleview_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = listComments.get(position);
        //đỗ dữ liệu
        listSubComments = new ArrayList<>();
        if (comment.getNameUser().trim().isEmpty()) {
            holder.tvUserName.setText("Unknown");
        } else {
            holder.tvUserName.setText(comment.getNameUser());
        }
        if (!(comment.getUrlImage().trim().isEmpty())) {
            Glide.with(context).load(comment.getUrlImage()).into(holder.imgUser);
        } else {
            holder.imgUser.setVisibility(View.GONE);
        }
        holder.tvContent.setText(comment.getContent());
        holder.tvDatePost.setText(comment.getDayOfPost());
        holder.tvLike.setText(String.valueOf(comment.getLike()));

        //Sự kiện khi ấn trả lời
        holder.tvReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.viewgroupReply.setVisibility(View.VISIBLE);//hiển thị view nhập comment
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference databaseReferenceUser = FirebaseDatabase.getInstance().getReference("users");
                databaseReferenceUser.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        if (!(user.getImageUser().trim().isEmpty())){
                            Glide.with(context).load(user.getImageUser()).into(holder.imgMyAvatar);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //Sự kiện khi nhấn Hủy
                holder.btnCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.edtReply.setText("");
                        holder.viewgroupReply.setVisibility(View.GONE);
                    }
                });

                //Sự kiện khi ấn Đăng
                holder.btnPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.edtReply.getText().toString().trim().isEmpty()){
                            holder.edtReply.setError("Bạn phải nhập bình luận trước!");
                            holder.edtReply.requestFocus();
                        }else{
                            SubComment subcomment = new SubComment();
                            if (listSubComments.size() > 0) {
                                subcomment.setId(listSubComments.get(listSubComments.size() - 1).getId() + 1);
                            } else {
                                subcomment.setId(1);
                            }
                            subcomment.setContent(holder.edtReply.getText().toString());
                            subcomment.setIdBlog(comment.getIdBlog());
                            subcomment.setIdComment(String.valueOf(comment.getId()));
                            subcomment.setIdUser(firebaseUser.getUid());
                            subcomment.setDayOfPost(getDateTime());
                            DatabaseReference databaseReferenceComment = FirebaseDatabase.getInstance().getReference("listSubComment");
                            databaseReferenceComment.child(String.valueOf(subcomment.getId())).setValue(subcomment).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(context, "Thêm bình luận thành công!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            holder.edtReply.setText("");
                            holder.viewgroupReply.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        //Báo cáo bình luận đến admin
        holder.tvReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //like bình luận
        holder.imglike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //Xử lý và đỗ dữ liệu cho recycleview listSubComment
        SubCommentAdapter subCommentAdapter = new SubCommentAdapter(context);//khai báo adapter

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        holder.recyclerView.setLayoutManager(layoutManager);//set linearlayoutManager

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("listSubComment");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listSubComments.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    SubComment subcmt = dataSnapshot.getValue(SubComment.class);
                    if (subcmt.getIdComment().equalsIgnoreCase(String.valueOf(comment.getId()))){
                        listSubComments.add(subcmt);
                    }
                }
                //Kiểm tra: không có bình luận con -> ẩn hiển thị bình luận con và ngược lại
                if (listSubComments.size() == 0){
                    holder.imgShow.setVisibility(View.GONE);
                    holder.tvShow.setVisibility(View.GONE);
                }else{
                    holder.tvShow.setText("Xem thêm "+listSubComments.size()+" bình luận");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "can't get data!", Toast.LENGTH_SHORT).show();
            }
        });

        //Tạo function reply cho bình luận con
        subCommentAdapter.setCallBack(new SubCommentAdapter.CallBack() {
            @Override
            public void callBackReply() {
                holder.viewgroupReply.setVisibility(View.VISIBLE);
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference databaseReferenceUser = FirebaseDatabase.getInstance().getReference("users");
                databaseReferenceUser.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        if (!(user.getImageUser().trim().isEmpty())){
                            Glide.with(context).load(user.getImageUser()).into(holder.imgMyAvatar);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //Sự kiện khi nhấn Hủy
                holder.btnCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.edtReply.setText("");
                        holder.viewgroupReply.setVisibility(View.GONE);
                    }
                });

                //Sự kiện khi ấn Đăng
                holder.btnPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.edtReply.getText().toString().trim().isEmpty()){
                            holder.edtReply.setError("Bạn phải nhập bình luận trước!");
                            holder.edtReply.requestFocus();
                        }else{
                            SubComment subcomment = new SubComment();
                            if (listSubComments.size() > 0) {
                                subcomment.setId(listSubComments.get(listSubComments.size() - 1).getId() + 1);
                            } else {
                                subcomment.setId(1);
                            }
                            subcomment.setContent(holder.edtReply.getText().toString());
                            subcomment.setIdBlog(comment.getIdBlog());
                            subcomment.setIdComment(String.valueOf(comment.getId()));
                            subcomment.setIdUser(firebaseUser.getUid());
                            subcomment.setDayOfPost(getDateTime());
                            DatabaseReference databaseReferenceComment = FirebaseDatabase.getInstance().getReference("listSubComment");
                            databaseReferenceComment.child(String.valueOf(subcomment.getId())).setValue(subcomment).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(context, "Thêm bình luận thành công!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            holder.edtReply.setText("");
                            holder.viewgroupReply.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        //Sự kiện ấn "xem nhiều bình luận"
        holder.imgShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.imgShow.isSelected()){
                    holder.imgShow.setSelected(false);
                    holder.tvShow.setText("Xem thêm "+listSubComments.size()+" bình luận");
                    holder.recyclerView.setVisibility(View.GONE);
                }else{
                    holder.imgShow.setSelected(true);
                    holder.tvShow.setText("Ẩn tất cả bình luận");
                    holder.recyclerView.setVisibility(View.VISIBLE);
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("listSubComment");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            listSubComments.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                SubComment subcmt = dataSnapshot.getValue(SubComment.class);
                                if (subcmt.getIdComment().equalsIgnoreCase(String.valueOf(comment.getId()))){
                                    listSubComments.add(subcmt);
                                }
                            }
                            subCommentAdapter.setListSubComments(listSubComments);
                            holder.recyclerView.setAdapter(subCommentAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(context, "can't get data!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        //Sự kiện ấn "xem nhiều bình luận"
        holder.tvShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.imgShow.isSelected()){
                    holder.imgShow.setSelected(false);
                    holder.tvShow.setText("Xem thêm "+listSubComments.size()+" bình luận");
                    holder.recyclerView.setVisibility(View.GONE);
                }else{
                    holder.imgShow.setSelected(true);
                    holder.tvShow.setText("Ẩn tất cả bình luận");
                    holder.recyclerView.setVisibility(View.VISIBLE);
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("listSubComment");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            listSubComments.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                SubComment subcmt = dataSnapshot.getValue(SubComment.class);
                                if (subcmt.getIdComment().equalsIgnoreCase(String.valueOf(comment.getId()))){
                                    listSubComments.add(subcmt);
                                }
                            }
                            subCommentAdapter.setListSubComments(listSubComments);
                            holder.recyclerView.setAdapter(subCommentAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(context, "can't get data!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    private String getDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss a, dd/MM/yyyy");
        String currentDateTime = simpleDateFormat.format(Calendar.getInstance().getTime());
        return currentDateTime;
    }

    @Override
    public int getItemCount() {
        return listComments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgUser, imglike, imgShow, imgMyAvatar;
        TextView tvUserName, tvDatePost, tvContent, tvLike, tvReply, tvReport, tvShow;
        RecyclerView recyclerView;
        LinearLayout viewgroupReply;
        EditText edtReply;
        Button btnPost, btnCancle;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.avatar_user_comment);
            imglike = itemView.findViewById(R.id.img_like_comment);
            imgShow = itemView.findViewById(R.id.img_show_more_comment);
            imgMyAvatar = itemView.findViewById(R.id.img_myavatar);
            tvContent = itemView.findViewById(R.id.tv_comment);
            tvUserName = itemView.findViewById(R.id.tv_username_comment);
            tvDatePost = itemView.findViewById(R.id.tv_daypost_comment);
            tvLike = itemView.findViewById(R.id.tv_like_comment);
            tvReply = itemView.findViewById(R.id.tv_reply);
            tvReport = itemView.findViewById(R.id.tv_report);
            tvShow = itemView.findViewById(R.id.tv_show_more_comment);
            recyclerView = itemView.findViewById(R.id.rcv_sub_comment);
            viewgroupReply = itemView.findViewById(R.id.viewgroup_reply);
            edtReply = itemView.findViewById(R.id.edt_input_comment);
            btnPost = itemView.findViewById(R.id.btn_post);
            btnCancle = itemView.findViewById(R.id.btn_cancle);
        }
    }
}
