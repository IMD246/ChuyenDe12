package com.example.EnglishBeginner.learn.testing.source;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EnglishBeginner.R;

import java.util.ArrayList;

public class UserHadChooseEnglishAdapter extends RecyclerView.Adapter<UserHadChooseEnglishAdapter.ViewHolder> {

    Context context;
    ArrayList<String> listUserHadChoose;
    ArrayList<String> listForUserToChoose;

     notifyData notifyData;
    public void setNotifyData(notifyData input){
         this.notifyData = input;
     }
    public UserHadChooseEnglishAdapter(Context context, ArrayList<String> listUserHadChoose, ArrayList<String> listForUserToChoose) {
        this.context = context;
        this.listUserHadChoose = listUserHadChoose;
        this.listForUserToChoose = listForUserToChoose;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View ref = LayoutInflater.from(context).inflate(R.layout.layout_fragment_test_selection_english_item, parent, false);

        return new UserHadChooseEnglishAdapter.ViewHolder(ref);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(listUserHadChoose!=null){
            holder.btnSelection.setText(listUserHadChoose.get(position));
        }


        holder.btnSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listForUserToChoose.add( listUserHadChoose.get(holder.getAdapterPosition()));
                listUserHadChoose.remove(holder.getAdapterPosition());
                notifyData.notifyDataChange();

            }
        });
    }


    @Override
    public int getItemCount() {
        return listUserHadChoose.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //khai báo các phần từ
        Button btnSelection;


        public ViewHolder(View itemView) {
            super(itemView);

            btnSelection = itemView.findViewById(R.id.selection_english_buttonItem);
        }
    }
    public interface notifyData{
         void notifyDataChange();
    }
}
