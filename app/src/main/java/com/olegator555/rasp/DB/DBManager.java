package com.olegator555.rasp.DB;

import Model.ServerAnswerModel;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.olegator555.rasp.DB.TableModel.*;

public class DBManager {
    private final DBProvider dbProvider;
    private SQLiteDatabase database;
    private final Context context;
    public ArrayList<ServerAnswerModel> model_list = new ArrayList<>();
    public static final String SUCCESSFULLY_INSERTED = "SuccessfullyInserted";

    public DBManager(Context context) {
        dbProvider = new DBProvider(context);
        this.context = context;
    }
    public void openDB() {
        database = dbProvider.getWritableDatabase();
    }
    public void closeDB() {
        database.close();
    }
    public void insertToDb(List<ServerAnswerModel> modelList) {
        new AsyncDBProvider().execute(new DBTask<Void>() {
            @Override
            public Void doWithDb() {
                insert(modelList);
                return null;
            }
        });
    }
    public ArrayList<ServerAnswerModel> getFromDb() {
        AsyncDBProvider asyncDBProvider = new AsyncDBProvider();
        try {
            model_list = (ArrayList<ServerAnswerModel>) asyncDBProvider.execute((DBTask<ArrayList<ServerAnswerModel>>)
                    () -> getStationsList()).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("Size", String.valueOf(model_list.size()));
        return model_list;
    }
    private void insert(List<ServerAnswerModel> model_list) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SUCCESSFULLY_INSERTED,
                Context.MODE_PRIVATE);
        if(!sharedPreferences.contains(SUCCESSFULLY_INSERTED)) {
            model_list.forEach(model -> {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COUNTRY, model.getCountry());
                contentValues.put(REGION, model.getRegion());
                contentValues.put(SETTLEMENT, model.getSettlement());
                contentValues.put(DIRECTION, model.getDirection());
                contentValues.put(STATION_NAME, model.getStation_name());
                contentValues.put(YANDEX_CODE, Integer.parseInt(model.getYandex_code().substring(1)));
                database.insert(TABLE_NAME, null, contentValues);
            });
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(SUCCESSFULLY_INSERTED, "");
            editor.apply();
        }
    }
    public ArrayList<ServerAnswerModel> getStationsList() {
        Cursor cursor = database.rawQuery("SELECT * FROM "+ TABLE_NAME,null);
        while (cursor.moveToNext()) {
            ServerAnswerModel model = new ServerAnswerModel();
            model.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
            model.setRegion(cursor.getString(cursor.getColumnIndex(REGION)));
            model.setSettlement(cursor.getString(cursor.getColumnIndex(SETTLEMENT)));
            model.setDirection(cursor.getString(cursor.getColumnIndex(DIRECTION)));
            model.setStation_name(cursor.getString(cursor.getColumnIndex(STATION_NAME)));
            model.setYandex_code(cursor.getString(cursor.getColumnIndex(YANDEX_CODE)));
            model_list.add(model);
        }
        cursor.close();
        return model_list;
    }

    interface DBTask <T> {
        T doWithDb();
    }
    @SuppressLint("StaticFieldLeak")
    class AsyncDBProvider extends AsyncTask<DBTask, Void, Object> {
        private ArrayList<ServerAnswerModel> model_list = new ArrayList<>();

        private void setModel_list(ArrayList<ServerAnswerModel> model_list) {
            this.model_list.addAll(model_list);
        }

        @Override
        protected Object doInBackground(DBTask... dbTasks) {
            return dbTasks[0].doWithDb();
        }


        @Override
        protected void onPostExecute(Object getter) {
            super.onPostExecute(getter);
            closeDB();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Log.d("Tag", "in progress");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            openDB();
        }
    }
}
