package org.pursuit.dogbreeds.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.pursuit.dogbreeds.network.DogService;

import java.util.ArrayList;
import java.util.List;

public abstract class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "Sheriff";
    private static DatabaseHelper instance;
    private static final String DATABASE_NAME = "Dogs Database";
    public static final String TABLE_NAME = "Dog_with_Pics";
    public static final String COLUMN1 = "dogName";
    public static final String COLUMN2 = "dogImageUrl";
    public static final int DATABASE_VERSION = 1;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext()) {
                @Override
                public void onCreate(SQLiteDatabase database) {
                    database.execSQL(
                            "CREATE TABLE " + TABLE_NAME +
                                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                    "dogName TEXT, dogImageUrl TEXT);");

                }

                @Override
                public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                    //No-Op

                }
            };
        }
        return instance;
    }


    public void addUrlToBreed(String breeds, String imageUrl) {
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_NAME + ";", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {

                    String result = cursor.getString(1);
                    if (imageUrl.contains(breeds)) {
                        SQLiteDatabase db = this.getWritableDatabase();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(COLUMN2, imageUrl);
                        db.insert(TABLE_NAME, null, contentValues);
                    }


                } while (cursor.moveToNext());
                cursor.close();
            }
        }

    }

    public void addDogListToDatabase(List<String> dogNames) {
        for (String dog : dogNames) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN1, dog);
            db.insert(TABLE_NAME, null, contentValues);

        }
    }

    public List<String> getBreedList() {
        List<String> breedList = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_NAME + ";", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    cursor.getString(1);
                    String breed = cursor.getString(1);

                    breedList.add(breed);

                } while (cursor.moveToNext());
                cursor.close();
            }
        }
        Log.d(TAG, "getOutcomeList: " + breedList);
        return breedList;
    }

    public String getBreed(String breed) {
        String result = " ";
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_NAME + ";", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    result = cursor.getString(1);
                    if (breed.equals(result)) {
                        return result;
                    }

                } while (cursor.moveToNext());
                cursor.close();
            }
        }
        return result;
    }

    public String getImageUrl(String imageUrl) {
        String result = " ";
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_NAME + ";", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    result = cursor.getString(2);
                    if (imageUrl.equals(result)) {
                        return result;
                    }

                } while (cursor.moveToNext());
                cursor.close();
            }
        }
        return result;
    }


}
