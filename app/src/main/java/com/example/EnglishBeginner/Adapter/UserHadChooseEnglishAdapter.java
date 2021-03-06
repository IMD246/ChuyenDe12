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

import java.lang.reflect.Array;
import java.net.CacheRequest;
import java.util.ArrayList;

public class UserHadChooseEnglishAdapter extends RecyclerView.Adapter<UserHadChooseEnglishAdapter.ViewHolder> {
    Context context;
    ArrayList<String> listUserHadChoose;
    ArrayList<String> listForUserToChoose;

    notifyData notifyData;

    public ArrayList<String> getListUserHadChoose() {
        return listUserHadChoose;
    }

    public void setNotifyData(notifyData input) {
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
        return new ViewHolder(ref);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (listUserHadChoose != null) {
            holder.btnSelection.setText(listUserHadChoose.get(position));
        }
        holder.cardView.setOnClickListener(v -> {
            listForUserToChoose.add(listUserHadChoose.get(holder.getAdapterPosition()));
            listUserHadChoose.remove(holder.getAdapterPosition());
            notifyData.notifyDataChange();
        });
    }


    @Override
    public int getItemCount() {
        return listUserHadChoose.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //khai b??o c??c ph???n t???
        private TextView btnSelection;
        private CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview_selection_english);
            btnSelection = itemView.findViewById(R.id.selection_english_buttonItem);
        }
    }

    public interface notifyData {
        void notifyDataChange();
    }
}
