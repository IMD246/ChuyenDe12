package com.example.EnglishBeginner.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EnglishBeginner.R;

import java.util.ArrayList;

public class UserChooseEnglishAdapter extends RecyclerView.Adapter<UserChooseEnglishAdapter.ViewHolder>  {
    Context context;
    ArrayList<String> listForUserToChoose;
    ArrayList<String> listUserHadChoose;

    UserChooseEnglishAdapter.notifyData notifyData;

    public void setNotifyData(notifyData input){
        this.notifyData = input;
    }
    public UserChooseEnglishAdapter(Context context, ArrayList<String> listUserHadChoose , ArrayList<String> listForUserToChoose) {
        this.context = context;
        this.listUserHadChoose = listUserHadChoose;
        this.listForUserToChoose = listForUserToChoose;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View ref = LayoutInflater.from(context).inflate(R.layout.layout_fragment_test_selection_english_item, parent, false);

        return new ViewHolder(ref);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(listForUserToChoose!=null){
            holder.btnAnswer.setText(listForUserToChoose.get(position));
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listUserHadChoose.add(listForUserToChoose.get(holder.getAdapterPosition()));
                listForUserToChoose.remove(holder.getAdapterPosition());
                notifyData.notifyDataChange();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listForUserToChoose.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //khai báo các phần từ
        private TextView btnAnswer;
        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            btnAnswer = itemView.findViewById(R.id.selection_english_buttonItem);
            cardView = itemView.findViewById(R.id.cardview_selection_english);
        }
    }
    public interface notifyData{
        void notifyDataChange();
    }
}
