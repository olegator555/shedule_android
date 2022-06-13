package com.olegator555.rasp.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;
import com.olegator555.rasp.DB.model.DBArgs;

import java.util.ArrayList;

import static com.olegator555.rasp.DB.TableModel.DB_NAME;
import static com.olegator555.rasp.DB.TableModel.TABLE_NAME;

public class DBProvider extends SQLiteOpenHelper {
    private ArrayList<DBArgs> dbArgsArrayList;
    private final String tableName;

    public DBProvider(@Nullable Context context, ArrayList<DBArgs> dbArgsArrayList, String tableName) {
        super(context, DB_NAME, null, TableModel.VERSION);
        this.dbArgsArrayList = dbArgsArrayList;
        this.tableName = tableName;
    }

    /*private final String CREATE_DATABASE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("" TEXT," + region
            + " TEXT," + settlement + " TEXT," + direction +
            " TEXT," + stationName + " TEXT," + yndexCode + " INTEGER PRIMARY KEY)";*/
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private String creationSql() {
        StringBuilder stringBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (");
        dbArgsArrayList.forEach(element -> {
            stringBuilder.append(element.getName()).append(" ").append(element.getSqlType());
            if (element.getPrimaryKey())
                stringBuilder.append(" PRIMARY KEY,");
            stringBuilder.append(", ");
        });
        Log.d("Debug sql", tableName);
        return stringBuilder.substring(0, stringBuilder.length()-3) + ")";
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(creationSql());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(DROP_TABLE);
        this.onCreate(sqLiteDatabase);
    }
}

