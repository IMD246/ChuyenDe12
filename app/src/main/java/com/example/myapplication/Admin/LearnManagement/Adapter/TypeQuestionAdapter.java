package com.example.myapplication.Admin.LearnManagement.Adapter;

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

import com.example.myapplication.Admin.LearnManagement.DTO.TypeQuestion;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class TypeQuestionAdapter extends RecyclerView.Adapter<TypeQuestionAdapter.TypeQuestionViewHolder> implements Filterable {

    private Context context;
    private List<TypeQuestion> typeQuestionList;
    private List<TypeQuestion>typeQuestionListOld;
    private MyDelegationLevel myDelegationLevel;

    public void setMyDelegationLevel(MyDelegationLevel myDelegationLevel) {
        this.myDelegationLevel = myDelegationLevel;
    }

    public TypeQuestionAdapter(Context context) {
        this.context = context;
    }

    public void setTypeQuestionList(List<TypeQuestion> typeQuestionList) {
        this.typeQuestionList = typeQuestionList;
        typeQuestionListOld = typeQuestionList;
    }

    @NonNull
    @Override
    public TypeQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.typequestionitem,parent,false);
        return new TypeQuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeQuestionViewHolder holder, int position) {
        TypeQuestion typeQuestion = typeQuestionList.get(position);
        if (typeQuestion==null)
        {return;}
        holder.tvNumber.setText(String.valueOf(position+1));
        holder.tvName.setText("TypeQuestion: "+typeQuestion.getTypeQuestionName());
        holder.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDelegationLevel != null)
                {
                    switch (v.getId())
                    {
                        case R.id.imgEdit:myDelegationLevel.editItem(typeQuestion);
                            break;
                        case R.id.imgDelete:myDelegationLevel.deleteItem(typeQuestion);
                            break;
                    }
                }
            }
        };
    }

    @Override
    public int getItemCount() {
        if (typeQuestionList !=null)
        {
            return typeQuestionList.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty() || strSearch.length() == 0)
                {
                    typeQuestionList = typeQuestionListOld;
                }
                else
                {
                    List<TypeQuestion> list = new ArrayList<>();
                    for (TypeQuestion typeQuestion : typeQuestionList)
                    {
                        if (typeQuestion.getTypeQuestionName().toLowerCase().contains(strSearch.toLowerCase()))
                        {
                            list.add(typeQuestion);
                        }
                    }
                    typeQuestionList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = typeQuestionList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                typeQuestionList = (List<TypeQuestion>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class TypeQuestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvNumber,tvName;
        private ImageView imgDelete,imgEdit;
        View.OnClickListener onClickListener;

        public void setOnClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        public TypeQuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvTypeQuestion);
            tvNumber = itemView.findViewById(R.id.tvNumberTypeQuestion);
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
    public interface MyDelegationLevel
    {
        public void editItem(TypeQuestion typeQuestion);
        public void deleteItem(TypeQuestion typeQuestion);
    }
}