package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.lang.Thread.sleep;

public class QuizActivity extends AppCompatActivity {

    private TextView questionNumber, score, questionText, timer;
    private Button option1, option2, option3, option4, next;
    private LinearLayout optionContainer;
    private ArrayList<QuestionModel> list = new ArrayList<QuestionModel>();
    final long totalTime = 300000;  // 5 mins
    private long timeLeft = 0;
    private int points = 0;

    private ColorStateList ColorDefault;
    private int questionCountTotal;
    private int position = 0;
    private QuestionModel currentQuestion;
    private boolean answered;
    private int correctAnswers = 0;
    private long backPressedTime;

    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final String KEY_ANSWERED = "keyAnswered";
    private static final String KEY_QUESTION = "keyQuestionList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionNumber = findViewById(R.id.questionNumber);
        score = findViewById(R.id.score);
        questionText = findViewById(R.id.questionText);
        timer = findViewById(R.id.timer);

        optionContainer = findViewById(R.id.optionContainer);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        next = findViewById(R.id.next);

        ColorDefault = option1.getBackgroundTintList();

        timeLeft = totalTime;
        new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long timeLeft) {
                int min = (int) (timeLeft / 1000) / 60;
                int sec = (int) (timeLeft / 1000) % 60;
                if (min < 1) {
                    timer.setTextColor(Color.RED);
                }

                String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", min, sec);
                timer.setText("Time Remaining : " + timeFormatted);
            }

            @Override
            public void onFinish() {
                finishQuiz();
            }
        }.start();

        list.add(new QuestionModel("Software programs that allow you to legally copy files and give them away at no cost are called which of the following?", "Probe ware", "Timeshare", "Public domain", "Shareware", "Public domain"));
        list.add(new QuestionModel("Which type of topology is best suited for large businesses which must carefully control and coordinate the operation of distributed branch outlets?", "Ring", "Local area", "Hierarchical", "Star", "Star"));
        list.add(new QuestionModel("Which of the following transmission directions listed is not a legitimate channel?", "Simplex", "Half Duplex", "Full Duplex", "Double Duplex", "Double Duplex"));
        list.add(new QuestionModel("\"Parity bits\" are used for which of the following purposes?", "Encryption of data", "To transmit faster", " To detect errors", "To identify the user", "To detect errors"));
        list.add(new QuestionModel("What kind of transmission medium is most appropriate to carry data in a computer network that is exposed to electrical interferences?", "Unshielded twisted pair", "Optical fiber", "Coaxial cable", "Microwave", "Optical fiber"));
        list.add(new QuestionModel("Which of the following is not an operating system?", "Windows", "Linux", "Oracle", "DOS", "Oracle"));
        list.add(new QuestionModel("If a page number is not found in the translation lookaside buffer, then it is known as a?", "Translation Lookaside Buffer miss", "Buffer miss", "Translation Lookaside Buffer hit", "All of the mentioned", "Translation Lookaside Buffer miss"));
        list.add(new QuestionModel("Which of the following operating systems does not support more than one program at a time?", "Linux", "Windows", "Mac", "DOS", "DOS"));
        list.add(new QuestionModel("Who provides the interface to access the services of the operating system?", "Library", "System Call", "API", "Assembly instruction", "System Call"));
        list.add(new QuestionModel("Which program runs first after booting the computer and loading the GUI?", "Desktop Manager", "File Manager", "Authentication", "Windows Explorer", "Authentication"));
        questionCountTotal = list.size();

        if (savedInstanceState == null) {

            showNextQuestion();

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!answered) {
                        Toast.makeText(QuizActivity.this, "Please Select One Option", Toast.LENGTH_SHORT).show();
                    } else {
                        position++;
                        showNextQuestion();
                    }
                }
            });
            
            for (int i = 0; i < 4; i++) {
                optionContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Log.d("DEBUG", "OnClick Button");
                        checkAnswer((Button) v);
                    }
                });
            }
        } else {
            list = savedInstanceState.getParcelableArrayList(KEY_QUESTION);
            position = savedInstanceState.getInt(KEY_QUESTION_COUNT) - 1;
            points = savedInstanceState.getInt(KEY_SCORE);
            timeLeft = savedInstanceState.getInt(KEY_MILLIS_LEFT);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED);
        }
    }

    private void showNextQuestion () {
        for (int i = 0; i < 4; i++) {
            optionContainer.getChildAt(i).setEnabled(true);
            optionContainer.getChildAt(i).setBackgroundTintList(ColorDefault);
        }

        if (position < questionCountTotal) {
            setLayout();
            answered = false;
            next.setAlpha(0.7f);
        }
        else {
            finishQuiz();
        }
    }

    private void finishQuiz() {
        timer.setText("Finished!");
        timeLeft = 0;
        Toast.makeText(this, "Finished!", Toast.LENGTH_SHORT).show();
        // Log.d ("DEBUG", "finish Quiz()");

        Intent intent = new Intent(QuizActivity.this, Finish.class);
        intent.putExtra("correctAnswers", String.valueOf(correctAnswers));
        intent.putExtra("score", String.valueOf(points));
        Log.d ("DEBUG", String.valueOf(correctAnswers));

        startActivity(intent);
    }

    private void setLayout() {
        questionNumber.setText("Question " + Integer.toString(position + 1) + " / 10");
        score.setText("SCORE : " + Integer.toString(points));
        questionText.setText(list.get(position).getQuestion());
        option1.setText(list.get(position).getOption1());
        option2.setText(list.get(position).getOption2());
        option3.setText(list.get(position).getOption3());
        option4.setText(list.get(position).getOption4());
    }

    private void checkAnswer (Button selectedOption) {
        // Log.d("DEBUG", "checkAnswer()");

        answered = true;
        next.setAlpha(1);

        if (selectedOption.getText().toString().equals(list.get(position).getAnswer())) {
            points += 5;
            correctAnswers++;
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        }
        else {
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
        }

        for (int i = 0; i < 4; i++) {
            optionContainer.getChildAt(i).setEnabled(false);
        }
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishQuiz();
        } else {
            Toast.makeText(this, "Press BACK again to Finish", Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(KEY_SCORE, points);
        outState.putInt(KEY_QUESTION_COUNT, position);
        outState.putLong(KEY_MILLIS_LEFT, timeLeft);
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTION, list);
    }
}