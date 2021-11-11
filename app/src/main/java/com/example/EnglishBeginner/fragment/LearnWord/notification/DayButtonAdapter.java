package com.example.EnglishBeginner.fragment.LearnWord.notification;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.fragment.LearnWord.notification.source.AlarmSqliteHelper;
import com.example.EnglishBeginner.fragment.LearnWord.notification.source.DayItem;

import java.util.ArrayList;

public class DayButtonAdapter extends RecyclerView.Adapter<DayButtonAdapter.ViewHolder> {

    public Context context;
    ArrayList<DayItem> listDay;
    AlarmSqliteHelper alarmSqliteHelper ;
    public DayButtonAdapter(Context context, ArrayList<DayItem> listDay) {
        this.context = context;
        this.listDay = listDay;
        alarmSqliteHelper = new AlarmSqliteHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View ref = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_button_item,parent,false);

        return new ViewHolder(ref);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.btn_day.setText(listDay.get(position).getDay());
        if(listDay.get(holder.getAdapterPosition()).getStatus()==0){
            holder.btn_day.setBackgroundColor(Color.GRAY);
        }else{
            holder.btn_day.setBackgroundColor(Color.GREEN);
        }



        holder.btn_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listDay.get(holder.getAdapterPosition()).getStatus()==0){
                    listDay.get(holder.getAdapterPosition()).setStatus(1);
                    holder.btn_day.setBackgroundColor(Color.GREEN);

                }else {
                    listDay.get(holder.getAdapterPosition()).setStatus(0);
                    holder.btn_day.setBackgroundColor(Color.GRAY);

                }
                alarmSqliteHelper.editDay(listDay.get(holder.getAdapterPosition()));
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return listDay.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        Button btn_day;

        public ViewHolder(View itemView){
            super(itemView);

            btn_day = itemView.findViewById(R.id.alarm_btnDay);

        }
    }
}