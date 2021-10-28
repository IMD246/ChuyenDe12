package com.example.myapplication.User.LearnWord.word;

import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import org.sufficientlysecure.htmltextview.HtmlTextView;

public class WordItemDetail extends AppCompatActivity {
    String htmlCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_item_detail);
        //hide appbar



        Bundle bundle = getIntent().getExtras();
        htmlCode = bundle.getString("htmlText");
        //Toast.makeText(getBaseContext(), htmlCode, Toast.LENGTH_SHORT).show();
        HtmlTextView htmlTextView = (HtmlTextView) findViewById(R.id.wordItemDetail_html_text);
        htmlTextView.setHtml(htmlCode);

    }
}
