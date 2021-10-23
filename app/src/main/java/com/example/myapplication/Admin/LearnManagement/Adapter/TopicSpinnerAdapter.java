package com.example.myapplication.Admin.LearnManagement.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.Admin.LearnManagement.DTO.Level;
import com.example.myapplication.Admin.LearnManagement.DTO.Topic;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class TopicSpinnerAdapter extends ArrayAdapter<Topic> {

    private List<Topic> topicList;

    public TopicSpinnerAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<Topic> objects) {
        super(context, resource, textViewResourceId, objects);
        topicList = new ArrayList<>(objects);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView ==null)
        {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listoptionitem,parent,false);
        }
        Topic topic = getItem(position);
        TextView tvOption = convertView.findViewById(R.id.tvOptionItem);
        tvOption.setText(String.valueOf(topic.getNameTopic()));

        return convertView;
    }
    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Topic> list = new ArrayList<>();
                if (constraint == null || constraint.length() ==0)
                {
                    list.addAll(topicList);
                }
                else
                {
                    String keyword = constraint.toString().toLowerCase();
                    for (Topic topic : topicList)
                    {
                        if (topic.getNameTopic().toLowerCase().contains(keyword))
                        {
                            list.add(topic);
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
                addAll((List<Topic>)results.values);
                notifyDataSetChanged();
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return ((Topic) resultValue).getNameTopic();
            }
        };
    }
}
