package com.example.LearnEnglish.main_interface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.LearnEnglish.DAO.DAOUserProfile;
import com.example.LearnEnglish.DTO.DEFAULTVALUE;
import com.example.LearnEnglish.Login.Login;
import com.example.LearnEnglish.R;
import com.example.LearnEnglish.learn.learning.LearningEnglishFragment;
import com.example.LearnEnglish.learn.testing.TestSelectionEnglishFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserInterfaceActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Khai báo các trường dữ liệu để lấy data trên firebase
    private DAOUserProfile daoUserProfile;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    //khai báo giá trị cho screen
    public static final int FRAGMENT_LEARN = 0;
    public static final int FRAGMENT_ALARM = 1;
    public static final int FRAGMENT_VOCABULARY = 2;
    public static final int FRAGMENT_PROFILE = 3;
    public static final int FRAGMENT_SETTING = 4;

    //khai báo giá trị màn hình hiện tại, mặc định là home
    private int myCurrentViewpager2 = FRAGMENT_LEARN;

    //khai báo drawerlayout
    private DrawerLayout drawerLayout;

    //Khai báo navigationView
    public NavigationView navigationView;

    //khai báo bottom navigation
    private BottomNavigationView bottomNavigationView;

    //khai báo Viewpager2 và adapter của nó
    private ViewPager2 viewPager2;
    public ViewPager2_Adapter viewPager2_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setControl();
        checkLogicDrawerLayout();
        processBottomNavigation();
        processViewPager2();
    }

    //kiểm tra logic khi drawer layout đóng

    private void checkLogicDrawerLayout() {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                switch (myCurrentViewpager2) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_learn).setChecked(true);
                        navigationView.getMenu().findItem(R.id.nav_profile).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_setting).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_help).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_logout).setChecked(false);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_alarm).setChecked(true);
                        navigationView.getMenu().findItem(R.id.nav_profile).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_setting).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_help).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_logout).setChecked(false);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_vocabulary).setChecked(true);
                        navigationView.getMenu().findItem(R.id.nav_profile).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_setting).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_help).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_logout).setChecked(false);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_profile).setChecked(true);
                        break;
                    case 4:
                        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_profile).setChecked(true);
                        break;
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }
    //lấy dữ liệu user

//    private void getDataUser() {
//        user = FirebaseAuth.getInstance().getCurrentUser();
//        databaseReference = FirebaseDatabase.getInstance().getReference("users");
//        databaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                User us = snapshot.getValue(User.class);
//                if (us != null) {
//                    Toast.makeText(UserInterfaceActivity.this, "" + us.getEmail().toString(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
    //Ánh xạ, khởi gán giá trị,...

    private void setControl() {
        //drawe layout
        drawerLayout = findViewById(R.id.drawer_layout);

        //navigationview
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //bottom navigation
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        //start app
        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_learn).setChecked(true);

        //Viewpager2
        viewPager2 = findViewById(R.id.view_pager2);
        viewPager2_adapter = new ViewPager2_Adapter(this);
        viewPager2.setAdapter(viewPager2_adapter);
    }

    //xử lí Viewpager2
    private void processViewPager2() {
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        Log.d("bạn đang ở màn hình thứ", "0");
                        myCurrentViewpager2 = FRAGMENT_LEARN;
                        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_learn).setChecked(true);
                        break;
                    case 1:
                        Log.d("bạn đang ở màn hình thứ", "1");
                        myCurrentViewpager2 = FRAGMENT_ALARM;
                        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_alarm).setChecked(true);
                        break;
                    case 2:
                        Log.d("bạn đang ở màn hình thứ", "2");
                        myCurrentViewpager2 = FRAGMENT_VOCABULARY;
                        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_vocabulary).setChecked(true);
                        break;
                    case 3:
                        Log.d("bạn đang ở màn hình thứ", "3");
                        myCurrentViewpager2 = FRAGMENT_PROFILE;
                        navigationView.getMenu().findItem(R.id.nav_profile).setChecked(true);
                        navigationView.getMenu().findItem(R.id.nav_setting).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_help).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_logout).setChecked(false);
                        break;
                    case 4:
                        Log.d("bạn đang ở màn hình thứ", "4");
                        myCurrentViewpager2 = FRAGMENT_SETTING;
                        navigationView.getMenu().findItem(R.id.nav_setting).setChecked(true);
                        navigationView.getMenu().findItem(R.id.nav_profile).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_help).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_logout).setChecked(false);
                        break;
                }
            }
        });
    }

    //xử lí bottom Navigation
    private void processBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            switch (id) {
                case R.id.bottom_nav_learn:
                    checkLogicScreen(FRAGMENT_LEARN, 0);
                    break;
                case R.id.bottom_nav_alarm:
                    checkLogicScreen(FRAGMENT_ALARM, 1);
                    break;
                case R.id.bottom_nav_vocabulary:
                    checkLogicScreen(FRAGMENT_VOCABULARY, 2);
                    break;
                case R.id.bottom_nav_profile:
                    drawerLayout.openDrawer(GravityCompat.END);
                    break;
            }
            return true;
        });
    }

    //xử lí navigation drawer
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_profile:
                checkLogicScreen(FRAGMENT_PROFILE, 3);
                break;
            case R.id.nav_setting:
                checkLogicScreen(FRAGMENT_SETTING, 4);
                break;
            case R.id.nav_help://ấn vào help sẽ chuyển acti
                Toast.makeText(UserInterfaceActivity.this, "click help", Toast.LENGTH_SHORT).show();
                navigationView.getMenu().findItem(R.id.nav_profile).setChecked(false);
                navigationView.getMenu().findItem(R.id.nav_setting).setChecked(false);
                navigationView.getMenu().findItem(R.id.nav_help).setChecked(true);
                navigationView.getMenu().findItem(R.id.nav_logout).setChecked(false);
                break;
            case R.id.nav_logout:
                navigationView.getMenu().findItem(R.id.nav_profile).setChecked(false);
                navigationView.getMenu().findItem(R.id.nav_setting).setChecked(false);
                navigationView.getMenu().findItem(R.id.nav_help).setChecked(false);
                navigationView.getMenu().findItem(R.id.nav_logout).setChecked(true);
                alertDialog();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.END);
        return true;
    }

    //hàm xử lí kiểm tra logic màn hình
    private void checkLogicScreen(int screen, int currentItemScreen) {
        if (myCurrentViewpager2 != screen) {
            viewPager2.setCurrentItem(currentItemScreen);
            myCurrentViewpager2 = screen;
        }
    }

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
                        startActivity(new Intent(UserInterfaceActivity.this, Login.class));
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

    //hàm chuyển màn hình
    public void navigationScreen(String string) {
        if (DEFAULTVALUE.LEARNING_SCREEN.equalsIgnoreCase(string)) {
            startActivity(new Intent(this, LearningEnglishFragment.class));
        } else if (DEFAULTVALUE.TEST_SCREEN.equalsIgnoreCase(string)) {
            startActivity(new Intent(this, TestSelectionEnglishFragment.class));
        }
    }
}