package com.example.EnglishBeginner.main_interface;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.EnglishBeginner.Adapter.ProcessTopic_Adapter;
import com.example.EnglishBeginner.DAO.DAOProcessUser;
import com.example.EnglishBeginner.DAO.DAOQuestion;
import com.example.EnglishBeginner.DTO.DEFAULTVALUE;
import com.example.EnglishBeginner.DTO.ProcessTopicItem;
import com.example.EnglishBeginner.DTO.Topic;
import com.example.EnglishBeginner.DTO.User;
import com.example.EnglishBeginner.Login.Login;
import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.learn.testing.TestEnglishActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserInterfaceActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Khai báo các trường dữ liệu để lấy data trên firebase
    public FirebaseUser firebaseUser;
    public DatabaseReference databaseReference;
    private TextView tvUserName, tvUserEmail;
    private ImageView imgUserName;
    private DAOQuestion daoQuestion;
    private DAOProcessUser daoProcessUser;

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
        getProFileFromRealTime();
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

    private void getProFileFromRealTime() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        String userId = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                getDataUserProfileToControl(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // khởi gán giá trị tới các view
    private void getDataUserProfileToControl(User user) {
        if (user != null) {
            tvUserName.setText(user.getFullname());
            tvUserEmail.setText(user.getEmail());
            if (!(user.getImageUser().trim().length() == 0 && user.getImageUser().isEmpty())) {
                Glide.with(UserInterfaceActivity.this).load(user.getImageUser()).into(imgUserName);
            }
        }
    }

    //Ánh xạ, khởi gán giá trị,...
    private void setControl() {
        //drawe layout
        drawerLayout = findViewById(R.id.drawer_layout);
        daoProcessUser = new DAOProcessUser(this);
        navigationView = findViewById(R.id.nav_view);
        // get view control trong navigationview có chứa drawable
        View header = navigationView.getHeaderView(0);
        tvUserEmail = header.findViewById(R.id.tv_username);
        tvUserName = header.findViewById(R.id.tv_name_user);
        imgUserName = header.findViewById(R.id.profile_image);
        //navigationview
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
                        myCurrentViewpager2 = FRAGMENT_LEARN;
                        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_learn).setChecked(true);
                        break;
                    case 1:
                        myCurrentViewpager2 = FRAGMENT_ALARM;
                        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_alarm).setChecked(true);
                        break;
                    case 2:
                        myCurrentViewpager2 = FRAGMENT_VOCABULARY;
                        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_vocabulary).setChecked(true);
                        break;
                    case 3:
                        myCurrentViewpager2 = FRAGMENT_PROFILE;
                        navigationView.getMenu().findItem(R.id.nav_profile).setChecked(true);
                        navigationView.getMenu().findItem(R.id.nav_setting).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_help).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_logout).setChecked(false);
                        break;
                    case 4:
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
    @SuppressLint("NonConstantResourceId")
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
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_profile:
                checkLogicScreen(FRAGMENT_PROFILE, 3);
                break;
            case R.id.nav_setting:
                checkLogicScreen(FRAGMENT_SETTING, 4);
                break;
            case R.id.nav_help://ấn vào help sẽ chuyển activities
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
            alertDialog();
        }
    }

    public void alertDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Bạn có muốn đăng xuất?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Có",
                (dialog, id) -> {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(UserInterfaceActivity.this, Login.class));
                });
        builder1.setNegativeButton(
                "Không",
                (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @SuppressLint("SetTextI18n")
    public void alertDialogTopic(Topic topic) {
        List<ProcessTopicItem> processTopicItemList = new ArrayList<>();
        daoQuestion = new DAOQuestion(this);

        if (topic != null) {
            daoQuestion.getDataFromRealTimeToList(topic);
        }
        Dialog dialog = new Dialog(UserInterfaceActivity.this);
        dialog.setContentView(R.layout.layout_popup_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tvLevel = dialog.findViewById(R.id.tv_level_topic);
        TextView tvTitle = dialog.findViewById(R.id.tv_title_topic);
        Button learn = dialog.findViewById(R.id.btn_learn_topic);
        Button test = dialog.findViewById(R.id.btn_test_topic);
        tvTitle.setText("Hãy cố gắng lên");
        tvLevel.setText("Level: 1");
        ProcessTopic_Adapter processTopic_adapter = new ProcessTopic_Adapter(dialog.getContext());
        assert topic != null;
        daoProcessUser.getDataFromRealTimeFirebase(firebaseUser.getUid(), topic.getId(), processTopicItemList, processTopic_adapter, tvTitle, tvLevel);
        learn.setOnClickListener(v -> {
            if (daoQuestion.getQuestionList().size() > 0) {
                Intent intent = new Intent(UserInterfaceActivity.this, TestEnglishActivity.class);
                intent.putExtra("listQuestion", (Serializable) daoQuestion.getQuestionList());
                intent.putExtra("learn", DEFAULTVALUE.LEARN);
                startActivity(intent);
            } else {
                DEFAULTVALUE.alertDialogMessage("Thông báo", "Chủ đề này hiện không có câu hỏi", UserInterfaceActivity.this);
            }
        });
        test.setOnClickListener(v -> {
            if (daoQuestion.getQuestionList().size() > 0) {
                Intent intent = new Intent(UserInterfaceActivity.this, TestEnglishActivity.class);
                intent.putExtra("listQuestion", (Serializable) daoQuestion.getQuestionList());
                intent.putExtra("learn", DEFAULTVALUE.TEST);
                intent.putExtra("idTopic", topic.getId());
                intent.putExtra("userID", firebaseUser.getUid());
                startActivity(intent);
            } else {
                DEFAULTVALUE.alertDialogMessage("Thông báo", "Chủ đề này hiện không có câu hỏi", UserInterfaceActivity.this);
            }
        });
        RecyclerView rcvLevelTopic = dialog.findViewById(R.id.rcvImageTopic);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(dialog.getContext(), LinearLayoutManager.HORIZONTAL, false);
        processTopic_adapter.setProcessTopicItemList(processTopicItemList);
        rcvLevelTopic.setLayoutManager(linearLayoutManager);
        rcvLevelTopic.setAdapter(processTopic_adapter);
        dialog.show();
    }

}