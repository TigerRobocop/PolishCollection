package com.unibratec.livia.polishcollection.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Livia on 31/10/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "dbPolish";
    public static final int DB_VERSION = 1;
    public static final String TBL_POLISH = "tbl_Polish";

    public static final String COL_dbID = "col_dbId";
    public static final String COL_ID = "col_id";
    public static final String COL_BRAND = "col_brand";
    public static final String COL_FINISH = "col_finish";
    public static final String COL_COLOR = "col_color";
    public static final String COL_NAME = "col_name";
    public static final String COL_IMGURL = "col_imgurl";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TBL_POLISH + "(" +
                COL_dbID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ID + " INTEGER NOT NULL, " +
                COL_BRAND + " INTEGER NOT NULL, " +
                COL_FINISH + " TEXT, " +
                COL_COLOR + " TEXT NOT NULL, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_IMGURL + " TEXT NOT NULL )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
