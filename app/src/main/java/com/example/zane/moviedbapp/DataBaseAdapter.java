package com.example.zane.moviedbapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Movie;
import android.util.Log;

public class DataBaseAdapter extends SQLiteOpenHelper {

    private static final String TAG = "DataBaseAdapter";

    //our singleton value
    private static DataBaseAdapter mInstance;

    //member variable
    //DataBaseAdapter db;

    
    //Database Info
    private static final String DATABASE_NAME = "WatchListDataBase";
    private static final int DATABASE_VERSION = 3;

    //Table Name
    private static final String TABLE_MOVIES = "movies";
    private static final String TABLE_MOVIES_RATED = "movies_rated";

    //Movie Table Columns
    static final String KEY_ID = "id";
    static final String KEY_MOVIE_ID = "movieid";
    static final String KEY_TITLE = "title";
    static final String KEY_POSTER = "poster";
    static final String KEY_RATING = "rating";



    public DataBaseAdapter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "onCreate: Database created");

        //create our tables
        String CREATE_MOVIES_TABLE = "CREATE TABLE " + TABLE_MOVIES +
                "(" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_MOVIE_ID + " INTEGER," +
                    KEY_TITLE + " TEXT, " +
                    KEY_POSTER + " TEXT" +
                ")";

        String CREATE_MOVIES_TABLE_RATED = "CREATE TABLE " + TABLE_MOVIES_RATED +
                "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_MOVIE_ID + " INTEGER," +
                KEY_TITLE + " TEXT, " +
                KEY_POSTER + " TEXT, " +
                KEY_RATING + " INTEGER" +
                ")";

        sqLiteDatabase.execSQL(CREATE_MOVIES_TABLE);
        sqLiteDatabase.execSQL(CREATE_MOVIES_TABLE_RATED);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d(TAG, "onUpgrade: Database upgrading");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES_RATED);
        onCreate(sqLiteDatabase);
    }

    //add movie for watchlist
    public void addMovie(int movieID, String title, String poster){
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        ContentValues values = new ContentValues();
        values.put(KEY_MOVIE_ID, movieID);
        values.put(KEY_TITLE, title);
        values.put(KEY_POSTER, poster);

        db.insert(TABLE_MOVIES, null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    //add movie rating
    public void addRating(int movieID, String title, String poster, int rating){
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        ContentValues values = new ContentValues();
        values.put(KEY_MOVIE_ID, movieID);
        values.put(KEY_TITLE, title);
        values.put(KEY_POSTER, poster);
        values.put(KEY_RATING, rating);

        db.insert(TABLE_MOVIES_RATED, null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    //get all data from table paramater
    public Cursor getAllData(String table){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + table, null);

        return res;
    }

    //check if movie is already in the database
    public boolean alreadyInDatabase(int fieldValue, String table){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select * from " + table + " where " + KEY_MOVIE_ID + " = " + fieldValue;

        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() <= 0) {
            return false;
        }
        cursor.close();
        return true;
    }

    //remove movie from database
    public void deleteMovie(int fieldValue, String table){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(table, KEY_MOVIE_ID + "=" + fieldValue, null);
    }
}
