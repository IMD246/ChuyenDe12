package com.example.myapplication.Admin.WordManagement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myapplication.Admin.Adapter.LevelSpinnerAdapter;
import com.example.myapplication.Admin.Adapter.TopicAdapter;
import com.example.myapplication.Admin.Adapter.WordToeicIetlsAdapter;
import com.example.myapplication.Admin.DAO.DAOIetls;
import com.example.myapplication.Admin.DAO.DAOImageStorage;
import com.example.myapplication.Admin.DAO.DAOLevel;
import com.example.myapplication.Admin.DAO.DAOTopic;
import com.example.myapplication.Admin.DTO.Level;
import com.example.myapplication.Admin.DTO.Topic;
import com.example.myapplication.Admin.DTO.Word;
import com.example.myapplication.R;
import com.google.rpc.context.AttributeContext;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class IetlsManagement extends AppCompatActivity {

    private RecyclerView rcvIetls;
    private WordToeicIetlsAdapter wordToeicIetlsAdapter;
    private AutoCompleteTextView atcIetls;
    private SearchView svIetls;
    private DAOIetls daoIetls;
    private ImageView imgAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ietls_management);
        initUI();
        getDataFirebase();
    }
    private void initUI() {
        daoIetls = new DAOIetls(this);
        rcvIetls = findViewById(R.id.rcvIetls);
        wordToeicIetlsAdapter = new WordToeicIetlsAdapter(this);
        imgAdd = findViewById(R.id.imgAdd);
        svIetls = findViewById(R.id.svIetls);
        atcIetls = findViewById(R.id.atcTypeWord);
        atcIetls.setAdapter(new ArrayAdapter<String>(this,R.layout.listoptionitem,R.id.tvOptionItem,getResources().getStringArray(R.array.typeWord)));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvIetls.setLayoutManager(linearLayoutManager);
        wordToeicIetlsAdapter.setWordList(daoIetls.getWordList());
        rcvIetls.setAdapter(wordToeicIetlsAdapter);
        wordToeicIetlsAdapter.setMyDelegationLevel(new WordToeicIetlsAdapter.MyDelegationLevel() {
            @Override
            public void editItem(Word word) {
                openDialog(Gravity.CENTER,2, word);
            }

            @Override
            public void deleteItem(Word word) {
                alertDialog(word);
            }
        });
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(Gravity.CENTER,1,null);
            }
        });
        svIetls.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                wordToeicIetlsAdapter.getFilter().filter(newText);
                return false;
            }
        });
        atcIetls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                wordToeicIetlsAdapter.setListDependOnTypeWord(atcIetls.getText().toString());
            }
        });
    }
    private void getDataFirebase() {
        daoIetls.getDataFromRealTimeToList(wordToeicIetlsAdapter);
    }
    public void openDialog(int center, int choice, Word word) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.addeditword);

        Window window = dialog.getWindow();
        if (window==null)
        {return;}
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = center;
        window.setAttributes(windowAttributes);

        if (Gravity.CENTER == center)
        {
            dialog.setCancelable(true);
        }
        else
        {
            dialog.setCancelable(false);
        }
        EditText edtWord = dialog.findViewById(R.id.edtWordItem);
        EditText edtMeaning = dialog.findViewById(R.id.edtMeaningItem);
        Spinner spnTypeWord = dialog.findViewById(R.id.spnTypeWordItem);
        TextView tvThemSua = dialog.findViewById(R.id.tvThemSua);
        if (word!=null)
        {
            spnTypeWord.setSelection(getSelectedSpinner(spnTypeWord,String.valueOf(word.getTypeWord())));
            edtMeaning.setText(word.getMeaning());
            edtWord.setText(word.getWord());
        }
        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if (choice == 2)
        {
            btnYes.setText("Sửa");
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Word word1 = new Word();
                    word1.setId(word.getId());
                    word1.setWord(edtWord.getText().toString());
                    word1.setTypeWord(spnTypeWord.getSelectedItem().toString());
                    word1.setMeaning(edtMeaning.getText().toString());
                    if (word.getWord().equalsIgnoreCase(word1.getWord())&&word.getTypeWord().equalsIgnoreCase(word1.getTypeWord())
                    &&word.getMeaning().equalsIgnoreCase(word1.getMeaning()))
                    { }
                    else {
                        daoIetls.editDataToFireBase(word1,edtWord);
                    }
                }
            });
        }
        else if (choice == 1)
        {
            btnYes.setText("Thêm");
            tvThemSua.setText("Thêm dữ liệu");
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Word word1 = new Word();
                    int i=1;
                    if (daoIetls.getWordList().size()>0)
                    {
                        i = daoIetls.getWordList().get(daoIetls.getWordList().size()-1).getId()+1;
                    }
                    word1.setId(i);
                    word1.setWord(edtWord.getText().toString());
                    word1.setTypeWord(spnTypeWord.getSelectedItem().toString());
                    word1.setMeaning(edtMeaning.getText().toString());
                    daoIetls.addDataToFireBase(word1,edtWord,edtMeaning);
                }
            });
        }
        dialog.show();
    }

    private int getSelectedSpinner(Spinner spnTypeWord, String valueOf) {
        for (int i=0; i< spnTypeWord.getCount();i++)
        {
            if (spnTypeWord.getItemAtPosition(i).toString().equalsIgnoreCase(valueOf))
            {
                return i;
            }
        }
        return 0;
    }

    // Xây dựng một Hộp thoại thông báo
    public void alertDialog(Word word) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Bạn có muốn xóa không?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Có",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        daoIetls.deleteDataToFire(word);
                    }
                });

        builder1.setNegativeButton(
                "Không",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}