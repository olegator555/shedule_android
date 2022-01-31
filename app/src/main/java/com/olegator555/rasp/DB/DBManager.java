package com.olegator555.rasp.DB;

import Model.ServerAnswerModel;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import static com.olegator555.rasp.DB.TableModel.*;

public class DBManager {
    private final DBProvider dbProvider;
    private SQLiteDatabase database;

    public DBManager(Context context) {
        dbProvider = new DBProvider(context);
    }
    public void openDB() {
        database = dbProvider.getWritableDatabase();
    }
    public void closeDB() {
        database.close();
    }
    public void insert(ServerAnswerModel model) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COUNTRY, model.getCountry());
        contentValues.put(REGION, model.getRegion());
        contentValues.put(SETTLEMENT, model.getSettlement());
        contentValues.put(DIRECTION, model.getDirection());
        contentValues.put(STATION_NAME, model.getStation_name());
        contentValues.put(YANDEX_CODE, model.getYandex_code().substring(1));
        database.insert(TABLE_NAME, null, contentValues);
    }
    public ArrayList<String> getTitleList(@Nullable String country){
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor;
        if(country!=null)
            cursor = database.rawQuery("SELECT " + COUNTRY + " FROM " + TABLE_NAME, null);
        else
            cursor = database.rawQuery("SELECT " + REGION + " FROM " + TABLE_NAME + " WHERE "
                    + COUNTRY + " = " + country, null);
        while (cursor.moveToNext())
        {
            String element;
            if(country!=null)
                element = cursor.getString(cursor.getColumnIndex(COUNTRY));
            else
                element = cursor.getString(cursor.getColumnIndex(REGION));
            list.add(element);
        }
        cursor.close();
        return list;
    }

    public ArrayList<ServerAnswerModel> getStationsList(String[] selection_args){
        ArrayList<ServerAnswerModel> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM "+ TABLE_NAME + " WHERE " + COUNTRY + " = ? AND "
                + REGION + " = ?", selection_args);
        while (cursor.moveToNext()) {
            ServerAnswerModel model = new ServerAnswerModel();
            model.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
            model.setRegion(cursor.getString(cursor.getColumnIndex(REGION)));
            model.setSettlement(cursor.getString(cursor.getColumnIndex(SETTLEMENT)));
            model.setDirection(cursor.getString(cursor.getColumnIndex(DIRECTION)));
            model.setStation_name(cursor.getString(cursor.getColumnIndex(STATION_NAME)));
            model.setYandex_code(cursor.getString(cursor.getColumnIndex(YANDEX_CODE)));
            list.add(model);
        }
        cursor.close();
        return list;
    }
}
