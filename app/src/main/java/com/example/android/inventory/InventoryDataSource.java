package com.example.android.inventory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wolfgang on 04.07.16.
 */
public class InventoryDataSource
{
    private SQLiteDatabase mDatabase;
    private InventoryDbHelper mHelper;
    private final String LOG_TAG = "InventoryDataSource";

    private String[] mColumns = {
            InventoryContract.ProductEntry.COLUMN_ID,
            InventoryContract.ProductEntry.COLUMN_NAME,
            InventoryContract.ProductEntry.COLUMN_PRICE,
            InventoryContract.ProductEntry.COLUMN_QUANTITY
    };

    public InventoryDataSource(Context context)
    {
        Log.d(LOG_TAG, "create dbHelper");
        mHelper = new InventoryDbHelper(context);
    }

    public void open()
    {
        Log.d(LOG_TAG, "Get reference to database.");
        mDatabase = mHelper.getWritableDatabase();
        mHelper.onCreate(mDatabase);
        Log.d(LOG_TAG, "Got reference to database. Path: " + mDatabase.getPath());
    }

    public void close()
    {
        mHelper.close();
        Log.d(LOG_TAG, "Database is closed by DBHelper.");
    }

    public Product createProduct(String aProductName, int aPrice, int aQuantity)
    {
        ContentValues values = new ContentValues();
        values.put(InventoryContract.ProductEntry.COLUMN_NAME, aProductName);
        values.put(InventoryContract.ProductEntry.COLUMN_PRICE, aPrice);
        values.put(InventoryContract.ProductEntry.COLUMN_QUANTITY, aQuantity);

        long insertId = mDatabase.insert(InventoryContract.ProductEntry.TABLE_NAME, null, values);

        return getProduct(insertId);
    }


    public Cursor getCursor(long aProductId)
    {
        Cursor cursor = mDatabase.query(
                InventoryContract.ProductEntry.TABLE_NAME,
                mColumns,
                InventoryContract.ProductEntry.COLUMN_ID + "=" + aProductId,
                null, null, null, null);

        cursor.moveToFirst();
        return cursor;
    }


    public Product getProduct(long aProductId)
    {
        return createProductForCursor(getCursor(aProductId));
    }

    public void updateQuantity(Product aProduct, int aQuantity)
    {
        ContentValues cv = new ContentValues();
        cv.put(InventoryContract.ProductEntry.COLUMN_QUANTITY, aQuantity);

        mDatabase.update(
                InventoryContract.ProductEntry.TABLE_NAME,
                cv,
                "? = ?",
                new String[] { InventoryContract.ProductEntry.COLUMN_ID, Long.toString(aProduct.getProductId())});

        aProduct.setQuantity(aQuantity);
    }

    private Product createProductForCursor(Cursor cursor)
    {
        int idIndex = cursor.getColumnIndex(InventoryContract.ProductEntry.COLUMN_ID);
        int idName = cursor.getColumnIndex(InventoryContract.ProductEntry.COLUMN_NAME);
        int idPrice = cursor.getColumnIndex(InventoryContract.ProductEntry.COLUMN_PRICE);
        int idQuantity = cursor.getColumnIndex(InventoryContract.ProductEntry.COLUMN_QUANTITY);

        long id = cursor.getLong(idIndex);
        String name = cursor.getString(idName);
        int price = cursor.getInt(idPrice);
        int quantity = cursor.getInt(idQuantity);

        Product Product = new Product(id, name, price, quantity);

        return Product;
    }

    public void deleteAllProducts()
    {
        final String SQL_DELETE_ENTRIES =
                "DELETE FROM " + InventoryContract.ProductEntry.TABLE_NAME;

        mDatabase.execSQL(SQL_DELETE_ENTRIES);
    }

    public void getAllProducts(ArrayList<Product> aProductList)
    {
        Cursor cursor = mDatabase.rawQuery("select * from " + InventoryContract.ProductEntry.TABLE_NAME, null);

        if (cursor .moveToFirst())
        {
            while (cursor.isAfterLast() == false)
            {
                aProductList.add(createProductForCursor(cursor));
                cursor.moveToNext();
            }
        }
    }
}