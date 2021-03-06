package com.example.EnglishBeginner.main_interface;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.EnglishBeginner.Adapter.ProcessTopic_Adapter;
import com.example.EnglishBeginner.DTO.DEFAULTVALUE;
import com.example.EnglishBeginner.DTO.ProcessTopicItem;
import com.example.EnglishBeginner.DTO.Question;
import com.example.EnglishBeginner.DTO.Topic;
import com.example.EnglishBeginner.DTO.User;
import com.example.EnglishBeginner.Login.Login;
import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.learn.testing.TestEnglishActivity;
import com.example.EnglishBeginner.profile.HelpActivity;
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
    //Khai b??o c??c tr?????ng d??? li???u ????? l???y data tr??n firebase
    public FirebaseUser firebaseUser;
    public DatabaseReference databaseReference;
    private TextView tvUserName, tvUserEmail;
    private ImageView imgUserName;

    //khai b??o gi?? tr??? cho screen
    public static final int FRAGMENT_LEARN = 0;
    public static final int FRAGMENT_BLOG = 1;
    public static final int FRAGMENT_VOCABULARY = 2;
    public static final int FRAGMENT_PROFILE = 3;
    public static final int FRAGMENT_SETTING = 4;
    public static final int FRAGMENT_ALARM = 5;


    //khai b??o gi?? tr??? m??n h??nh hi???n t???i, m???c ?????nh l?? home
    public int myCurrentViewpager2;

    //khai b??o drawerlayout
    private DrawerLayout drawerLayout;

    //Khai b??o navigationView
    public NavigationView navigationView;

    //khai b??o bottom navigation
    private BottomNavigationView bottomNavigationView;

    //khai b??o Viewpager2 v?? adapter c???a n??
    public ViewPager2 viewPager2;
    public ViewPager2_Adapter viewPager2_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setControl();
        //x??? l?? currentScreen khi t??? activity kh??c tr??? l???i
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            checkLogicScreen(FRAGMENT_PROFILE, Integer.parseInt(bundle.getString("currentScreen")));
        }
        getProFileFromRealTime();
        checkLogicDrawerLayout();
        processBottomNavigation();
        processViewPager2();
    }

    //ki???m tra logic khi drawer layout ????ng
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
                        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_blog).setChecked(true);
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
                    case 5:
                        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_profile).setChecked(true);
                        break;
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }
    //l???y d??? li???u user

    private void getProFileFromRealTime() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
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

    // kh????i ga??n gia?? tri?? t????i ca??c view
    private void getDataUserProfileToControl(User user) {
        if (user != null) {
            tvUserName.setText(user.getFullname());
            tvUserEmail.setText(user.getEmail());
            if (!(user.getImageUser().trim().length() == 0 && user.getImageUser().isEmpty())) {
                Glide.with(UserInterfaceActivity.this).load(user.getImageUser()).into(imgUserName);
            }
        }
    }

    //??nh x???, kh???i g??n gi?? tr???,...
    private void setControl() {
        //drawe layout
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // get view control trong navigationview co?? ch????a drawable
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

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(8));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float v = 1 - Math.abs(position);
                page.setScaleY(0.8f + v * 0.2f);
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);
    }

    //x??? l?? Viewpager2
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
                        myCurrentViewpager2 = FRAGMENT_BLOG;
                        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_blog).setChecked(true);
                        break;
                    case 2:
                        myCurrentViewpager2 = FRAGMENT_VOCABULARY;
                        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_vocabulary).setChecked(true);
                        break;
                    case 3:
                        myCurrentViewpager2 = FRAGMENT_PROFILE;
                        navigationView.getMenu().findItem(R.id.nav_profile).setChecked(true);
                        navigationView.getMenu().findItem(R.id.nav_setting).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_alarm).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_help).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_logout).setChecked(false);
                        break;
                    case 4:
                        myCurrentViewpager2 = FRAGMENT_SETTING;
                        navigationView.getMenu().findItem(R.id.nav_profile).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_setting).setChecked(true);
                        navigationView.getMenu().findItem(R.id.nav_alarm).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_logout).setChecked(false);
                        break;
                    case 5:
                        myCurrentViewpager2 = FRAGMENT_ALARM;
                        navigationView.getMenu().findItem(R.id.nav_profile).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_setting).setChecked(false);
                        navigationView.getMenu().findItem(R.id.nav_alarm).setChecked(true);
                        navigationView.getMenu().findItem(R.id.nav_logout).setChecked(false);
                        break;
                }
            }
        });
    }

    //x??? l?? bottom Navigation
    @SuppressLint("NonConstantResourceId")
    private void processBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            switch (id) {
                case R.id.bottom_nav_learn:
                    checkLogicScreen(FRAGMENT_LEARN, 0);
                    break;
                case R.id.bottom_nav_blog:
                    checkLogicScreen(FRAGMENT_BLOG, 1);
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

    //x??? l?? navigation drawer
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
            case R.id.nav_alarm:
                checkLogicScreen(FRAGMENT_ALARM, 5);
                break;
            case R.id.nav_help://???n v??o help s??? chuy???n activities
                Intent intent = new Intent(UserInterfaceActivity.this, HelpActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                alertDialog();
                navigationView.getMenu().findItem(R.id.nav_profile).setChecked(false);
                navigationView.getMenu().findItem(R.id.nav_setting).setChecked(false);
                navigationView.getMenu().findItem(R.id.nav_alarm).setChecked(false);
                navigationView.getMenu().findItem(R.id.nav_logout).setChecked(false);
                bottomNavigationView.getMenu().findItem(R.id.bottom_nav_profile).setChecked(false);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.END);
        return true;
    }

    //h??m x??? l?? ki???m tra logic m??n h??nh
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
        builder1.setMessage("B???n c?? mu???n ????ng xu???t?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "C??",
                (dialog, id) -> {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(UserInterfaceActivity.this, Login.class));
                });
        builder1.setNegativeButton(
                "Kh??ng",
                (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @SuppressLint("SetTextI18n")
    public void alertDialogTopic(Topic topic) {
        List<ProcessTopicItem> processTopicItemList = new ArrayList<>();
        Dialog dialog = new Dialog(UserInterfaceActivity.this);
        dialog.setContentView(R.layout.layout_popup_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tvLevel = dialog.findViewById(R.id.tv_level_topic);
        TextView tvTitle = dialog.findViewById(R.id.tv_title_topic);
        Button learn = dialog.findViewById(R.id.btn_learn_topic);
        Button test = dialog.findViewById(R.id.btn_test_topic);
        RelativeLayout relativeLayout = dialog.findViewById(R.id.rltPopupDialog);
        List<Question> questionList = new ArrayList<>();
        tvTitle.setText("Ha??y c???? g????ng l??n");
        tvLevel.setText("Level: 1");
        ProcessTopic_Adapter processTopic_adapter = new ProcessTopic_Adapter(dialog.getContext());
        RecyclerView rcvLevelTopic = dialog.findViewById(R.id.rcvImageTopic);
        assert topic != null;
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("listquestion");
        databaseReference1.orderByChild("idTopic").equalTo(topic.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (questionList != null) {
                    questionList.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Question question = dataSnapshot.getValue(Question.class);
                    questionList.add(question);
                }
                learn.setOnClickListener(v -> {
                    if (questionList.size() > 0) {
                        Intent intent = new Intent(UserInterfaceActivity.this, TestEnglishActivity.class);
                        intent.putExtra("listQuestion", (Serializable) questionList);
                        intent.putExtra("learn", DEFAULTVALUE.LEARN);
                        startActivity(intent);
                    } else {
                        DEFAULTVALUE.alertDialogMessage("Th??ng ba??o", "Chu?? ?????? na??y hi????n kh??ng co?? c??u ho??i", UserInterfaceActivity.this);
                    }
                });
                test.setOnClickListener(v -> {
                    if (questionList.size() > 0) {
                        Intent intent = new Intent(UserInterfaceActivity.this, TestEnglishActivity.class);
                        intent.putExtra("listQuestion", (Serializable) questionList);
                        intent.putExtra("learn", DEFAULTVALUE.TEST);
                        intent.putExtra("idTopic", topic.getId());
                        intent.putExtra("userID", firebaseUser.getUid());
                        startActivity(intent);
                    } else {
                        DEFAULTVALUE.alertDialogMessage("Th??ng ba??o", "Chu?? ?????? na??y hi????n kh??ng co?? c??u ho??i", UserInterfaceActivity.this);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(dialog.getContext(), LinearLayoutManager.HORIZONTAL, false);
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("listProcessUser");
        databaseReference2.child(firebaseUser.getUid()).child("listTopic/" + topic.getId() + "/listProcess").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (processTopicItemList != null) {
                            processTopicItemList.clear();
                        }
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            ProcessTopicItem processTopicItem = dataSnapshot.getValue(ProcessTopicItem.class);
                            processTopicItemList.add(processTopicItem);
                        }
                        if (processTopicItemList.get(processTopicItemList.size() - 1).getProgress() == 2) {
                            tvLevel.setText("Level: Huy????n thoa??i");
                            tvTitle.setText("Ba??n ??a?? th??ng tha??o ky?? n??ng na??y!");
                            test.setText("Luy????n t????p");
                            relativeLayout.setBackgroundResource(R.drawable.ct_layout_popup_dialog1);
                        }
                        processTopic_adapter.setProcessTopicItemList(processTopicItemList);
                        rcvLevelTopic.setLayoutManager(linearLayoutManager);
                        rcvLevelTopic.setAdapter(processTopic_adapter);
                        processTopic_adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        dialog.show();
    }

}