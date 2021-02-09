package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Finish extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        Log.d ("DEBUG", "HERE");

        TextView tv1, tv2;
        Button btnTryAgain;

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);

        btnTryAgain = findViewById(R.id.btnTry);

        Intent intent = getIntent();
        String correctAnswers = intent.getStringExtra("correctAnswers");
        String score = intent.getStringExtra("score");

        tv1.setText("Correct Answers : " + correctAnswers);
        tv2.setText("Score : " + score);

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryAgain();
            }
        });
    }

    public void tryAgain() {
        Intent intent = new Intent(Finish.this, MainActivity.class);
        startActivity(intent);
    }
}