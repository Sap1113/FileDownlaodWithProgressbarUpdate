package com.FileDownlaodWithProgressUpdate.MyWork.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {


    public DBHelper(Context context) {
        super(context, Const.DB_NAME, null, 44);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 44:
                db.execSQL("CREATE TABLE FileToByte (trackId INTEGER , trackByte BLOB)");
                break;
        }
    }

    public void execute(String Statment) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL(Statment);
        } catch (Exception e) {
            Log.error("DataBaseHelper - ExecuteQuery", e);
            e.printStackTrace();
        } finally {
            db.close();
            db = null;
        }
    }

    public Cursor query(String Statment) {
        Cursor cur = null;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            cur = db.rawQuery(Statment, null);
            cur.moveToPosition(-1);
        } catch (Exception e) {
            Log.error("DataBaseHelper - ExecuteCursor", e);
        } finally {

            db.close();
            db = null;
        }

        return cur;
    }

    public static String getDBStr(String str) {

        str = str != null ? str.replaceAll("'", "''") : null;
        return str;

    }

    public void upgrade(int level) {
        switch (level) {
            case 0:
                doUpdate1();
            case 1:
                // doUpdate2();
            case 2:
                // doUpdate3();
            case 3:
                // doUpdate4();
        }
    }

    private void doUpdate1() {

		/*
         * [1] customerid will be same for all order and it refer to the logged in user
		 * [2] is_downloaded: 1 = downloading is completed, 0 = downloading is pending 
		 * [3] do_update: 1 = update is available on the server, 0 = updated copy (default value)
		 * [4] is_downloading : 1 = downloading is running. 0 = downloading is Downloading Completed and caption Change "Read"...
		 */
        this.execute("CREATE TABLE Image (imageId INTEGER PRIMARY KEY , ImageName TEXT, ImagePathFull TEXT, ImagePathThum TEXT, Audio TEXT, is_downloaded INTEGER, is_downloading INTEGER, is_deleted INTEGER DEFAULT 0 )");
        this.execute("CREATE TABLE FileToByte (trackId INTEGER , trackByte BLOB)");
    }

    public SQLiteDatabase getConnection() {
        SQLiteDatabase dbCon = this.getWritableDatabase();

        return dbCon;
    }
}
