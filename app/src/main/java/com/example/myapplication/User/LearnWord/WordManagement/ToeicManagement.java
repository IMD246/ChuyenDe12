package com.example.myapplication.User.LearnWord.WordManagement;

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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.User.Adapter.WordToeicIetlsAdapter;
import com.example.myapplication.User.DAO.DAOToeic;

public class ToeicManagement extends AppCompatActivity {

    private RecyclerView rcvToeic;
    private WordToeicIetlsAdapter wordToeicIetlsAdapter;
    private AutoCompleteTextView atcToeic;
    private SearchView svToeic;
    private DAOToeic daoToeic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toeic_management);
        initUI();
        getDataFirebase();
    }
    private void initUI() {
        daoToeic = new DAOToeic(this);
        rcvToeic = findViewById(R.id.rcvToeic);
        wordToeicIetlsAdapter = new WordToeicIetlsAdapter(this);
        svToeic = findViewById(R.id.svToeic);
        atcToeic = findViewById(R.id.atcTypeWord);
        atcToeic.setAdapter(new ArrayAdapter<String>(this, R.layout.listoptionitem, R.id.tvOptionItem,getResources().getStringArray(R.array.typeWord)));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvToeic.setLayoutManager(linearLayoutManager);
        wordToeicIetlsAdapter.setWordList(daoToeic.getWordList());
        rcvToeic.setAdapter(wordToeicIetlsAdapter);
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
        svToeic.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        atcToeic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                wordToeicIetlsAdapter.setListDependOnTypeWord(atcToeic.getText().toString());
            }
        });
    }
    private void getDataFirebase() {
        daoToeic.getDataFromRealTimeToList(wordToeicIetlsAdapter);
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
                        daoToeic.editDataToFireBase(word1,edtWord);
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
                    if (daoToeic.getWordList().size()>0)
                    {
                        i = daoToeic.getWordList().get(daoToeic.getWordList().size()-1).getId()+1;
                    }
                    word1.setId(i);
                    word1.setWord(edtWord.getText().toString());
                    word1.setTypeWord(spnTypeWord.getSelectedItem().toString());
                    word1.setMeaning(edtMeaning.getText().toString());
                    daoToeic.addDataToFireBase(word1,edtWord);
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
                        daoToeic.deleteDataToFire(word);
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