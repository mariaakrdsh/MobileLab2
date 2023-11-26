package com.example.mobilelab2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private static final String CREATE_QUERY = "CREATE TABLE students (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT" +
            ", pib TEXT" +
            ", score1 INTEGER" +
            ", score2 INTEGER" +
            ", address TEXT" +
            ");";

    public DBHandler(@Nullable Context context) {
        super(context, "students", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS students");
    }
    public List<Student> getAll() {
        List<Student> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM students";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int studentID = cursor.getInt(0);
                String pib = cursor.getString(1);
                int score1 = cursor.getInt(2);
                int score2 = cursor.getInt(3);
                String address = cursor.getString(4);

                Student newStudent = new Student(studentID, pib, score1, score2, address);
                returnList.add(newStudent);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return returnList;
    }

    public void addStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("pib", student.getPib());
        values.put("score1", student.getScore1());
        values.put("score2", student.getScore2());
        values.put("address", student.getAddress());

        db.insert("students", null, values);
        db.close();
    }

    public List<Student> getStudentsWithAverageAbove60() {
        List<Student> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM students WHERE (score1 + score2) / 2.0 > 60";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int studentID = cursor.getInt(0);
                String pib = cursor.getString(1);
                int score1 = cursor.getInt(2);
                int score2 = cursor.getInt(3);
                String address = cursor.getString(4);

                Student newStudent = new Student(studentID, pib, score1, score2, address);
                returnList.add(newStudent);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return returnList;
    }

    public double getPercentageOfStudentsWithAverageAbove60() {
        double percentage = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to count the total number of students
        String totalStudentsQuery = "SELECT COUNT(*) FROM students";
        Cursor totalCursor = db.rawQuery(totalStudentsQuery, null);
        int totalStudentsCount = 0;
        if (totalCursor.moveToFirst()) {
            totalStudentsCount = totalCursor.getInt(0);
        }
        totalCursor.close();

        // Query to count the number of students with average above 60
        String above60Query = "SELECT COUNT(*) FROM students WHERE (score1 + score2) / 2.0 > 60";
        Cursor above60Cursor = db.rawQuery(above60Query, null);
        int above60Count = 0;
        if (above60Cursor.moveToFirst()) {
            above60Count = above60Cursor.getInt(0);
        }
        above60Cursor.close();

        db.close();

        // Calculate the percentage if total count is not zero
        if (totalStudentsCount > 0) {
            percentage = (double) above60Count / totalStudentsCount * 100;
        }

        return percentage;
    }


}
