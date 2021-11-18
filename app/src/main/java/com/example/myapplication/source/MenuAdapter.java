package com.example.myapplication.source;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    Context context;
    ArrayList<String> listMenu;
    setAdapterCallBack setAdapterOfListBlog;
    RecyclerView rcl;
    private Boolean check = true;
    int previousPosition = 0;
    public void setSetAdapterOfListBlog(setAdapterCallBack setAdapterOfListBlog) {
        this.setAdapterOfListBlog = setAdapterOfListBlog;
    }

    public MenuAdapter(Context context, ArrayList<String> listMenu,RecyclerView rcl) {
        this.context = context;
        this.listMenu = listMenu;
        this.rcl = rcl;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.blogmenu_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txt_title.setText(listMenu.get(position));
        if (check)
        {
            holder.ln_menuBlog.setBackgroundColor(Color.BLUE);
            check = false;
        }
        holder.txt_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (previousPosition == holder.getAdapterPosition())
                {
                    view = rcl.findViewHolderForAdapterPosition(previousPosition).itemView;
                    holder.ln_menuBlog = view.findViewById(R.id.lnItemMenuBlog);
                    holder.ln_menuBlog.setBackgroundColor(Color.BLUE);
                    previousPosition = holder.getAdapterPosition();
                }
                else
                {
                    view = rcl.findViewHolderForAdapterPosition(previousPosition).itemView;
                    holder.ln_menuBlog = view.findViewById(R.id.lnItemMenuBlog);
                    holder.ln_menuBlog.setBackgroundColor(Color.WHITE);

                    view = rcl.findViewHolderForAdapterPosition(holder.getAdapterPosition()).itemView;
                    holder.ln_menuBlog = view.findViewById(R.id.lnItemMenuBlog);
                    holder.ln_menuBlog.setBackgroundColor(Color.BLUE);
                    previousPosition = holder.getAdapterPosition();
                }
                setAdapterOfListBlog.setAdapter(listMenu.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listMenu!=null) {
            return listMenu.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txt_title;
        LinearLayout ln_menuBlog;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_title = itemView.findViewById(R.id.txt_menu_item);
            ln_menuBlog = itemView.findViewById(R.id.lnItemMenuBlog);

        }
    }
  public interface setAdapterCallBack{
        void setAdapter(String typeBlog);
  }
}
