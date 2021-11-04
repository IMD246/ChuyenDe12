package com.example.EnglishBeginner.Admin;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.EnglishBeginner.Admin.Adapter.ViewPageAdapter;
import com.example.EnglishBeginner.Admin.DTO.LearnItem;
import com.example.EnglishBeginner.Admin.LearnManagement.LearnQuestion;
import com.example.EnglishBeginner.Admin.LearnManagement.LevelManagement;
import com.example.EnglishBeginner.Admin.LearnManagement.QuestionInterface;
import com.example.EnglishBeginner.Admin.LearnManagement.TopicManagement;
import com.example.EnglishBeginner.Admin.LearnManagement.TypeQuestionManagement;
import com.example.EnglishBeginner.Admin.WordManagement.IetlsManagement;
import com.example.EnglishBeginner.Admin.WordManagement.ToeicManagement;
import com.example.EnglishBeginner.Admin.DTO.DEFAULTVALUE;
import com.example.EnglishBeginner.Login.Login;
import com.example.EnglishBeginner.R;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminInterface extends AppCompatActivity {

    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;

    //Khai báo drawerLayout
    private DrawerLayout drawerLayout;

    NavigationView navigationView;
    TextView tvUserName;
    FirebaseUser user;

    //khai báo biến lưu index màn hình hiện tại
    private int myCurrentScreen = 0;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_interface);
        user = FirebaseAuth.getInstance().getCurrentUser();
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        tvUserName = header.findViewById(R.id.tvUserNameAdmin);
        if (user!=null)
        {
            tvUserName.setText(user.getEmail());
        }
        //kiểm tra logic màn hình khi đóng drawer
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                switch (myCurrentScreen){
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
            public void onDrawerStateChanged(int newState) {

            }
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.item_menu_drawer_change_pass:
                    startActivity(new Intent(AdminInterface.this, ChangePasswordActivity_Admin.class));
                    break;
                case R.id.item_menu_drawer_logout:
                    alertDialog();
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.END);
            return true;
        });

        bottomNavigationView = findViewById(R.id.botnav);
        int[] colors = new int[] {
                Color.GRAY,
                Color.BLUE,
        };

        int [][] states = new int [][]{
                new int[] { android.R.attr.state_enabled, -android.R.attr.state_checked},
                new int[] {android.R.attr.state_enabled, android.R.attr.state_checked}
        };
        bottomNavigationView.setItemTextColor(new ColorStateList(states, colors));
        bottomNavigationView.setItemIconTintList(new ColorStateList(states, colors));

        viewPager = findViewById(R.id.viewPage);
        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(this);
        viewPager.setAdapter(viewPageAdapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.learn).setChecked(true);
                        myCurrentScreen = 0;
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.word).setChecked(true);
                        myCurrentScreen = 1;
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.analysic).setChecked(true);
                        myCurrentScreen = 2;
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.account).setChecked(true);
                        myCurrentScreen = 3;
                        break;
                    case 4:
                        bottomNavigationView.getMenu().findItem(R.id.profile).setChecked(true);
                        myCurrentScreen = 4;
                        break;
                }
            }
        });
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.learn:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.word:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.analysic:
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.account:
                    viewPager.setCurrentItem(3);
                    break;
                case R.id.profile:
                    drawerLayout.openDrawer(GravityCompat.END);
                    break;
            }
            return true;
        });
    }

    // Dùng hàm xử lý nút quay lại của thiết bị
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    public void alertDialog() {
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
    // hàm chuyển màn hình dựa theo tên
    public void Transaction(LearnItem learnItem) {
        if (learnItem.getName().equals(DEFAULTVALUE.LEVEL)) {
            startActivity(new Intent(AdminInterface.this, LevelManagement.class));
        } else if (learnItem.getName().equals(DEFAULTVALUE.TYPEQUESTION)) {
            startActivity(new Intent(AdminInterface.this, TypeQuestionManagement.class));
        } else if (learnItem.getName().equals(DEFAULTVALUE.TOPIC)) {
            startActivity(new Intent(AdminInterface.this, TopicManagement.class));
        } else if (learnItem.getName().equals(DEFAULTVALUE.QUESTION)) {
            startActivity(new Intent(AdminInterface.this, QuestionInterface.class));
        }
        else if (learnItem.getName().equals(DEFAULTVALUE.LEARNTOPIC))
        {
            startActivity(new Intent(AdminInterface.this, LearnQuestion.class));
        }
        else if (learnItem.getName().equals(DEFAULTVALUE.TOEIC))
        {
            startActivity(new Intent(AdminInterface.this, ToeicManagement.class));
        }
        else if (learnItem.getName().equals(DEFAULTVALUE.IETLS))
        {
            startActivity(new Intent(AdminInterface.this, IetlsManagement.class));
        }
    }
}