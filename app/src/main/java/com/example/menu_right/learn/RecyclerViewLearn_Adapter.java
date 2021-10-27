package com.example.menu_right.learn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menu_right.R;

import java.util.ArrayList;

public class RecyclerViewLearn_Adapter extends RecyclerView.Adapter<RecyclerViewLearn_Adapter.LearnViewHolder> {
    //khai báo các trường dữ liệu
    public ArrayList<Learn> learnArrayList;
    public Interface_Learn interface_learn;
    private Context context;

    //hàm constructor
    public RecyclerViewLearn_Adapter(Context context, ArrayList<Learn> learnArrayList, Interface_Learn interface_learn) {
        this.context = context;
        this.learnArrayList = learnArrayList;
        this.interface_learn = interface_learn;
    }

    //khởi tạo view holder
    @NonNull
    @Override
    public LearnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_recycleview_learn, parent, false);
        return new LearnViewHolder(view);
    }

    //xử lí giao diện, action cho viewholder
    @Override
    public void onBindViewHolder(@NonNull LearnViewHolder holder, int position) {
        Learn learn = learnArrayList.get(position);
        if (learn == null) {
            return;
        }
        holder.tvtitle.setText(learn.getTitle());
        //xử lí khi click item learn:
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interface_learn.onClickItemLearn(learn);
            }
        });
    }

    //trả về số phần tử của list
    @Override
    public int getItemCount() {
        if (learnArrayList != null) {
            return learnArrayList.size();
        }
        return 0;
    }

    //class ViewHodler
    public class LearnViewHolder extends RecyclerView.ViewHolder {
        private TextView tvtitle;
        private CardView layout;

        public LearnViewHolder(@NonNull View itemView) {
            super(itemView);
            tvtitle = itemView.findViewById(R.id.tv_title);
            layout = itemView.findViewById(R.id.cardview_item_learn);
        }
    }
    public interface Interface_Learn {
        public void onClickItemLearn(Learn learn);
    }
}
