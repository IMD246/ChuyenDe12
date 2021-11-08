package com.example.EnglishBeginner.Admin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.EnglishBeginner.Admin.DTO.Level;
import com.example.EnglishBeginner.R;

import java.util.ArrayList;
import java.util.List;

public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.LevelViewHolder> implements Filterable {

    private Context context;
    private List<Level> listLevel;
    private List<Level> listLevelOld;
    private MyDelegationLevel myDelegationLevel;

    public void setMyDelegationLevel(MyDelegationLevel myDelegationLevel) {
        this.myDelegationLevel = myDelegationLevel;
    }

    public List<Level> getListLevel() {
        return listLevel;
    }

    public LevelAdapter(Context context) {
        this.context = context;
    }

    public void setListLevel(List<Level> listLevel) {
        this.listLevel = listLevel;
        listLevelOld = listLevel;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LevelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.levelitem, parent, false);
        return new LevelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LevelViewHolder holder, int position) {
        Level level = listLevel.get(position);
        if (level == null) {
            return;
        }
        if (level.getUrlImage().isEmpty()) {
        } else {
            Glide.with(context).load(level.getUrlImage()).into(holder.imgLevel);
        }
        holder.tvName.setText("Level: " + String.valueOf(level.getNameLevel()));
        holder.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDelegationLevel != null) {
                    switch (v.getId()) {
                        case R.id.imgEdit:
                            myDelegationLevel.editItem(level);
                            break;
                        case R.id.imgDelete:
                            myDelegationLevel.deleteItem(level);
                            break;
                    }
                }
            }
        };
    }

    @Override
    public int getItemCount() {
        if (listLevel != null) {
            return listLevel.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty() || strSearch.length() == 0) {
                    listLevel = listLevelOld;
                } else {
                    List<Level> list = new ArrayList<>();
                    for (Level level : listLevelOld) {
                        if (level.getNameLevel() == Integer.parseInt(strSearch)) {
                            list.add(level);
                        }
                    }
                    listLevel = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listLevel;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listLevel = (List<Level>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class LevelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvName;
        private ImageView imgDelete, imgEdit,imgLevel;
        View.OnClickListener onClickListener;

        public void setOnClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        public LevelViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvTitleLevel);
            imgLevel = itemView.findViewById(R.id.imgLevel);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            imgDelete.setOnClickListener(this);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgEdit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onClick(v);
        }
    }

    public interface MyDelegationLevel {
        public void editItem(Level level);

        public void deleteItem(Level level);
    }
}
