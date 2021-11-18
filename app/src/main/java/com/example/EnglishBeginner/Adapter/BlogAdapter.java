package com.example.EnglishBeginner.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EnglishBeginner.DTO.Blog;
import com.example.EnglishBeginner.R;

import java.util.ArrayList;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder> {

    Context context;
    ArrayList<Blog> listBlog;

    public BlogAdapter(Context context) {
        this.context = context;
        listBlog = new ArrayList<>();
    }

    public void setListBlog(ArrayList<Blog> listBlog) {
        this.listBlog = listBlog;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.blog_recyclerview_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Blog temp = listBlog.get(position);
//        holder.txt_comment.setText(String.valueOf(temp.getComment()));
//        holder.txt_like.setText(String.valueOf(temp.getLike()));
//        holder.txt_dayOfPost.setText(temp.getDayOfPost());
        holder.txt_title.setText(temp.getTitle());
//        holder.txt_userName.setText(temp.get());
//        holder.txt_view.setText(String.valueOf( temp.getView()));

        holder.ln_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        if (listBlog.size()>0) {
            return listBlog.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img_user,img_blog;
        TextView txt_title,txt_userName,txt_dayOfPost,txt_like,txt_comment,txt_view;
        LinearLayout ln_detail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_user = itemView.findViewById(R.id.blog_img_avatar);
            img_blog = itemView.findViewById(R.id.blog_img_thumnail);
            txt_title = itemView.findViewById(R.id.blog_txt_title);
            txt_userName = itemView.findViewById(R.id.blog_txt_userName);
            txt_dayOfPost = itemView.findViewById(R.id.blog_txt_dayOfPost);
            txt_like = itemView.findViewById(R.id.blog_txt_like);
            txt_comment = itemView.findViewById(R.id.blog_txt_comment);
            txt_view = itemView.findViewById(R.id.blog_txt_view);
            ln_detail = itemView.findViewById(R.id.blog_ln_detail);
        }
    }
}
