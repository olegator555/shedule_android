package com.olegator555.rasp.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import static com.olegator555.rasp.DB.TableModel.*;

public class DBProvider extends SQLiteOpenHelper {
    private static final String CREATE_DATABASE = "CREATE TABLE " + TABLE_NAME + " ("
            + COUNTRY + " TEXT," + REGION
            + " TEXT," + SETTLEMENT + " TEXT," + DIRECTION +
            " TEXT," + STATION_NAME + " TEXT," + YANDEX_CODE + " TEXT PRIMARY KEY)";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DBProvider(@Nullable Context context) {
        super(context, DB_NAME, null, TableModel.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(DROP_TABLE);
        this.onCreate(sqLiteDatabase);
    }
}

