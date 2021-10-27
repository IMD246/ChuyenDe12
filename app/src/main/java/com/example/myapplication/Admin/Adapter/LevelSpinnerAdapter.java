package com.example.myapplication.Admin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.Admin.DTO.Level;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class LevelSpinnerAdapter extends ArrayAdapter<Level> {

    private List<Level> listLevel;

    public LevelSpinnerAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<Level> objects) {
        super(context, resource, textViewResourceId, objects);
        listLevel = new ArrayList<>(objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listoptionitem, parent, false);
        }
        Level level = getItem(position);
        TextView tvOption = convertView.findViewById(R.id.tvOptionItem);
        tvOption.setText(String.valueOf(level.getNameLevel()));

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Level> list = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    list.addAll(listLevel);
                } else {
                    String keyword = constraint.toString().toLowerCase().trim();
                    for (Level level : listLevel) {
                        if (Integer.parseInt(keyword) == level.getNameLevel()) {
                            list.add(level);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                filterResults.count = list.size();
                return null;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                addAll((List<Level>) results.values);
                notifyDataSetChanged();
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                String s = Integer.toString(((Level) resultValue).getNameLevel());
                return s;
            }
        };
    }
}
