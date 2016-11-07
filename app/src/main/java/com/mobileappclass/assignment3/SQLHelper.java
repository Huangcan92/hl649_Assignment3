package com.mobileappclass.assignment3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by 0 on 2016/11/4.
 */
public class SQLHelper extends SQLiteOpenHelper {
    private Context context;

    public SQLHelper(Context context) {
        this(context, "DB", null, 1);
        this.context = context;
    }

    public SQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists " +
                "tab ("
                + "id integer primary key autoincrement,name TEXT,"
                + "mtime TEXT,my TEXT,mx TEXT,netid TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void add(Info info) {
        ContentValues values = new ContentValues();
        values.put("mtime", info.getTime());
        values.put("my", info.getY());
        values.put("mx", info.getX());
        values.put("netid", info.getNetid());
        getWritableDatabase().insert("tab", null, values);
    }

    public List<Info> getInfos() {
        List<Info> items = new ArrayList<>();
        Cursor cursor = getWritableDatabase().rawQuery("SELECT * FROM TAB", new String[]{});
        while (cursor.moveToNext()) {
            String mtime = cursor.getString(cursor.getColumnIndex("mtime"));
            String my = cursor.getString(cursor.getColumnIndex("my"));
            String mx = cursor.getString(cursor.getColumnIndex("mx"));
            String netid = cursor.getString(cursor.getColumnIndex("netid"));

            items.add(new Info(mtime, my, mx, netid));
        }
        Collections.reverse(items);
        return items;
    }
}
