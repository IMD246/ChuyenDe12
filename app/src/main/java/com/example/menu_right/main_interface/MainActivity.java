package com.example.menu_right.main_interface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.menu_right.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //khai báo giá trị cho screen
    public static final int FRAGMENT_LEARN = 1;
    public static final int FRAGMENT_ALARM = 2;
    public static final int FRAGMENT_VOCABULARY = 3;
    public static final int FRAGMENT_PROFILE = 4;
    public static final int FRAGMENT_SETTING = 5;

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
    }

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

        //xử lí bottom Navigation
        processBottomNavigation();

        //xử lí Viewpager2
        processViewPager2();

    }

    //xử lí Viewpager2
    private void processViewPager2() {
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        myCurrentViewpager2 = FRAGMENT_LEARN;
                        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_learn).setChecked(true);
                        navigationView.getMenu().findItem(R.id.nav_profile).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_setting).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_help).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_logout).setChecked(false);
                        break;
                    case 1:
                        myCurrentViewpager2 = FRAGMENT_ALARM;
                        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_alarm).setChecked(true);
                        navigationView.getMenu().findItem(R.id.nav_profile).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_setting).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_help).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_logout).setChecked(false);
                        break;
                    case 2:
                        myCurrentViewpager2 = FRAGMENT_VOCABULARY;
                        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_vocabulary).setChecked(true);
                        navigationView.getMenu().findItem(R.id.nav_profile).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_setting).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_help).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_logout).setChecked(false);
                        break;
                    case 3:
                        myCurrentViewpager2 = FRAGMENT_PROFILE;
                        Log.d("3", "3");
                        navigationView.getMenu().findItem(R.id.nav_profile).setChecked(true);
                        navigationView.getMenu().findItem(R.id.nav_setting).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_help).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_logout).setChecked(false);
                        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_learn).setChecked(false);
                        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_alarm).setChecked(false);
                        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_vocabulary).setChecked(false);
                        break;
                    case 4:
                        myCurrentViewpager2 = FRAGMENT_SETTING;
                        navigationView.getMenu().findItem(R.id.nav_profile).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_setting).setChecked(true);
                        navigationView.getMenu().findItem(R.id.nav_help).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_logout).setChecked(false);
                        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_learn).setChecked(false);
                        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_alarm).setChecked(false);
                        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_vocabulary).setChecked(false);
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
                Toast.makeText(MainActivity.this, "click help", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                navigationView.getMenu().findItem(R.id.nav_profile).setChecked(false);
                navigationView.getMenu().findItem(R.id.nav_setting).setChecked(false);
                navigationView.getMenu().findItem(R.id.nav_help).setChecked(false);
                navigationView.getMenu().findItem(R.id.nav_logout).setChecked(true);
                bottomNavigationView.getMenu().findItem(R.id.bottom_nav_learn).setChecked(false);
                bottomNavigationView.getMenu().findItem(R.id.bottom_nav_alarm).setChecked(false);
                bottomNavigationView.getMenu().findItem(R.id.bottom_nav_vocabulary).setChecked(false);
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
//                        FirebaseAuth.getInstance().signOut();
//                        startActivity(new Intent(UserInterface.this, Login.class));
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