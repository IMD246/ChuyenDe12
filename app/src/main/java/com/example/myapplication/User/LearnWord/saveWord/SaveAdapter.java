package com.example.myapplication.User.LearnWord.saveWord;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
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
import com.example.myapplication.R;
import com.example.myapplication.User.LearnWord.saveWord.source.SaveSqliteHelper;
import com.example.myapplication.User.LearnWord.word.source.MySingleton;
import com.example.myapplication.User.DTO.Word;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

//import org.chromium.base.Callback;
//import org.chromium.base.Promise;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SaveAdapter extends RecyclerView.Adapter<SaveAdapter.ViewHolder> {

    Context context;
    ArrayList<Word> listSave ;
    MediaPlayer player;
    SaveSqliteHelper sqlLiteHelper;

    public SaveAdapter(Context context, ArrayList<Word> listSave, SaveSqliteHelper sqlLiteHelper) {
        this.context = context;
        this.listSave = listSave;
        this.sqlLiteHelper = sqlLiteHelper;
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
        holder.txt_saveMeaning.setText(listSave.get(position).getWord());
        holder.speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAudioLink(listSave.get(holder.getAdapterPosition()).getWord().toString().trim());
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
                }
            });
        } catch (Exception exception) {
            Log.d(exception.toString(), "PlaySong: ");
        }

    }
    private ArrayList<String> translateText(String word,TextView textView) {
        ArrayList<String> listPossibleResult = new ArrayList<>();
        try {

            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                    .setTargetLanguage(FirebaseTranslateLanguage.VI)
                    .build();
            FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);

            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();

            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {


                        translator.translate(word.trim().toString().toLowerCase()).addOnSuccessListener(new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(String s) {

                            textView.setText(s);


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                    }


            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listPossibleResult;
    }

}
