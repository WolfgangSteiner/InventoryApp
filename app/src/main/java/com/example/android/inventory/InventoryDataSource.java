package com.example.android.inventory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wolfgang on 04.07.16.
 */
public class InventoryDataSource
{
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private InventoryDbHelper mHelper;
    private final String LOG_TAG = "InventoryDataSource";

    private String[] mColumns = {
            InventoryContract.ProductEntry.COLUMN_ID,
            InventoryContract.ProductEntry.COLUMN_NAME,
            InventoryContract.ProductEntry.COLUMN_PRICE,
            InventoryContract.ProductEntry.COLUMN_QUANTITY,
            InventoryContract.ProductEntry.COLUMN_IMAGEPATH
    };

    public InventoryDataSource(Context context)
    {
        mContext = context;
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

    public Product createProduct(String aProductName, int aPrice, int aQuantity, String aImagePath)
    {
        ContentValues values = new ContentValues();
        values.put(InventoryContract.ProductEntry.COLUMN_NAME, aProductName);
        values.put(InventoryContract.ProductEntry.COLUMN_PRICE, aPrice);
        values.put(InventoryContract.ProductEntry.COLUMN_QUANTITY, aQuantity);
        values.put(InventoryContract.ProductEntry.COLUMN_IMAGEPATH, aImagePath);

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
        Log.d(LOG_TAG, "udating quantity to " + aQuantity);

        ContentValues cv = new ContentValues();
        cv.put(InventoryContract.ProductEntry.COLUMN_QUANTITY, aQuantity);

        int result = mDatabase.update(
            InventoryContract.ProductEntry.TABLE_NAME,
            cv,
            InventoryContract.ProductEntry.COLUMN_ID + " = " + Long.toString(aProduct.getProductId()),
            null);

        aProduct.setQuantity(aQuantity);
    }

    public void updatePrice(Product aProduct, int aPrice)
    {
        Log.d(LOG_TAG, "udating price to " + aPrice);

        ContentValues cv = new ContentValues();
        cv.put(InventoryContract.ProductEntry.COLUMN_PRICE, aPrice);

        int result = mDatabase.update(
            InventoryContract.ProductEntry.TABLE_NAME,
            cv,
            InventoryContract.ProductEntry.COLUMN_ID + " = " + Long.toString(aProduct.getProductId()),
            null);

        aProduct.setPriceInCents(aPrice);
    }

    private int getPrice(long aProductId)
    {
        Cursor cursor = getCursor(aProductId);
        cursor.moveToFirst();
        int idPrice = cursor.getColumnIndex(InventoryContract.ProductEntry.COLUMN_PRICE);
        return cursor.getInt(idPrice);
    }

    private int getQuantity(long aProductId)
    {
        Cursor cursor = getCursor(aProductId);
        Log.d(LOG_TAG, "get quantity of product: " + createProductForCursor(cursor));
        cursor.moveToFirst();
        int idQuantity = cursor.getColumnIndex(InventoryContract.ProductEntry.COLUMN_QUANTITY);
        return cursor.getInt(idQuantity);
    }

    private Product createProductForCursor(Cursor cursor)
    {
        int idIndex = cursor.getColumnIndex(InventoryContract.ProductEntry.COLUMN_ID);
        int idName = cursor.getColumnIndex(InventoryContract.ProductEntry.COLUMN_NAME);
        int idPrice = cursor.getColumnIndex(InventoryContract.ProductEntry.COLUMN_PRICE);
        int idQuantity = cursor.getColumnIndex(InventoryContract.ProductEntry.COLUMN_QUANTITY);
        int idImagePath = cursor.getColumnIndex(InventoryContract.ProductEntry.COLUMN_IMAGEPATH);

        long id = cursor.getLong(idIndex);
        String name = cursor.getString(idName);
        int price = cursor.getInt(idPrice);
        int quantity = cursor.getInt(idQuantity);
        String imagePath = cursor.getString(idImagePath);

        Product product = new Product(id, name, price, quantity, imagePath);

        Log.d(LOG_TAG, "Instantiated product: "  + product);

        return product;
    }

    public void deleteAllProducts()
    {
        final String SQL_DELETE_ENTRIES =
                "DELETE FROM " + InventoryContract.ProductEntry.TABLE_NAME;

        mDatabase.execSQL(SQL_DELETE_ENTRIES);
    }

    public void deleteProduct(Product aProduct)
    {
        String SQL_DELETE_ENTRY =
            "DELETE FROM " + InventoryContract.ProductEntry.TABLE_NAME
            + " WHERE " + InventoryContract.ProductEntry.COLUMN_ID
            + " = " + Long.toString(aProduct.getProductId());

        File directory = mContext.getDir("images", Context.MODE_PRIVATE);
        File mypath = new File(directory, aProduct.getImagePath());
        mypath.delete();

        mDatabase.execSQL(SQL_DELETE_ENTRY);

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