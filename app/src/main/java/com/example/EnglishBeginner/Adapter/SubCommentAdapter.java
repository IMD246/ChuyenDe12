package com.example.EnglishBeginner.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.EnglishBeginner.Blog.BlogDetailActivity;
import com.example.EnglishBeginner.DTO.Blog;
import com.example.EnglishBeginner.DTO.Comment;
import com.example.EnglishBeginner.DTO.LikeComment;
import com.example.EnglishBeginner.DTO.SubComment;
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

import java.util.HashMap;
import java.util.List;

public class SubCommentAdapter extends RecyclerView.Adapter<SubCommentAdapter.ViewHolder>{

    Context context;
    List<SubComment> listComments;
    CallBack callBack;

    public SubCommentAdapter(Context context) {
        this.context = context;
    }

    public void setListSubComments(List<SubComment> listComments) {
        this.listComments = listComments;
    }
    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_recycleview_sub_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SubComment subComment = listComments.get(position);
        DatabaseReference databaseReferenceUser = FirebaseDatabase.getInstance().getReference("users");
        databaseReferenceUser.child(subComment.getIdUser()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                HashMap<String, Object> hashMap = new HashMap<>();
                if (task.getResult().exists()){
                    for (DataSnapshot dataSnapshot : task.getResult().getChildren()){
                        hashMap.put(dataSnapshot.getKey(), dataSnapshot.getValue());
                    }
                    if (String.valueOf(hashMap.get("fullname")).trim().isEmpty()){
                        holder.tvFullName.setText("Unknown");
                    }else{
                        holder.tvFullName.setText(String.valueOf(hashMap.get("fullname")));
                    }
                    if (!(String.valueOf(hashMap.get("imageUser")).trim().isEmpty())){
                        Glide.with(context).load(String.valueOf(hashMap.get("imageUser"))).into(holder.imgUser);
                    }
                }
            }
        });
        holder.tvContent.setText(subComment.getContent());
        holder.tvPostDate.setText(subComment.getDayOfPost());
        holder.tvLike.setText(String.valueOf(subComment.getLike()));

        //like bình luận
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReferenceLike = FirebaseDatabase.getInstance().getReference("listLikeSubComment");
        databaseReferenceLike.child(String.valueOf(subComment.getId())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.tvLike.setText(String.valueOf(snapshot.getChildrenCount()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReferenceLike.child(subComment.getId()+"/"+firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    holder.imgLike.setSelected(true);
                }else {
                    holder.imgLike.setSelected(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LikeComment likeComment = new LikeComment();
                if (holder.imgLike.isSelected()) {
                    holder.imgLike.setSelected(false);
                    databaseReferenceLike.child(subComment.getId()+"/"+firebaseUser.getUid()).removeValue().isSuccessful();
                } else {
                    holder.imgLike.setSelected(true);
                    likeComment.setIdComment(subComment.getId());
                    likeComment.setIdUser(firebaseUser.getUid());
                    databaseReferenceLike.child(subComment.getId()+"/"+firebaseUser.getUid()).setValue(likeComment).isSuccessful();
                }
            }
        });

        //xử lý trả lời bình luận
        holder.tvReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.callBackReply();
            }
        });

        holder.tvReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        if (listComments.size() > 0) {
            return listComments.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgUser, imgLike;
        TextView tvFullName, tvPostDate, tvContent, tvLike, tvReply, tvReport;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.avatar_user_comment);
            imgLike = itemView.findViewById(R.id.img_like_comment);
            tvContent = itemView.findViewById(R.id.tv_comment);
            tvFullName = itemView.findViewById(R.id.tv_username_comment);
            tvPostDate = itemView.findViewById(R.id.tv_daypost_comment);
            tvLike = itemView.findViewById(R.id.tv_like_comment);
            tvReply = itemView.findViewById(R.id.tv_reply);
            tvReport = itemView.findViewById(R.id.tv_report);
        }
    }
    public interface CallBack{
        public void callBackReply();
    }
}
