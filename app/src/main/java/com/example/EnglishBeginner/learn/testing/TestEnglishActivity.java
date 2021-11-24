package com.example.EnglishBeginner.learn.testing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EnglishBeginner.Adapter.ReviewCourse_Adapter;
import com.example.EnglishBeginner.DAO.DAOProcessUser;
import com.example.EnglishBeginner.DTO.DEFAULTVALUE;
import com.example.EnglishBeginner.DTO.Question;
import com.example.EnglishBeginner.DTO.ReviewCourse;
import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.learn.FinishEnglishFragment;
import com.example.EnglishBeginner.learn.learning.LearningEnglishFragment;
import com.example.EnglishBeginner.main_interface.UserInterfaceActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import pl.droidsonroids.gif.GifImageView;

public class TestEnglishActivity extends AppCompatActivity implements View.OnClickListener, TextToSpeech.OnInitListener {
    private ArrayList<ReviewCourse> reviewCourseArrayList;
    private ProgressBar progressBar;
    private ImageView imgExit;
    @SuppressLint("StaticFieldLeak")
    public static Button btnPass, btnSubmit;
    private List<Question> arrayListQuestion;
    private int count = 0, countcorrect = 0;
    private int max = 0;
    private int maxTypeQuestion = 3;
    public String correctQuestion = null, answer = null;
    private int randomTypeQuestion = 0;
    private String typeLearn = null;
    private int idTopic;
    private String userID;
    private DAOProcessUser daoProcessUser;
    public Boolean checkFinishResult = true;
    public int countSkip = 0;
    private Question question;
    private TextToSpeech textToSpeech;
    protected static final int RESULT_SPEECH = 1;
    private String corectAnswer = "";
    private List<String>typeQuestionList;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_english);
        textToSpeech = new TextToSpeech(this, this);
        reviewCourseArrayList = new ArrayList<>();
        daoProcessUser = new DAOProcessUser(this);
        typeQuestionList = new ArrayList<>();
        typeQuestionList = Arrays.asList(getResources().getStringArray(R.array.typeQuestion));
        Intent intent = getIntent();
        if (intent != null) {
            arrayListQuestion = (List<Question>) intent.getSerializableExtra("listQuestion");
            typeLearn = intent.getStringExtra("learn");
            idTopic = intent.getIntExtra("idTopic", -1);
            userID = intent.getStringExtra("userID");
        }
        if (arrayListQuestion != null && arrayListQuestion.size() > 0) {
            max = arrayListQuestion.size() - 1;
        }
        imgExit = findViewById(R.id.img_btn_exit);
        btnPass = findViewById(R.id.btn_pass);
        btnPass.setOnClickListener(this);
        btnSubmit = findViewById(R.id.btn_continute);
        btnSubmit.setOnClickListener(this);
        progressBar = findViewById(R.id.learnTopic_progress_bar);
        //Sử kiện nút trở lại
        imgExit.setOnClickListener(v -> {
            Intent intent1 = new Intent(TestEnglishActivity.this, UserInterfaceActivity.class);
            startActivity(intent1);
        });
        processLearn();
    }

    @SuppressLint("SetTextI18n")
    private void processLearn() {
        int min = 0;
        int randomQuestion = (int) Math.floor(Math.random() * (max - min + 1) + min);
        question = arrayListQuestion.get(randomQuestion);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (arrayListQuestion.size() < 10) {
            progressBar.setMax(arrayListQuestion.size());
            if (count < arrayListQuestion.size()) {
                transactionFragment();
            } else {
                if (typeLearn.equalsIgnoreCase(DEFAULTVALUE.TEST)) {
                    btnPass.setText("Xem lại bài học");
                    imgExit.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    if (countcorrect >= arrayListQuestion.size()) {
                        daoProcessUser.updateProcess(userID, idTopic);
                        checkFinishResult = true;
                    } else {
                        checkFinishResult = false;
                    }
                    fragmentTransaction.replace(R.id.frameLayout_Fragment, new FinishEnglishFragment()).commit();
                } else if (typeLearn.equalsIgnoreCase(DEFAULTVALUE.LEARN)) {
                    btnPass.setVisibility(View.GONE);
                    imgExit.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    fragmentTransaction.replace(R.id.frameLayout_Fragment, new FinishEnglishFragment()).commit();
                }
            }
        } else {
            progressBar.setMax(10);
            if (count < 10) {
                transactionFragment();
            } else {
                if (typeLearn.equalsIgnoreCase(DEFAULTVALUE.TEST)) {
                    btnPass.setText("Xem lại bài học");
                    imgExit.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    if (countcorrect >= 8) {
                        daoProcessUser.updateProcess(userID, idTopic);
                        checkFinishResult = true;
                    } else {
                        checkFinishResult = false;
                    }
                    fragmentTransaction.replace(R.id.frameLayout_Fragment, new FinishEnglishFragment()).commit();
                } else if (typeLearn.equalsIgnoreCase(DEFAULTVALUE.LEARN)) {
                    btnPass.setVisibility(View.GONE);
                    imgExit.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    fragmentTransaction.replace(R.id.frameLayout_Fragment, new FinishEnglishFragment()).commit();
                }
            }
        }
    }

    @SuppressLint("ResourceType")
    private void transactionFragment() {
        FragmentTransaction fragmentTransaction;
        if (count == 0 && countSkip == 0) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
        } else {
            fragmentTransaction = getSupportFragmentManager().beginTransaction().setCustomAnimations(R.transition.transisionfragmentlearn, R.transition.transisionfragmentlearn);
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("question", question);
        randomTypeQuestion = (int) Math.floor(Math.random() * (typeQuestionList.size()-1 + 1) + 0);
        correctQuestion = question.getCorrectAnswer();
        if (typeLearn.equalsIgnoreCase(DEFAULTVALUE.TEST)) {
            btnPass.setVisibility(View.VISIBLE);
            if (typeQuestionList.get(randomTypeQuestion).equalsIgnoreCase(DEFAULTVALUE.IMAGE)) {
                TestChooseImageFragment testChooseImageFragment = new TestChooseImageFragment();
                testChooseImageFragment.setArguments(bundle);
                if (count == 0 && countSkip == 0) {
                    fragmentTransaction.add(R.id.frameLayout_Fragment, testChooseImageFragment).commit();
                } else if (count == 0 && countSkip > 0) {
                    fragmentTransaction.replace(R.id.frameLayout_Fragment, testChooseImageFragment).commit();
                    countSkip = 0;
                } else {
                    fragmentTransaction.replace(R.id.frameLayout_Fragment, testChooseImageFragment).commit();
                }
            } else if (typeQuestionList.get(randomTypeQuestion).equalsIgnoreCase(DEFAULTVALUE.LISTEN)) {
                TestListenFragment testListenFragment = new TestListenFragment();
                testListenFragment.setArguments(bundle);
                if (count == 0 && countSkip == 0) {
                    fragmentTransaction.add(R.id.frameLayout_Fragment, testListenFragment).commit();
                } else if (count == 0 && countSkip > 0) {
                    fragmentTransaction.replace(R.id.frameLayout_Fragment, testListenFragment).commit();
                    countSkip = 0;
                } else {
                    fragmentTransaction.replace(R.id.frameLayout_Fragment, testListenFragment).commit();
                }
            } else if (typeQuestionList.get(randomTypeQuestion).equalsIgnoreCase(DEFAULTVALUE.WRITE)) {
                TestWriteFragment testWriteFragment = new TestWriteFragment();
                testWriteFragment.setArguments(bundle);
                if (count == 0 && countSkip == 0) {
                    fragmentTransaction.add(R.id.frameLayout_Fragment, testWriteFragment).commit();
                } else if (count == 0 && countSkip > 0) {
                    fragmentTransaction.replace(R.id.frameLayout_Fragment, testWriteFragment).commit();
                    countSkip = 0;
                } else {
                    fragmentTransaction.replace(R.id.frameLayout_Fragment, testWriteFragment).commit();
                }
            } else if (typeQuestionList.get(randomTypeQuestion).equalsIgnoreCase(DEFAULTVALUE.READ)) {
                TestSelectionEnglishFragment testSelectionFragment = new TestSelectionEnglishFragment();
                testSelectionFragment.setArguments(bundle);
                if (count == 0 && countSkip == 0) {
                    fragmentTransaction.add(R.id.frameLayout_Fragment, testSelectionFragment).commit();
                } else if (count == 0 && countSkip > 0) {
                    fragmentTransaction.replace(R.id.frameLayout_Fragment, testSelectionFragment).commit();
                    countSkip = 0;
                } else {
                    fragmentTransaction.replace(R.id.frameLayout_Fragment, testSelectionFragment).commit();
                }
            }
        } else if (typeLearn.equalsIgnoreCase(DEFAULTVALUE.LEARN)) {
            btnPass.setVisibility(View.GONE);
            LearningEnglishFragment learningEnglishFragment = new LearningEnglishFragment();
            learningEnglishFragment.setArguments(bundle);
            if (count == 0 && countSkip == 0) {
                fragmentTransaction.add(R.id.frameLayout_Fragment, learningEnglishFragment).commit();
            } else if (count == 0 && countSkip > 0) {
                fragmentTransaction.replace(R.id.frameLayout_Fragment, learningEnglishFragment).commit();
                countSkip = 0;
            } else {
                fragmentTransaction.replace(R.id.frameLayout_Fragment, learningEnglishFragment).commit();
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_continute:
                submitResult();
                break;
            case R.id.btn_pass:
                if (arrayListQuestion.size() < 10) {
                    if (count >= arrayListQuestion.size()) {
                        alertDialogReviewCourse();
                    } else {
                        skipQuestion();
                    }
                } else {
                    if (count < 10) {
                        skipQuestion();
                    } else {
                        alertDialogReviewCourse();
                    }
                }
                break;
        }
    }

    private void alertDialogReviewCourse() {
        Dialog dialog = new Dialog(TestEnglishActivity.this);
        dialog.setContentView(R.layout.layout_review_course);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        window.setAttributes(windowAttributes);
        ImageView imgExit = dialog.findViewById(R.id.btn_close);
        imgExit.setOnClickListener(v -> dialog.dismiss());
        RecyclerView recyclerView = dialog.findViewById(R.id.recycleview_review_course);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(dialog.getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        ReviewCourse_Adapter reviewCourse_adapter = new ReviewCourse_Adapter(dialog.getContext());
        reviewCourse_adapter.setLevelArrayList(reviewCourseArrayList);
        recyclerView.setAdapter(reviewCourse_adapter);
        reviewCourse_adapter.setInterface_course(corectAnswer -> {
            setCorectAnswer(corectAnswer);
            texttoSpeak();
        });
        dialog.show();

    }

    public void setCorectAnswer(String corectAnswer) {
        this.corectAnswer = corectAnswer;
    }

    @SuppressLint("ObsoleteSdkInt")
    private void texttoSpeak() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(corectAnswer, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_SPEECH) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (corectAnswer != null) {
                    corectAnswer = text.get(0);
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void submitResult() {
        ReviewCourse reviewCourse = new ReviewCourse();
        if (count < arrayListQuestion.size() && count < 10) {
            if (typeLearn.equalsIgnoreCase(DEFAULTVALUE.TEST)) {
                reviewCourse.setCorrectAnswer(question.getCorrectAnswer());
                reviewCourse.setQuestion(question.getTitle());
                reviewCourse.setUserAnswer(answer);
                reviewCourse.setTypeQuestion(typeQuestionList.get(randomTypeQuestion));
                if (answer == null) {
                    alertDialog("Không chính xác", false, 1);
                    reviewCourse.setCheck(false);
                } else {
                    if (answer.trim().equalsIgnoreCase(correctQuestion.trim())) {
                        alertDialog("Chính xác", true, 1);
                        countcorrect++;
                        reviewCourse.setCheck(true);
                    } else {
                        alertDialog("Không chính xác", false, 1);
                        reviewCourse.setCheck(false);
                    }
                    answer = "";
                }
                reviewCourseArrayList.add(reviewCourse);
            } else if (typeLearn.equalsIgnoreCase(DEFAULTVALUE.LEARN)) {
                countcorrect++;
                alertDialog("Đã hoàn thành phần học", true, 1);
            }
        } else {
            startActivity(new Intent(TestEnglishActivity.this, UserInterfaceActivity.class));
        }
        count++;
        progressBar.setProgress(count, true);
    }

    private void skipQuestion() {
        alertDialog("Bạn đã bỏ qua câu hỏi này!", true, 2);
        countSkip++;
    }

    // Xây dựng một Hộp thoại thông báo
    @SuppressLint("UseCompatLoadingForDrawables")
    public void alertDialog(String msg, Boolean check, int choice) {
        Dialog dialog = new Dialog(TestEnglishActivity.this);
        dialog.setContentView(R.layout.layout_notify_answer_correct);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        window.setAttributes(windowAttributes);
        LinearLayout linearLayout = dialog.findViewById(R.id.rltCheckAnswer);
        GifImageView imgResult = dialog.findViewById(R.id.imgResult);
        @SuppressLint("CutPasteId") TextView tvtitle = dialog.findViewById(R.id.id_corect_title);
        Button btnContinueNotify = dialog.findViewById(R.id.btn_continute_notify);
        if (check) {
            imgResult.setImageResource(R.drawable.correct);
            linearLayout.setBackgroundResource(R.color.greenResult);
            tvtitle.setTextColor(Color.parseColor("#58a700"));
            btnContinueNotify.setBackground(getResources().getDrawable(R.drawable.ct_layout_button3, null));
        } else {
            imgResult.setImageResource(R.drawable.incorrect);
            linearLayout.setBackgroundResource(R.color.red_incorrect);
            tvtitle.setTextColor(Color.parseColor("#ea2b2b"));
            btnContinueNotify.setBackground(getResources().getDrawable(R.drawable.ct_layout_button4, null));
        }
        @SuppressLint("CutPasteId") TextView tvCorrect = dialog.findViewById(R.id.id_corect_title);
        tvCorrect.setText(msg);
        btnContinueNotify.setOnClickListener(v ->
        {
            if (choice == 1) {
                if (count > 0) {
                    new CountDownTimer(2000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            btnPass.setEnabled(false);
                            btnSubmit.setEnabled(false);
                        }

                        public void onFinish() {
                            btnPass.setEnabled(true);
                            btnSubmit.setEnabled(true);
                            processLearn();
                        }
                    }.start();
                } else {
                    processLearn();
                }
            } else if (choice == 2) {
                new CountDownTimer(2000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        btnPass.setEnabled(false);
                        btnSubmit.setEnabled(false);
                    }

                    public void onFinish() {
                        btnPass.setEnabled(true);
                        btnSubmit.setEnabled(true);
                        processLearn();
                    }
                }.start();
            }
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.US);
            textToSpeech.setSpeechRate(1.0f);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("error", "This Language is not supported");
            } else {
                texttoSpeak();
            }
        } else {
            Log.e("error", "Failed to Initialize");
        }
    }
}