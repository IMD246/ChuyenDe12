package com.example.EnglishBeginner.fragment.LearnWord.saveWord;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.example.EnglishBeginner.fragment.LearnWord.saveWord.source.SaveSqliteHelper;
import com.example.EnglishBeginner.fragment.LearnWord.word.source.MySingleton;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SaveAdapter extends RecyclerView.Adapter<SaveAdapter.ViewHolder> {

    Context context;
    ArrayList<Word> listSave ;
    MediaPlayer player;
    SaveSqliteHelper sqlLiteHelper;
    TextToSpeech textToSpeech;
    public SaveAdapter(Context context, ArrayList<Word> listSave, SaveSqliteHelper sqlLiteHelper,TextToSpeech textToSpeech) {
        this.context = context;
        this.listSave = listSave;
        this.sqlLiteHelper = sqlLiteHelper;
        this.textToSpeech=textToSpeech;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View ref = LayoutInflater.from(context).inflate(R.layout.saveword_item,parent,false);

        return new ViewHolder(ref);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txt_saveWord.setText(listSave.get(position).getWord());
//        translateText(listSave.get(position).getWord().toString(),holder.txt_saveMeaning);

        holder.txt_saveMeaning.setText(listSave.get(position).getMeaning());
        holder.speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                texttoSpeak(listSave.get(holder.getAdapterPosition()).getWord().toString().trim());
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlLiteHelper.removeSaveWord(listSave.get(holder.getAdapterPosition()));
                listSave.remove(listSave.get(holder.getAdapterPosition()));
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listSave.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_saveWord,txt_saveMeaning;
        ImageView remove,speech;
        public ViewHolder(View itemView) {
            super(itemView);
            this.txt_saveWord = itemView.findViewById(R.id.txt_saveWord);
            this.txt_saveMeaning = itemView.findViewById(R.id.txt_saveWordMeaning);
            this.remove = itemView.findViewById(R.id.saveWord_img_remove);
            this.speech = itemView.findViewById(R.id.img_save_speach);
        }
    }

    private void texttoSpeak(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
        else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
}
