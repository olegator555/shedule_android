package com.olegator555.rasp.DB;

import Utils.AppLevelUtilsAndConstants;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import androidx.annotation.Nullable;
import com.olegator555.rasp.DB.model.DBArgs;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class AsyncDBWriter extends Thread {
    private DBProvider dbProvider;
    private final Activity activity;
    private String tableName;
    private ArrayList<DBArgs> args;
    private Class<?> clazz;
    private ArrayList<Object> objects;
    public static final String SUCCESSFULLY_INSERTED = "SuccessfullyInserted";
    private final String action;
    private final String[] whereArgs;
    private SQLiteDatabase sqLiteDatabase;

    public AsyncDBWriter(Activity activity, String action, Class<?> clazz) {
        this.activity = activity;
        this.action = action;
        this.clazz = clazz;
        this.whereArgs = null;
    }
    public AsyncDBWriter(Activity activity, String action, Class<?> clazz, ArrayList<Object> objects) {
        this.activity = activity;
        this.action = action;
        this.clazz = clazz;
        this.objects = objects;
        this.whereArgs = null;
    }
    public AsyncDBWriter(Activity activity, String action, Class<?> clazz, ArrayList<Object> objects,
                         String[] whereArgs) {
        this.activity = activity;
        this.action = action;
        this.clazz = clazz;
        this.objects = objects;
        this.whereArgs = whereArgs;
    }

    public void onThreadCompleted(ArrayList<Object> objects) {}
    public static final String INSERT = "insert";
    public static final String READ = "read";
    @Override
    public void run() {
        createTableFromModelClass();
        dbProvider = new DBProvider(activity.getApplicationContext(),args, tableName);
        openDB();
        switch (action) {
            case READ:
                objects = readFromDb(whereArgs);
                activity.runOnUiThread(() -> onThreadCompleted(objects));
                break;
            case INSERT:
                insert(objects);
                activity.runOnUiThread(this::onThreadCompleted);
        }
        closeDB();

    }
    public void openDB() {
        sqLiteDatabase = dbProvider.getWritableDatabase();
    }
    public void closeDB() {
        sqLiteDatabase.close();
    }
    public void onThreadCompleted() {}
    public void createTableFromModelClass() {
        args = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            DatabaseTransferable databaseTransferable = field.getAnnotation(DatabaseTransferable.class);
            if (databaseTransferable!=null) {
                args.add(new DBArgs(field.getName().toUpperCase(), databaseTransferable.sqlType(),
                        databaseTransferable.primaryKey()));
            }
        }
        String[] temp = clazz.getName().split("\\.");
        tableName = temp[temp.length-1].toUpperCase();
        Log.d("Table name: " , tableName);
    }
    public ArrayList<Object> readFromDb(@Nullable String[] where) {
        Cursor cursor;
        if (where==null)
            cursor = dbProvider.getReadableDatabase().rawQuery("SELECT * FROM "+ tableName,null);
        else
            cursor = dbProvider.getReadableDatabase().rawQuery("SELECT * FROM "+ tableName, where);
        ArrayList<Object> model_list = new ArrayList<>();
        Object object = null;
        while (cursor.moveToNext()) {
            for (DBArgs element : args) {
                String value = cursor.getString(cursor.getColumnIndex(element.getName().toUpperCase()));
                String setter = "set" + element.getName().charAt(0) + element.getName()
                        .substring(1, element.getName().length()-1).toLowerCase();
                try {
                    object = clazz.getConstructor();
                    clazz.getMethod(setter, String.class).invoke(object, value);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    Log.e("NoSuchMethodException thrown", "Setter for field " + element.getName()
                            + " not found");
                } catch (InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            model_list.add(object);

        }
        cursor.close();
        return model_list;
    }
    private void insert(ArrayList<Object> model_list) {
        if (AppLevelUtilsAndConstants.readFromPreferences(activity.getApplicationContext(), SUCCESSFULLY_INSERTED, String.class)!=null) {
            model_list.forEach(model -> {
                ContentValues contentValues = new ContentValues();
                /*contentValues.put(COUNTRY, model.getCountry());
                contentValues.put(REGION, model.getRegion());
                contentValues.put(SETTLEMENT, model.getSettlement());
                contentValues.put(DIRECTION, model.getDirection());
                contentValues.put(STATION_NAME, model.getStation_name());
                contentValues.put(YANDEX_CODE, Integer.parseInt(model.getYandex_code().substring(1)));
                database.insert(TABLE_NAME, null, contentValues);*/
                for (DBArgs element : args) {
                    String setter = "get" + element.getName().charAt(0) + element.getName()
                            .substring(1, element.getName().length()-1).toLowerCase();
                    try {
                        String output = (String) clazz.getMethod(setter, String.class).invoke(model);
                        contentValues.put(element.getName(), output);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                        Log.e("NoSuchMethodException thrown", "Getter for field " + element.getName()
                                + " not found");
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                dbProvider.getWritableDatabase().insert(tableName,null, contentValues);
                Log.d("SQL", "INserted values!");
            });

            AppLevelUtilsAndConstants.writeToPreferences(activity.getApplicationContext(), SUCCESSFULLY_INSERTED, "", String.class);
        }
    }
}
