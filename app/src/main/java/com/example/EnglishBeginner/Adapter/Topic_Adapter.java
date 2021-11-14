package com.example.EnglishBeginner.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.EnglishBeginner.DAO.DAOProcessUser;
import com.example.EnglishBeginner.DTO.ProcessTopicItem;
import com.example.EnglishBeginner.DTO.Topic;
import com.example.EnglishBeginner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Topic_Adapter extends RecyclerView.Adapter<Topic_Adapter.TopicViewHolder> {
    //khai báo các trường dữ liệu
    public List<Topic> topicList;
    public Interface_Learn interface_learn;
    private final Context context;
    private String uid;
    //hàm constructor
    public Topic_Adapter(Context context) {
        this.context = context;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTopicList(List<Topic> topicList) {
        this.topicList = topicList;
        notifyDataSetChanged();
    }

    public void setInterface_learn(Interface_Learn interface_learn) {
        this.interface_learn = interface_learn;
    }

    //khởi tạo view holder
    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_topic, parent, false);
        return new TopicViewHolder(view);
    }
    //xử lí giao diện, action cho viewHolder
    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        Topic topic = topicList.get(position);
        if (topic == null) {
            return;
        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("listProcessUser/"+uid+"/listTopic/"+topic.getId()+"/listProcess");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int dem=0;
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren())
                {
                    ProcessTopicItem processTopicItem = dataSnapshot1.getValue(ProcessTopicItem.class);
                    if (processTopicItem.getProgress() >= 2)
                    {
                        dem++;
                    }
                }
                holder.progressBar.setProgress(dem,true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (!(topic.getUrlImage().trim().isEmpty())) {
            Glide.with(context).load(topic.getUrlImage()).into(holder.imgTopic);
        }
        holder.tvNameTopic.setText(topic.getNameTopic());
        //xử lí khi click item learn:
        holder.layout.setOnClickListener(v -> interface_learn.createAlertDialog(topic));
    }

    //trả về số phần tử của list
    @Override
    public int getItemCount() {
        return topicList.size();
    }

    //class ViewHolder
    public static class TopicViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgTopic;
        private final TextView tvNameTopic;
        private final LinearLayout layout;
        private ProgressBar progressBar;

        public TopicViewHolder(View itemView) {
            super(itemView);
            imgTopic = itemView.findViewById(R.id.imgTopic);
            progressBar = itemView.findViewById(R.id.learnTopic_progress_bar);
            tvNameTopic = itemView.findViewById(R.id.tvNameTopic);
            layout = itemView.findViewById(R.id.layout_btn_lesson);
        }
    }

    public interface Interface_Learn {

        void createAlertDialog(Topic topic);
    }
}
