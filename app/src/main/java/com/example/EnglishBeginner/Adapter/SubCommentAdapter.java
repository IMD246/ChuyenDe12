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
import com.example.EnglishBeginner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class SubCommentAdapter extends RecyclerView.Adapter<SubCommentAdapter.ViewHolder>{

    Context context;
    List<Comment> listComments;

    public SubCommentAdapter(Context context) {
        this.context = context;
    }

    public void setListComments(List<Comment> listComments) {
        this.listComments = listComments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_recycleview_sub_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = listComments.get(position);
        if (comment.getNameUser().trim().isEmpty()) {
            holder.tvFullName.setText("Unknown");
        } else {
            holder.tvFullName.setText(comment.getNameUser());
        }
        if (!(comment.getUrlImage().trim().isEmpty())) {
            Glide.with(context).load(comment.getUrlImage()).into(holder.imgUser);
        } else {
            holder.imgUser.setVisibility(View.GONE);
        }
        holder.tvContent.setText(comment.getContent());
        holder.tvPostDate.setText(comment.getDayOfPost());
        holder.tvLike.setText(String.valueOf(comment.getLike()));
        holder.imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.tvReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
}
