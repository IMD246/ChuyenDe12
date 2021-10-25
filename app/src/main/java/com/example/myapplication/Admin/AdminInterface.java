package com.example.myapplication.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.myapplication.Admin.LearnManagement.DTO.LearnItem;
import com.example.myapplication.Admin.LearnManagement.LevelManagement;
import com.example.myapplication.Admin.LearnManagement.QuestionInterface;
import com.example.myapplication.Admin.LearnManagement.TopicManagement;
import com.example.myapplication.Admin.LearnManagement.TypeQuestionManagement;
import com.example.myapplication.DEFAULTVALUE;
import com.example.myapplication.Login.Login;
import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AdminInterface extends AppCompatActivity {

    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_interface);
        bottomNavigationView = findViewById(R.id.botnav);
        viewPager = findViewById(R.id.viewPage);
        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager(),FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPageAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position)
                {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.learn).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.word).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.analysic).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.account).setChecked(true);
                        break;
                    case 4:
                        bottomNavigationView.getMenu().findItem(R.id.profile).setChecked(true);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.learn: viewPager.setCurrentItem(0);
                    break;
                    case R.id.word: viewPager.setCurrentItem(1);
                        break;
                    case R.id.analysic: viewPager.setCurrentItem(2);
                        break;
                    case R.id.account: viewPager.setCurrentItem(3);
                        break;
                    case R.id.profile: viewPager.setCurrentItem(4);
                        break;
                }
                return true;
            }
        });
    }

    // Dùng hàm xử lý nút quay lại của thiết bị
    @Override
    public void onBackPressed() {
        alertDialog();
    }
    public void alertDialog()
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Bạn có muốn đăng xuất?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Có",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(AdminInterface.this, Login.class));
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
    public void Transaction(LearnItem learnItem)
    {
        if (learnItem.getName().equals(DEFAULTVALUE.LEVEL))
        {
            startActivity(new Intent(AdminInterface.this, LevelManagement.class));
        }
        else if (learnItem.getName().equals(DEFAULTVALUE.TYPEQUESTION))
        {
            startActivity(new Intent(AdminInterface.this, TypeQuestionManagement.class));
        }
        else if (learnItem.getName().equals(DEFAULTVALUE.TOPIC))
        {
            startActivity(new Intent(AdminInterface.this, TopicManagement.class));
        }
        else if (learnItem.getName().equals(DEFAULTVALUE.QUESTION))
        {
            startActivity(new Intent(AdminInterface.this, QuestionInterface.class));
        }
    }
}