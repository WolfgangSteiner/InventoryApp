package com.example.android.inventory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class InventoryDbHelper extends SQLiteOpenHelper
{
    private static final String LOG_TAG = "InventoryDbHelper";
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Inventory.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
        "CREATE TABLE IF NOT EXISTS " + InventoryContract.ProductEntry.TABLE_NAME
            + " ("
            + InventoryContract.ProductEntry.COLUMN_ID + " INTEGER PRIMARY KEY," 
            + InventoryContract.ProductEntry.COLUMN_NAME + TEXT_TYPE + COMMA_SEP 
            + InventoryContract.ProductEntry.COLUMN_PRICE + TEXT_TYPE + COMMA_SEP 
            + InventoryContract.ProductEntry.COLUMN_QUANTITY + INTEGER_TYPE + COMMA_SEP
            + InventoryContract.ProductEntry.COLUMN_IMAGEPATH + TEXT_TYPE
            + " )";

    private Context mContext;

    public InventoryDbHelper(Context aContext)
    {
        super(aContext, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = aContext;
        Log.d(LOG_TAG, "Created database:" + getDatabaseName());
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.i(LOG_TAG, "Creating table!");
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + InventoryContract.ProductEntry.TABLE_NAME);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void deleteDatabase()
    {
        mContext.deleteDatabase(DATABASE_NAME);
    }
}