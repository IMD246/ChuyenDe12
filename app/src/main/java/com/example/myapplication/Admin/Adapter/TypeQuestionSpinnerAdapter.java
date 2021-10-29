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

import com.example.myapplication.Admin.DTO.TypeQuestion;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class TypeQuestionSpinnerAdapter extends ArrayAdapter<TypeQuestion> {

    private List<TypeQuestion> typeQuestionList;

    public TypeQuestionSpinnerAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<TypeQuestion> objects) {
        super(context, resource, textViewResourceId, objects);
        typeQuestionList = new ArrayList<>(objects);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView ==null)
        {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listoptionitem,parent,false);
        }
        TypeQuestion typeQuestion = getItem(position);
        TextView tvOption = convertView.findViewById(R.id.tvOptionItem);
        tvOption.setText(String.valueOf(typeQuestion.getTypeQuestionName()));

        return convertView;
    }
    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<TypeQuestion> list = new ArrayList<>();
                if (constraint == null || constraint.length() ==0)
                {
                    list.addAll(typeQuestionList);
                }
                else
                {
                    String keyword = constraint.toString().toLowerCase();
                    for (TypeQuestion typeQuestion : typeQuestionList)
                    {
                        if (typeQuestion.getTypeQuestionName().toLowerCase().contains(keyword))
                        {
                            list.add(typeQuestion);
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
//                addAll((List<TypeQuestion>)results.values);
                notifyDataSetChanged();
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return ((TypeQuestion) resultValue).getTypeQuestionName();
            }
        };
    }
}
