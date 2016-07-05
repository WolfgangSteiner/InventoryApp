package com.example.android.inventory;

import android.app.Application;

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
    }

    @Override
    public void onTerminate()
    {
        mDataSource.close();

        super.onTerminate();
    }
}
