package com.example.EnglishBeginner.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EnglishBeginner.DTO.DEFAULTVALUE;
import com.example.EnglishBeginner.DTO.Word;
import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.fragment.LearnWord.history.HistoryItem;
import com.example.EnglishBeginner.fragment.LearnWord.history.source.HistorySqliteDataHelper;
import com.example.EnglishBeginner.fragment.LearnWord.word.WordItemDetail;
import com.example.EnglishBeginner.fragment.LearnWord.word.source.SqlLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class WordToeicIetlsAdapter extends RecyclerView.Adapter<WordToeicIetlsAdapter.WordToeicIetlsViewHolder> implements Filterable {

    private Context context;
    private List<Word> wordList;
    private List<Word> wordListOld;
    private String typeWordOld = DEFAULTVALUE.ALL;
    private String keyWord = "";

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
        typeWordOld = typeWord;
        if (wordList.size() == 0) {
            wordList = wordListOld;
        }
        if (typeWord.equalsIgnoreCase(DEFAULTVALUE.ALL) && keyWord.isEmpty()) {
            wordList = wordListOld;
        } else {
            List<Word> list = new ArrayList<>();
            for (Word word : wordListOld) {
                if (!keyWord.isEmpty()) {
                    if (typeWord.equalsIgnoreCase(DEFAULTVALUE.ALL)) {
                        if (word.getWord().toLowerCase().contains(keyWord.toLowerCase())){
                            list.add(word);
                        }
                    }
                    else
                    {
                        if (word.getWord().toLowerCase().contains(keyWord.toLowerCase())
                                && word.getTypeWord().equalsIgnoreCase(typeWord)) {
                            list.add(word);
                        }
                    }
                } else {
                    if (!typeWord.equalsIgnoreCase(DEFAULTVALUE.ALL)) {
                        if (word.getTypeWord().equalsIgnoreCase(typeWord)) {
                            list.add(word);
                        }
                    }
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

        holder.onClickListener = v -> {
            if (myDelegationLevel != null) {
                switch (v.getId()) {
                    case R.id.imgSave_Word:
                        myDelegationLevel.saveItem(word);
                        break;
                    case R.id.imgSpeech_Word:
                        myDelegationLevel.speechItem(word);
                        break;
                }
            }
        };
        holder.layout_toeicIelts.setOnClickListener(v -> {
            HistorySqliteDataHelper historySqliteDataHelper = new HistorySqliteDataHelper(context);
            HistoryItem is=
                    new HistoryItem(wordList.get(holder.getAdapterPosition()).getId()+"",
                            wordList.get(holder.getAdapterPosition()).getWord(),
                            HistoryItem.getDateTimeNow());
            historySqliteDataHelper.addHistory(is);
            SqlLiteHelper sqlLiteHelperOfWord = new SqlLiteHelper(context,"Dictionary.db",3);

            Bundle bundle = new Bundle();
            bundle.putString("htmlText", sqlLiteHelperOfWord.getHtmlTextByWord(wordList.get(holder.getAdapterPosition()).getWord()));

            Intent it = new Intent(context, WordItemDetail.class);
            it.putExtras(bundle);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(it);
        });
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
                keyWord = constraint.toString();
                if (strSearch.isEmpty() && typeWordOld.equalsIgnoreCase(DEFAULTVALUE.ALL)) {
                    if (wordList.size() == 0) {
                        wordList = wordListOld;
                    }
                } else {
                    List<Word> list = new ArrayList<>();
                    for (Word word : wordList) {
                        if (!keyWord.isEmpty()) {
                            if (typeWordOld.equalsIgnoreCase(DEFAULTVALUE.ALL)) {
                                if (word.getWord().toLowerCase().contains(keyWord.toLowerCase())){
                                    list.add(word);
                                }
                            }
                            else
                            {
                                if (word.getWord().toLowerCase().contains(keyWord.toLowerCase())
                                        && word.getTypeWord().equalsIgnoreCase(typeWordOld)) {
                                    list.add(word);
                                }
                            }
                        } else {
                            if (!typeWordOld.equalsIgnoreCase(DEFAULTVALUE.ALL)) {
                                if (word.getTypeWord().equalsIgnoreCase(typeWordOld)) {
                                    list.add(word);
                                }
                            }
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
        LinearLayout layout_toeicIelts;
        private ImageView imgSave, imgSpeech;
        View.OnClickListener onClickListener;

        public void setOnClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        public WordToeicIetlsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWord = itemView.findViewById(R.id.tvWordToeicIetls);
            tvMeaning = itemView.findViewById(R.id.tvMeaning);
            tvNumberList = itemView.findViewById(R.id.tvNumberListToeicIetls);
            imgSpeech = itemView.findViewById(R.id.imgSpeech_Word);
            imgSpeech.setOnClickListener(this);
            imgSave = itemView.findViewById(R.id.imgSave_Word);
            imgSave.setOnClickListener(this);
            layout_toeicIelts = itemView.findViewById(R.id.layout_IeltsToeic);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onClick(v);
        }
    }

    public interface MyDelegationLevel {
        public void saveItem(Word word);

        public void speechItem(Word word);
    }
}
