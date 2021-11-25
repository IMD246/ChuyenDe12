package com.example.EnglishBeginner.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.EnglishBeginner.DTO.Blog;
import com.example.EnglishBeginner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder> {

    Context context;
    List<Blog> listBlog;
    DatabaseReference databaseReference;

    public BlogAdapter(Context context) {
        this.context = context;
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
    }

    public void setListBlog(List<Blog> listBlog) {
        this.listBlog = listBlog;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.blog_recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Blog temp = listBlog.get(position);
        holder.txt_comment.setText(String.valueOf(temp.getComment()));
        holder.txt_like.setText(String.valueOf(temp.getLike()));
        holder.txt_dayOfPost.setText(temp.getDayOfPost());
        holder.txt_title.setText(temp.getTitle());
        holder.txt_view.setText(String.valueOf(temp.getView()));
        if (!(temp.getUrlImage().trim().isEmpty())) {
            Glide.with(context).load(temp.getUrlImage()).into(holder.img_blog);
        } else {
            holder.img_blog.setVisibility(View.GONE);
        }
        databaseReference.child(temp.getIdUser()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                HashMap<String, Object> hashMap = new HashMap<>();
                for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                    hashMap.put(dataSnapshot.getKey(), dataSnapshot.getValue());
                }
                String urlImage = hashMap.get("imageUser").toString();
                String fullName = hashMap.get("fullname").toString();
                if (fullName.trim().isEmpty())
                {
                    holder.txt_userName.setText("Unknown");
                }
                else {
                    holder.txt_userName.setText(hashMap.get("fullname").toString());
                }
                if (!(urlImage.trim().isEmpty())) {
                    Glide.with(context).load(urlImage).into(holder.img_user);
                } else {
                    holder.img_user.setVisibility(View.GONE);
                }
            }
        });
        holder.ln_detail.setOnClickListener(view -> {

        });
    }

    @Override
    public int getItemCount() {
        if (listBlog.size() > 0) {
            return listBlog.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_user, img_blog;
        TextView txt_title, txt_userName, txt_dayOfPost, txt_like, txt_comment, txt_view;
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
