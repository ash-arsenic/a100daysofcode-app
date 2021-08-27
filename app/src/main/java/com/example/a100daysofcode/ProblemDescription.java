package com.example.a100daysofcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProblemDescription extends AppCompatActivity implements CheckAnswerDialog.AnswerDialogListener{

    Problem problem;
    int day_num;

    TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_description);
        problem = (Problem) getIntent().getSerializableExtra("PROBLEM");
        day_num = getIntent().getIntExtra("DAY", 0);

//        To be deleted
        boolean current = getIntent().getBooleanExtra("CURRENT", false);

        TextView day = findViewById(R.id.problem_day);
        day.setText("Day "+String.valueOf(day_num));

        status = (TextView) findViewById(R.id.problem_status_description);
        if (problem.isStatus() == 1) {
            status.setText("Completed");
        } else if(problem.isStatus() == 0) {
            status.setText("Not Completed");
        } else {
            status.setText("Time Up!!");
        }

        TextView problemDescription = (TextView) findViewById(R.id.problem_statement);
        problemDescription.setText(problem.getStatement());

        Button checkAnswer = findViewById(R.id.check_answer_btn);
        checkAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckAnswerDialog dialog = new CheckAnswerDialog();
                dialog.show(getSupportFragmentManager(), "Check Answer Dialog");
            }
        });

        TextView timeLeft = findViewById(R.id.problem_time_left);
        if (problem.isStatus() == 0) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("counter");
            BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    long time = intent.getLongExtra("remaining", 0);
                    timeLeft.setText(MainActivity.formatTime(time));
                }
            };
            registerReceiver(broadcastReceiver, intentFilter);
        }
    }

    @Override
    public void applyText(String ans) {
        if(problem.getAnswer().equals(ans)) {
            ProblemsDatabase db = new ProblemsDatabase(this);
            if(db.update(new Problem(problem.getTitle(), problem.getStatement(), problem.getAnswer(), problem.getDifficulty(), 1), day_num)) {
                Toast.makeText(this, "Correct Answer", Toast.LENGTH_SHORT).show();
                status.setText("Correct");
            }
        } else {
            Toast.makeText(this, "Wrong Answer", Toast.LENGTH_SHORT).show();
        }
    }
}