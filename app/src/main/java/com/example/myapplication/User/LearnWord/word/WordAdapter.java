package com.example.myapplication.User.LearnWord.word;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.myapplication.R;
import com.example.myapplication.User.LearnWord.history.HistoryItem;
import com.example.myapplication.User.LearnWord.history.source.HistorySqliteDataHelper;
import com.example.myapplication.User.LearnWord.saveWord.source.SaveSqliteHelper;
import com.example.myapplication.User.LearnWord.word.source.MySingleton;
import com.example.myapplication.User.LearnWord.word.source.SqlLiteHelper;
import com.example.myapplication.User.DTO.Word;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder> {

    Context context;
    ArrayList<Word> listWord;
    SqlLiteHelper databaseHelper;
    MediaPlayer player;

    public WordAdapter(Context context, ArrayList<Word> listWord, SqlLiteHelper databaseHelper) {
        this.context = context;
        this.listWord = listWord;
        this.databaseHelper = databaseHelper;

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
                getAudioLink(listWord.get(holder.getAdapterPosition()).getWord().toString().trim());
            }
        });
        //save tu
        holder.img_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveSqliteHelper sqliteHelper = new SaveSqliteHelper(v.getContext());
                sqliteHelper.addSaveWord(temp);

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

    //lay ra meaning cua tu` tren api
    private void getAudioLink(String word) {
        //lay ra chuoi trong search view
        String url = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            JSONObject jsonObject = response.getJSONObject(0);


                            JSONArray jsonArray = jsonObject.getJSONArray("phonetics");
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            String audio = jsonObject1.getString("audio");

                            PlaySong(audio);

                        } catch (Exception exception) {
                            Toast.makeText(context, "bug found.", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "am thanh khong co san....", Toast.LENGTH_SHORT).show();

            }
        });
        MySingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);

        //goi len api

        //yeu cau api gui ve file json


    }

    private void PlaySong(String url) {
        try {
            Toast.makeText(context, url, Toast.LENGTH_SHORT).show();
            Uri uri = Uri.parse("https:" + url);
            player = new MediaPlayer();
            player.setDataSource(context, uri);
            player.prepare();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    mp.start();
                    Toast.makeText(context, "playing", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception exception) {
            Log.d(exception.toString(), "PlaySong: ");
        }

    }

}
