package com.example.myapplication.Admin.LearnManagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class LearnItemAdapter extends RecyclerView.Adapter<LearnItemAdapter.LearnItemHolder>{
    private Context mcontext;
    private List<LearnItem>learnItemList;
    private MyLearnItemInterface delegation;

    public void setDelegation(MyLearnItemInterface delegation) {
        this.delegation = delegation;
    }

    public LearnItemAdapter(Context mcontext) {
        this.mcontext = mcontext;
    }

    public void setLearnItemList(List<LearnItem> learnItemList) {
        this.learnItemList = learnItemList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public LearnItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.learnitem,parent,false);
        return new LearnItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LearnItemHolder holder, int position) {
        final LearnItem learnItem = learnItemList.get(position);
        if (learnItem == null)
        {
            return;
        }
        holder.imageView.setImageResource(learnItem.getImageItem());
        holder.name.setText(learnItem.getName());
        holder.rlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegation != null) {
                    delegation.Transaction(learnItem);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        if (learnItemList != null)
        {
            return learnItemList.size();
        }
        return 0;
    }

    public class LearnItemHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView name;
        private RelativeLayout rlt;
        public LearnItemHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imgItem);
            name = itemView.findViewById(R.id.nameItem);
            rlt = itemView.findViewById(R.id.rltlearnitem);
        }
    }
    public interface MyLearnItemInterface{
        public void Transaction(LearnItem learn);
    }
}
