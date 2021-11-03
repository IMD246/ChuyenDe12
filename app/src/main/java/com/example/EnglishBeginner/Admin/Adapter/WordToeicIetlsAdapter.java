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

import com.example.EnglishBeginner.Admin.DTO.Word;
import com.example.EnglishBeginner.DEFAULTVALUE;
import com.example.EnglishBeginner.R;

import java.util.ArrayList;
import java.util.List;

public class WordToeicIetlsAdapter extends RecyclerView.Adapter<WordToeicIetlsAdapter.WordToeicIetlsViewHolder> implements Filterable {

    private Context context;
    private List<Word> wordList;
    private List<Word> wordListOld;

    private MyDelegationLevel myDelegationLevel;

    public void setMyDelegationLevel(MyDelegationLevel myDelegationLevel) {
        this.myDelegationLevel = myDelegationLevel;
    }

    public WordToeicIetlsAdapter(Context context) {
        this.context = context;
    }

    public void setWordList(List<Word> wordList) {
        this.wordList = wordList;
        wordListOld = wordList;
    }

    public void setListDependOnTypeWord(@NonNull String typeWord) {
        if (wordList.size() == 0) {
            wordList = wordListOld;
        }
        if (typeWord.equalsIgnoreCase(DEFAULTVALUE.TYPEWORD)) {
            wordList = wordListOld;
        } else {
            List<Word> list = new ArrayList<>();
            for (Word word : wordListOld) {
                if (word.getTypeWord().equalsIgnoreCase(typeWord)) {
                    list.add(word);
                }
            }
            wordList = list;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WordToeicIetlsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.toeicietlsitem, parent, false);
        return new WordToeicIetlsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordToeicIetlsViewHolder holder, int position) {
        Word word = wordList.get(position);
        if (word == null) {
            return;
        }
        holder.tvWord.setText(word.getWord() + " (" + word.getTypeWord() + ")");
        holder.tvNumberList.setText(String.valueOf(position + 1));
        holder.tvMeaning.setText(word.getMeaning());

        holder.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDelegationLevel != null) {
                    switch (v.getId()) {
                        case R.id.imgEdit_Word:
                            myDelegationLevel.editItem(word);
                            break;
                        case R.id.imgDelete_Word:
                            myDelegationLevel.deleteItem(word);
                            break;
                    }
                }
            }
        };
    }

    @Override
    public int getItemCount() {
        if (wordList != null) {
            return wordList.size();
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
                    if (wordList.size() == 0) {
                        wordList = wordListOld;
                    }
                } else {
                    List<Word> list = new ArrayList<>();
                    for (Word word : wordList) {
                        if (word.getWord().toLowerCase().contains(strSearch.toLowerCase())) {
                            list.add(word);
                        }
                    }
                    wordList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = wordList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                wordList = (List<Word>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class WordToeicIetlsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvWord, tvMeaning, tvNumberList;
        private ImageView imgDelete, imgEdit;
        View.OnClickListener onClickListener;

        public void setOnClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        public WordToeicIetlsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWord = itemView.findViewById(R.id.tvWordToeicIetls);
            tvMeaning = itemView.findViewById(R.id.tvMeaning);
            tvNumberList = itemView.findViewById(R.id.tvNumberListToeicIetls);
            imgDelete = itemView.findViewById(R.id.imgDelete_Word);
            imgDelete.setOnClickListener(this);
            imgEdit = itemView.findViewById(R.id.imgEdit_Word);
            imgEdit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onClick(v);
        }
    }

    public interface MyDelegationLevel {
        public void editItem(Word word);

        public void deleteItem(Word word);
    }
}
