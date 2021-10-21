package com.example.myapplication.Admin.LearnManagement;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Admin.LearnManagement.DAO.DAOImageStorage;
import com.example.myapplication.DEFAULTVALUE;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> implements Filterable {

    private Context context;
    private List<Topic> topicList;
    private List<Topic>topicListOld;
    private StorageReference firebaseStorage;

    private MyDelegationLevel myDelegationLevel;

    public void setMyDelegationLevel(MyDelegationLevel myDelegationLevel) {
        this.myDelegationLevel = myDelegationLevel;
    }

    public TopicAdapter(Context context) {
        this.context = context;
    }

    public void setTopicList(List<Topic> topicList) {
        this.topicList = topicList;
        topicListOld = topicList;
        notifyDataSetChanged();
    }
    public void setTopicListDependOnLevel(@NonNull String level)
    {
        if (level.equals(DEFAULTVALUE.LEVELLABEL.trim()))
        {
            topicList = topicListOld;
        }
        else
        {
            List<Topic> list = new ArrayList<>();
            for (Topic topic : topicListOld)
            {
                if (topic.getLevel() == Integer.parseInt(level.toLowerCase().trim()))
                {
                    list.add(topic);
                }
            }
            topicList = list;
        }
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topicitem,parent,false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        Topic topic = topicList.get(position);
        if (topic==null)
        {return;}
        try {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/Topic "+topic.getId());
            File file = File.createTempFile("tempfile",".jpg");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    holder.imgTopic.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        holder.tvLevel.setText("Level: "+String.valueOf(topic.getLevel()));
        holder.tvNameTopic.setText("Topic: "+topic.getNameTopic());

        holder.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDelegationLevel != null)
                {
                    switch (v.getId())
                    {
                        case R.id.imgEdit_Topic:myDelegationLevel.editItem(topic);
                            break;
                        case R.id.imgDelete_Topic:myDelegationLevel.deleteItem(topic);
                            break;
                    }
                }
            }
        };
    }

    @Override
    public int getItemCount() {
        if (topicList !=null)
        {
            return topicList.size();
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
                    topicList = topicListOld;
                }
                else
                {
                    List<Topic> list = new ArrayList<>();
                    for (Topic topic : topicList)
                    {
                        if (topic.getNameTopic().toLowerCase().contains(strSearch.toLowerCase()))
                        {
                            list.add(topic);
                        }
                    }
                    topicList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = topicList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                topicList = (List<Topic>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class TopicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvLevel,tvNameTopic;
        private ImageView imgDelete,imgEdit,imgTopic;
        View.OnClickListener onClickListener;

        public void setOnClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            imgTopic = itemView.findViewById(R.id.imgvTopic);
            tvNameTopic = itemView.findViewById(R.id.tvTopic);
            tvLevel = itemView.findViewById(R.id.tvTopic_Level);
            imgDelete = itemView.findViewById(R.id.imgDelete_Topic);
            imgDelete.setOnClickListener(this);
            imgEdit = itemView.findViewById(R.id.imgEdit_Topic);
            imgEdit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onClick(v);
        }
    }
    public interface MyDelegationLevel
    {
        public void editItem(Topic topic);
        public void deleteItem(Topic topic);
    }
}
