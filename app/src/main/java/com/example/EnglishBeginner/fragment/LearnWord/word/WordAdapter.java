package com.example.EnglishBeginner.fragment.LearnWord.word;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.EnglishBeginner.DTO.Word;
import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.fragment.LearnWord.history.HistoryItem;
import com.example.EnglishBeginner.fragment.LearnWord.history.source.HistorySqliteDataHelper;
import com.example.EnglishBeginner.fragment.LearnWord.saveWord.source.SaveSqliteHelper;
import com.example.EnglishBeginner.fragment.LearnWord.word.source.MySingleton;
import com.example.EnglishBeginner.fragment.LearnWord.word.source.SqlLiteHelper;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder> {

    Context context;
    ArrayList<Word> listWord;
    SqlLiteHelper databaseHelper;
    MediaPlayer player;
    TextToSpeech textToSpeech;
    public WordAdapter(Context context, ArrayList<Word> listWord, SqlLiteHelper databaseHelper,TextToSpeech textToSpeech) {
        this.context = context;
        this.listWord = listWord;
        this.databaseHelper = databaseHelper;
        this.textToSpeech = textToSpeech;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View ref = LayoutInflater.from(context).inflate(R.layout.word_item, parent, false);

        return new ViewHolder(ref);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Word temp = listWord.get(position);
        holder.txt_word.setText(temp.getWord());
        holder.txt_pronounce.setText(temp.getPronounce());
//

        //set event
        // them su kien phat am cho tu
        holder.img_speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeak(listWord.get(holder.getAdapterPosition()).getWord().toString().trim());
            }
        });
        //save tu
        holder.img_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveSqliteHelper sqliteHelper = new SaveSqliteHelper(v.getContext());
                sqliteHelper.addSaveWordByButton(temp);

            }
        });

        //tao su kien chuyen trang cho tu
        holder.lvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add to history
                HistorySqliteDataHelper historySqliteDataHelper = new HistorySqliteDataHelper(context);
                 HistoryItem is=
                         new HistoryItem(listWord.get(holder.getAdapterPosition()).getId()+"",
                                 listWord.get(holder.getAdapterPosition()).getWord(),
                                 HistoryItem.getDateTimeNow());
                historySqliteDataHelper.addHistory(is);

                Bundle bundle = new Bundle();
                bundle.putString("htmlText", listWord.get(holder.getAdapterPosition()).getHtmlText().toString());

                Intent it = new Intent(context, WordItemDetail.class);
                it.putExtras(bundle);
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(it);

            }
        });

    }


    @Override
    public int getItemCount() {
        return listWord.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_word, txt_pronounce;
        ImageView img_speech;
        ImageView img_save;
        LinearLayout lvDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txt_pronounce = itemView.findViewById(R.id.txt_pronounce);
            this.lvDetail = itemView.findViewById(R.id.word_item_lnDetail);
            this.txt_word = itemView.findViewById(R.id.txt_word);
            this.img_speech = itemView.findViewById(R.id.img_speech);
            this.img_save = itemView.findViewById(R.id.img_bookMark);
        }
    }


    public void textToSpeak(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
        else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
}
