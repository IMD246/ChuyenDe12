package com.example.myapplication.User.LearnWord.practice.source;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TranslateText {
    public void makeNetworkRequest(String word, TextView txtview) {

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\"fromLang\": \"auto-detect\",\r\"text\": \""+word+"\",\r\"to\": \"vi\"\r }");
        Request request = new Request.Builder()
                .url("https://cheap-translate.p.rapidapi.com/translate")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("x-rapidapi-host", "cheap-translate.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "7ab6c14598mshff589a0869fafeep1510ddjsne9435a44be7a")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                txtview.setText(word);

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String temp = response.body().string();
                            String[]arr = temp.split(",");
                            String []arr2=arr[0].split(":");
                            String temp2=arr2[1].substring(0);
                            String lastResult = removeFirstandLast(temp2);
                            txtview.setText(lastResult);
                            Log.e("aaaa", "run: "+lastResult );
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });


    }

    public static String removeFirstandLast(String str)
    {

        // Creating a StringBuffer object
        StringBuffer sb = new StringBuffer(str);

        // Removing the last character
        // of a string
        sb.delete(str.length() - 1, str.length());

        // Removing the first character
        // of a string
        sb.delete(0, 1);

        // Converting StringBuffer into
        // string & return modified string
        return sb.toString();
    }
}
