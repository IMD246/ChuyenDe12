package com.example.EnglishBeginner.fragment.LearnWord.vocubulary;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.fragment.LearnWord.history.HistoryScreen;
import com.example.EnglishBeginner.fragment.LearnWord.notification.AlarmScreen;
import com.example.EnglishBeginner.fragment.LearnWord.practice.PracticeScreen;
import com.example.EnglishBeginner.fragment.LearnWord.saveWord.SaveScreen;
import com.example.EnglishBeginner.fragment.LearnWord.word.WordScreen;

import java.util.ArrayList;

public class VocubularyAapter extends RecyclerView.Adapter<VocubularyAapter.ViewHolder> {

    public Context context;
    ArrayList<VocubularyItem> listVocubularyItem;

    public VocubularyAapter(Context context, ArrayList<VocubularyItem> listVocubularyItem) {
        this.context = context;
        this.listVocubularyItem = listVocubularyItem;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View ref = LayoutInflater.from(context).inflate(R.layout.vocubulary_list_item,parent,false);

        return new ViewHolder(ref);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txt_VocubularyText.setText(listVocubularyItem.get(position).getWordText());
        holder.txt_VocubularyImage.setImageResource(listVocubularyItem.get(position).getWordImage());

        //gan su kien chuyen man hinh
        if(position == 0){
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Intent intent = new Intent(context, AlarmScreen.class);
                   Intent intent = new Intent(context, SaveScreen.class);
                   context.startActivity(intent);
                }
            });
        }
        else if(position == 1){
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, HistoryScreen.class);
                    context.startActivity(intent);

                }
            });
        }
        else if(position == 2){
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WordScreen.class);
                    context.startActivity(intent);

                }
            });
        }

        else if(position == 3){
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PracticeScreen.class);
                    context.startActivity(intent);

                }
            });
        }
        else if(position == 4){
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AlarmScreen.class);
                    context.startActivity(intent);

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listVocubularyItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_VocubularyText;
        ImageView txt_VocubularyImage;
        CardView container;
        public ViewHolder(View itemView){
            super(itemView);
            container = itemView.findViewById(R.id.vocubulary_linearContainer);
        txt_VocubularyText = itemView.findViewById(R.id.vocubulary_ItemText);
        txt_VocubularyImage = itemView.findViewById(R.id.vocubulary_ItemImage);
        }
    }

}
