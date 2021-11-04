package com.example.EnglishBeginner.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EnglishBeginner.R;

import java.util.ArrayList;
import java.util.List;

public class ImageLevel_Adapter extends RecyclerView.Adapter<ImageLevel_Adapter.imageViewHolder> {
    //khai báo các trường dữ liệu
    public List<String> integerList;
    private Context context;

    //hàm constructor
    public ImageLevel_Adapter(Context context) {
        this.context = context;
        integerList = new ArrayList<>();
    }

    public void setIntegerList(List<String> integerList) {
        this.integerList = integerList;
        notifyDataSetChanged();
    }

    //khởi tạo view holder
    @NonNull
    @Override
    public imageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_img, parent, false);
        return new imageViewHolder(view);
    }

    //xử lí giao diện, action cho viewholder
    @Override
    public void onBindViewHolder(@NonNull imageViewHolder holder, int position) {

        //xử lí khi click item learn:
        holder.textView.setText(integerList.get(position));
    }

    //trả về số phần tử của list
    @Override
    public int getItemCount() {
        if (integerList != null) {
            return integerList.size();
        }
        return 0;
    }
    //class ViewHodler
    public class imageViewHolder extends RecyclerView.ViewHolder {
//        private ImageView imgLesson;

        private TextView textView;

        public imageViewHolder(@NonNull View itemView) {
            super(itemView);
//            imgLesson = itemView.findViewById(R.id.img_item_rscDialog);
            textView = itemView.findViewById(R.id.img_item_rscDialog);
        }
    }
}
