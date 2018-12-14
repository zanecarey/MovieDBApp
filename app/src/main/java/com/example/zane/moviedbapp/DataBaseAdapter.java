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
    private static final int DATABASE_VERSION = 2;

    //Table Name
    private static final String TABLE_MOVIES = "movies";

    //Movie Table Columns
    static final String KEY_ID = "id";
    static final String KEY_MOVIE_ID = "movieid";
    static final String KEY_TITLE = "title";
    static final String KEY_POSTER = "poster";

    //singleton pattern
//    public static synchronized DataBaseAdapter getInstance(Context context){
//
//        if(mInstance == null){
//            mInstance = new DataBaseAdapter(context.getApplicationContext());
//        }
//        return mInstance;
//    }

    public DataBaseAdapter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "onCreate: Database created");

        //create our table
        String CREATE_MOVIES_TABLE = "CREATE TABLE " + TABLE_MOVIES +
                "(" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_MOVIE_ID + " INTEGER," +
                    KEY_TITLE + " TEXT, " +
                    KEY_POSTER + " TEXT" +
                ")";

        sqLiteDatabase.execSQL(CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d(TAG, "onUpgrade: Database upgrading");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        onCreate(sqLiteDatabase);
    }

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

    public Cursor getAllData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_MOVIES, null);

        return res;
    }

    public boolean alreadyInDatabase(int fieldValue){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select * from " + TABLE_MOVIES + " where " + KEY_MOVIE_ID + " = " + fieldValue;

        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() <= 0) {
            return false;
        }
        cursor.close();
        return true;
    }

    public void deleteMovie(int fieldValue){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_MOVIES, KEY_MOVIE_ID + "=" + fieldValue, null);
    }
}
