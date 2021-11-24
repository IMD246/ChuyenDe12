package com.example.EnglishBeginner.Admin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.EnglishBeginner.Admin.DTO.Blog;
import com.example.EnglishBeginner.R;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogViewHolder> {

    // Fields
    private Context context;
    private List<Blog> blogList;
    private StorageReference firebaseStorage;

    private MyDelegationLevel myDelegationLevel;

    //Constructor
    public void setMyDelegationLevel(MyDelegationLevel myDelegationLevel) {
        this.myDelegationLevel = myDelegationLevel;

    }
    //Properties
    public BlogAdapter(Context context) {
        this.context = context;
        blogList = new ArrayList<>();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setBlogList(List<Blog> BlogList) {
        this.blogList = BlogList;
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_item, parent, false);
        return new BlogViewHolder(view);
    }
    // đổ dữ liệu vào view dữ liệu và xử lý view
    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {
        Blog blog = blogList.get(position);
        if (blog == null) {
            return;
        }
        if (blog.getUrl_image().isEmpty() || blog.getUrl_image().trim().length() == 0) {
            holder.imgBlogThumnail.setVisibility(View.GONE);
        } else {
            holder.imgBlogThumnail.setVisibility(View.VISIBLE);
            Glide.with(context).load(blog.getUrl_image()).into(holder.imgBlogThumnail);
        }
        holder.txt_userName.setText("Câu trả lời: " + blog.getNameUser());
        holder.txt_title.setText("Câu trả lời: " + blog.getTitle());
        holder.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDelegationLevel != null) {
                    switch (v.getId()) {
                        case R.id.img_blogApply:
                            myDelegationLevel.applyBlog(blog);
                            break;
                        case R.id.img_blogNotApply:
                            myDelegationLevel.notApplyBlog(blog);
                            break;
                    }
                }
            }
        };
    }

    @Override
    public int getItemCount() {
        if (blogList != null) {
            return blogList.size();
        }
        return 0;
    }
    //Tạo View Holder và ánh xạ các view con và sử dụng
    public static class BlogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //Fields
        private final TextView txt_userName,txt_title;
        private final ImageView imgBlogThumnail;
        View.OnClickListener onClickListener;
        //Properties
        public void setOnClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }
        //Constructor
        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBlogThumnail = itemView.findViewById(R.id.img_blogThumnail);
            txt_userName = itemView.findViewById(R.id.tv_blogUserName);
            txt_title = itemView.findViewById(R.id.tv_blogTitle);
            ImageView btn_apply = itemView.findViewById(R.id.img_blogApply);
            ImageView btn_notApply = itemView.findViewById(R.id.img_blogNotApply);

            btn_apply.setOnClickListener(this);
            btn_notApply.setOnClickListener(this);
        }
        //set điều kiện onclick cho các view
        @Override
        public void onClick(View v) {
            onClickListener.onClick(v);
        }
    }
    // tạo một interface để định nghĩa hàm sau
    public interface MyDelegationLevel {
        public void applyBlog(Blog Blog);

        public void notApplyBlog(Blog Blog);
    }
}
