package com.example.myapplication.User.LearnWord.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    public Context context;
    ArrayList<HistoryItem> listHistory;

    public HistoryAdapter(Context context, ArrayList<HistoryItem> listHistory) {
        this.context = context;
        this.listHistory = listHistory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View ref = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item,parent,false);

        return new ViewHolder(ref);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txt_time.setText(listHistory.get(position).time);
        holder.txt_word.setText(listHistory.get(position).word);
    }

    @Override
    public int getItemCount() {
        return listHistory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_word,txt_time;

        public ViewHolder(View itemView){
            super(itemView);

            txt_time = itemView.findViewById(R.id.fragment_history_time);
            txt_word = itemView.findViewById(R.id.fragment_history_word);
        }
    }
}
