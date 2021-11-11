package com.example.EnglishBeginner.fragment.LearnWord.practice.source;

import android.util.Log;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;


public class TranslateText {
    public void HintTextTranslate(String word, TextView txtview) {

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\"fromLang\": \"auto-detect\",\r\"text\": \"" + word + "\",\r\"to\": \"vi\"\r }");
        Request request = new Request.Builder()
                .url("https://cheap-translate.p.rapidapi.com/translate")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("x-rapidapi-host", "cheap-translate.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "7ab6c14598mshff589a0869fafeep1510ddjsne9435a44be7a")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                txtview.setText(word);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    String temp = response.body().string();
                    String[] arr = temp.split(",");
                    String[] arr2 = arr[0].split(":");
                    String temp2 = arr2[1].substring(0);
                    String lastResult = removeFirstandLast(temp2);
                    txtview.setText(lastResult);
                    Log.e("aaaa", "run: " + lastResult);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

//            }
//        });


        //    public String TranslateText(String word) {
//        String temp="";
//        OkHttpClient client = new OkHttpClient();
//
//        MediaType mediaType = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(mediaType, "{\r\"fromLang\": \"auto-detect\",\r\"text\": \""+word+"\",\r\"to\": \"vi\"\r }");
//        Request request = new Request.Builder()
//                .url("https://cheap-translate.p.rapidapi.com/translate")
//                .post(body)
//                .addHeader("content-type", "application/json")
//                .addHeader("x-rapidapi-host", "cheap-translate.p.rapidapi.com")
//                .addHeader("x-rapidapi-key", "7ab6c14598mshff589a0869fafeep1510ddjsne9435a44be7a")
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
////                new Handler(Looper.getMainLooper()).post(new Runnable() {
////                    @Override
////                    public void run() {
//                try {
//                    String temp = response.body().string();
//                    String[]arr = temp.split(",");
//                    String []arr2=arr[0].split(":");
//                    String temp2=arr2[1].substring(0);
//                    String lastResult = removeFirstandLast(temp2);
//                    temp=lastResult;
//                    Log.e("aaaa", "run: "+lastResult );
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
////            }
////        });
//
//        return temp;
//    }
//
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
