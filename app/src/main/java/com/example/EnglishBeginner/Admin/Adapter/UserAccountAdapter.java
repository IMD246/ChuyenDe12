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

import com.example.EnglishBeginner.Admin.DTO.UserAccount;
import com.example.EnglishBeginner.R;

import java.util.ArrayList;
import java.util.List;

public class UserAccountAdapter extends RecyclerView.Adapter<UserAccountAdapter.UserAccountViewHolder> implements Filterable {

    private Context context;
    private List<UserAccount> userAccountList;
    private List<UserAccount> userAccountListOld;
    private MyDelegationLevel myDelegationLevel;

    public void setMyDelegationLevel(MyDelegationLevel myDelegationLevel) {
        this.myDelegationLevel = myDelegationLevel;
    }

    public UserAccountAdapter(Context context) {
        this.context = context;
    }

    public void setUserAccountList(List<UserAccount> userAccountList) {
        this.userAccountList = userAccountList;
        userAccountListOld = userAccountList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserAccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.useraccountitem, parent, false);
        return new UserAccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAccountViewHolder holder, int position) {
        UserAccount userAccount = userAccountList.get(position);
        String s = "Đang hoạt động";
        if (userAccount == null) {
            return;
        }
        if (userAccount.getBlock() == true)
        {
            holder.imgBlock.setImageResource(R.drawable.ic_lock);
            s = "Tạm ngưng hoạt động";
        }
        else {
            holder.imgBlock.setImageResource(R.drawable.ic_unlock);
        }
        holder.tvEmail.setText("Email: " + userAccount.getEmail());
        holder.tvStaticUser.setText("Trạng thái: "+s);
        holder.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDelegationLevel != null) {
                    switch (v.getId()) {
                        case R.id.imgBlockUserAccountItem:
                            myDelegationLevel.blockItem(userAccount);
                            break;
                    }
                }
            }
        };
    }

    @Override
    public int getItemCount() {
        if (userAccountList != null) {
            return userAccountList.size();
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
                    userAccountList = userAccountListOld;
                } else {
                    List<UserAccount> list = new ArrayList<>();
                    for (UserAccount userAccount : userAccountList) {
                        if (userAccount.getEmail().trim().toLowerCase().contains(strSearch.toLowerCase())) {
                            list.add(userAccount);
                        }
                    }
                    userAccountList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = userAccountList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                userAccountList = (List<UserAccount>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class UserAccountViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvEmail,tvStaticUser;
        private ImageView imgBlock;
        View.OnClickListener onClickListener;

        public void setOnClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        public UserAccountViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmail = itemView.findViewById(R.id.tvEmailUserAccount);
            tvStaticUser = itemView.findViewById(R.id.tvStaticUserAccount);
            imgBlock = itemView.findViewById(R.id.imgBlockUserAccountItem);
            imgBlock.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onClick(v);
        }
    }

    public interface MyDelegationLevel {
        public void blockItem(UserAccount userAccount);
    }
}
