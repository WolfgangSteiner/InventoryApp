package com.example.android.inventory;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by wolfgang on 05.07.16.
 */
public class InventoryApplication extends Application
{
    public ArrayList<Product> mProductList;
    public InventoryDataSource mDataSource;

    @Override
    public void onCreate()
    {
        super.onCreate();

        mProductList = new ArrayList<Product>();
        mDataSource = new InventoryDataSource(this);
        mDataSource.open();
        mDataSource.getAllProducts(mProductList);

        File directory = this.getDir("images", Context.MODE_PRIVATE);
        Log.d("directory", directory.getAbsolutePath().toString());
    }

    @Override
    public void onTerminate()
    {
        mDataSource.close();

        super.onTerminate();
    }
}
