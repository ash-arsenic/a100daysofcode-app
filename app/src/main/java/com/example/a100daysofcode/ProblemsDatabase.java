package com.example.a100daysofcode;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ProblemsDatabase extends SQLiteOpenHelper {

    public static final String TABLE = "PROBLEMS";
    public static final String NUM = "NUM";
    public static final String TITLE = "TITLE";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String ANSWER = "ANSWER";
    public static final String DIFFICULTY = "DIFFICULTY";
    public static final String STATUS = "STATUS";

    public ProblemsDatabase(@Nullable Context context) {
        super(context, "problems.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE + " (" + NUM + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TITLE + " TEXT, " + DESCRIPTION + " TEXT, " + ANSWER + " TEXT, " + DIFFICULTY +" TEXT, " + STATUS + " INTEGER);");

        String title[] = {"Prime digit replacements",
                "Permuted multiples",
                "Combinatoric selections",
                "Poker hands",
                "Lychrel numbers",
                "Powerful digit sum",
                "Square root convergents",
                "Spiral primes",
                "XOR decryption",
                "Prime pair sets"
        };

        String description =
                "It can be seen that the number, 125874, and its double, 251748, contain exactly the same digits, but in a different order.\n" +
                "\n" +
                "Find the smallest positive integer, x, such that 2x, 3x, 4x, 5x, and 6x, contain the same digits.";

        String answer = "420356";

        for(int i=0; i<10; i++) {
            Problem problem = new Problem(title[i], description, answer, "easy", 0);
            addOne(problem, TABLE, sqLiteDatabase);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addOne(Problem problem, String table, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();

        cv.put(TITLE, problem.getTitle());
        cv.put(DESCRIPTION, problem.getStatement());
        cv.put(ANSWER, problem.getAnswer());
        cv.put(DIFFICULTY, problem.getDifficulty());
        cv.put(STATUS, problem.isStatus());

        long insert = db.insert(table, null, cv);

        return insert != -1;
    }

    public boolean update(Problem problem, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TITLE, problem.getTitle());
        cv.put(DESCRIPTION, problem.getStatement());
        cv.put(ANSWER, problem.getAnswer());
        cv.put(DIFFICULTY, problem.getDifficulty());
        cv.put(STATUS, problem.isStatus());

        long insert = db.update(TABLE, cv, NUM+" = ? ", new String[]{String.valueOf(id)});

        return insert != -1;
    }

    public ArrayList<Problem> getEveryone(String table) {
        ArrayList<Problem> data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + table, null);

        if (cursor.moveToFirst()) {
            do {
                String problemTitle = cursor.getString(1);
                String problemDescription = cursor.getString(2);
                String problemAnswer = cursor.getString(3);
                String problemDifficulty = cursor.getString(4);
                int status = cursor.getInt(5);

                Problem problem = new Problem(problemTitle, problemDescription, problemAnswer, problemDifficulty, status);
                data.add(problem);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return data;
    }
}
