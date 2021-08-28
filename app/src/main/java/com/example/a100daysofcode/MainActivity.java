package com.example.a100daysofcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    RecyclerView daysListRv;
    TextView timeLeft;
    public ArrayList<Problem> mData;
    public ArrayList<Problem> tillToday;
    DaysAdapter mAdapter;

    public static final String TAG = "MainActivity";
    public static final String PREFERENCES = "preferences";
    public static final String DAY = "day";
    public static final String FIRST_TIME = "firstTime";
    public static final String ENDED = "ended";

    public ArrayList<Boolean> ended;

    public static boolean firstTime;
    public static int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);

        firstTime = sharedPreferences.getBoolean(FIRST_TIME, true);
        count = sharedPreferences.getInt(BackgroundService.COUNT, 0);

        ProblemsDatabase db = new ProblemsDatabase(this);
        mData = db.getEveryone(ProblemsDatabase.TABLE);

        tillToday = new ArrayList<>();
        for(int i=0; i<=count; i++) {
            tillToday.add(mData.get(i));
        }

        timeLeft = findViewById(R.id.timeLeft);
        daysListRv = findViewById(R.id.days_list_rv);
        mAdapter = new DaysAdapter(tillToday);
        daysListRv.setAdapter(mAdapter);
        daysListRv.setLayoutManager(new LinearLayoutManager(this));
        daysListRv.addItemDecoration(new DividerItemDecoration(daysListRv.getContext(), DividerItemDecoration.VERTICAL));

//          Starting background activity first time app runs
//        Intent service = null;
        if(firstTime) {
            SharedPreferences preferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(FIRST_TIME, false);
            editor.apply();
            Intent service = new Intent(MainActivity.this, BackgroundService.class);
            startService(service);
        }

        ImageButton refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int extra = sharedPreferences.getInt(BackgroundService.COUNT, 0);
                tillToday.clear();
                ProblemsDatabase db = new ProblemsDatabase(MainActivity.this);
                mData = db.getEveryone(ProblemsDatabase.TABLE);
                for(int i=0; i<=extra; i++) {
                    tillToday.add(mData.get(i));
                }
                Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                mAdapter.notifyDataSetChanged();
                daysListRv.smoothScrollToPosition(tillToday.size()-1);
            }
        });

        //  Receiving Data from background
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("counter");
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long time = intent.getLongExtra("remaining", 0);
                timeLeft.setText(formatTime(time));
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);

    }

    public static String formatTime(long millis) {
        @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
                );
        return time;
    }

    class DaysAdapter extends RecyclerView.Adapter<DaysViewHolder> {
        ArrayList<Problem> data;

        public DaysAdapter(ArrayList<Problem> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public DaysViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.days_list_vh, parent, false);
            return new DaysViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DaysViewHolder holder, int position) {
            int currentPosition = holder.getAdapterPosition();
            Problem p = data.get(currentPosition);
            holder.dayNumber.setText("Day " + String.valueOf(currentPosition + 1));
            holder.problemTitle.setText(p.getTitle());
            if (p.isStatus() == 1) {
                holder.problemStatus.setText("Completed");
            } else if(p.isStatus() == 0) {
                holder.problemStatus.setText("Not Completed");
            } else {
                holder.problemStatus.setText("Time Up!!");
            }
            holder.daysList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean current = false;
                    if(currentPosition == data.size()-1) {
                        current = true;
                    }
                    Intent intent = new Intent(MainActivity.this, ProblemDescription.class);
                    intent.putExtra("DAY", currentPosition + 1);
                    intent.putExtra("PROBLEM", p);
                    intent.putExtra("CURRENT", current);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    class DaysViewHolder extends RecyclerView.ViewHolder {
        TextView dayNumber;
        TextView problemTitle;
        TextView problemStatus;
        RelativeLayout daysList;
        public DaysViewHolder(@NonNull View itemView) {
            super(itemView);
            daysList = itemView.findViewById(R.id.days_list_layout);
            dayNumber = itemView.findViewById(R.id.day_number);
            problemTitle = itemView.findViewById(R.id.problem_title);
            problemStatus = itemView.findViewById(R.id.problem_status);
        }
    }
}