package doron_vaskoinc.exifeditor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ImageDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "assets.db";
    private static final String TABLE_NAME = "assets";
    public static final String COL_1 = "ID";
    private static final String COL_2 = "URI";
    private static final String COL_3 = "FILEPATH";
    private static final String COL_4 = "FILENAME";
    private static final String COL_5 = "LAT";
    private static final String COL_6 = "LATREF";
    private static final String COL_7 = "LON";
    private static final String COL_8 = "LONREF";
    private static final String COL_9 = "DATETIME";
    private static final String COL_10 = "MAKE";
    private static final String COL_11 = "MODEL";


    ImageDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,URI TEXT,FILEPATH TEXT,FILENAME TEXT,LAT DOUBLE,LATREF TEXT, LON DOUBLE, LONREF TEXT, DATETIME TEXT, MAKE TEXT, MODEL TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    boolean insertData(String uri, String filepath, String filename, Double lat, String latref, Double lon, String lonref, String datetime, String make, String model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, uri);
        contentValues.put(COL_3, filepath);
        contentValues.put(COL_4, filename);
        contentValues.put(COL_5, lat);
        contentValues.put(COL_6, latref);
        contentValues.put(COL_7, lon);
        contentValues.put(COL_8, lonref);
        contentValues.put(COL_9, datetime);
        contentValues.put(COL_10, make);
        contentValues.put(COL_11, model);


        long result = db.insert(TABLE_NAME, null, contentValues);

        return result != -1;

    }

    Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from " + TABLE_NAME,null);
    }
}
