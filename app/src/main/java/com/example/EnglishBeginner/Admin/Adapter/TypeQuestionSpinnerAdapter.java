package com.example.EnglishBeginner.Admin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.EnglishBeginner.Admin.DTO.TypeQuestion;
import com.example.EnglishBeginner.R;

import java.util.ArrayList;
import java.util.List;

public class TypeQuestionSpinnerAdapter extends ArrayAdapter<String> {

    private List<String> typeQuestionList;

    public TypeQuestionSpinnerAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<String> objects) {
        super(context, resource, textViewResourceId, objects);
        typeQuestionList = objects;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listoptionitem,parent,false);
        }
        TextView tvOption = convertView.findViewById(R.id.tvOptionItem);
        tvOption.setText(typeQuestionList.get(position));

        return convertView;
    }
    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<String> list = new ArrayList<>();
                if (constraint == null || constraint.length() ==0)
                {
                    list.addAll(typeQuestionList);
                }
                else
                {
                    String keyword = constraint.toString().toLowerCase();
                    for (String s : typeQuestionList)
                    {
                        if (s.toLowerCase().contains(keyword))
                        {
                            list.add(s);
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
                addAll((List<String>)results.values);
                notifyDataSetChanged();
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return ((String) resultValue);
            }
        };
    }
}
