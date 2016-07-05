package com.example.android.inventory;

import android.provider.BaseColumns;

/**
 * Created by wolfgang on 04.07.16.
 */
public class InventoryContract
{
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public InventoryContract() {}

    /* Inner class that defines the table contents */
    public static abstract class ProductEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "product";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "title";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_IMAGEPATH = "image_path";
    }
}
