package com.example.EnglishBeginner.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EnglishBeginner.DTO.ProcessTopicItem;
import com.example.EnglishBeginner.R;

import java.util.ArrayList;
import java.util.List;

public class ProcessTopic_Adapter extends RecyclerView.Adapter<ProcessTopic_Adapter.ProcessTopicViewHolder> {
    //khai báo các trường dữ liệu
    public List<ProcessTopicItem> processTopicItemList;
    private Context context;

    //hàm constructor
    public ProcessTopic_Adapter(Context context) {
        this.context = context;
        processTopicItemList = new ArrayList<>();
    }

    public void setProcessTopicItemList(List<ProcessTopicItem> processTopicItemList) {
        this.processTopicItemList = processTopicItemList;
        notifyDataSetChanged();
    }
    //khởi tạo view holder
    @NonNull
    @Override
    public ProcessTopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_img, parent, false);
        return new ProcessTopicViewHolder(view);
    }

    //xử lí giao diện, action cho viewholder
    @Override
    public void onBindViewHolder(@NonNull ProcessTopicViewHolder holder, int position) {
        ProcessTopicItem processTopicItem = processTopicItemList.get(position);
        if (processTopicItem.getProgress() < 2) {
            holder.imgProcessTopic.setImageResource(R.drawable.level_blur);
            holder.tvTitleProcessTopic.setText("");
        }
        else {
            holder.imgProcessTopic.setImageResource(R.drawable.level);
            holder.tvTitleProcessTopic.setText(""+processTopicItem.getProcess());
        }
    }

    //trả về số phần tử của list
    @Override
    public int getItemCount() {
        if (processTopicItemList != null) {
            return processTopicItemList.size();
        }
        return 0;
    }
    //class ViewHodler
    public class ProcessTopicViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProcessTopic;
        private TextView tvTitleProcessTopic;

        public ProcessTopicViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProcessTopic = itemView.findViewById(R.id.imgProcessTopic);
            tvTitleProcessTopic = itemView.findViewById(R.id.tvTitleProcessTopic);
        }
    }
}
