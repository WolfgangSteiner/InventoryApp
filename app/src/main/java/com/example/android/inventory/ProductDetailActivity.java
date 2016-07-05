package com.example.android.inventory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ProductDetailActivity extends AppCompatActivity
{
    private EditText mQuantityField;
    private EditText mPriceField;
    private TextView mProductNameField;
    private int mQuantity;

    private ArrayList<Product> mProductList;
    private InventoryDataSource mDataSource;
    private InventoryApplication mInventoryApplication;

    private Product mProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mInventoryApplication = (InventoryApplication) getApplication();
        mProductList = mInventoryApplication.mProductList;
        mDataSource = mInventoryApplication.mDataSource;

        setContentView(R.layout.activity_product_detail);

        Intent intent = getIntent();
        int productIndex = intent.getIntExtra("product_index", 0);

        mProduct = mProductList.get(productIndex);

        mQuantity = mProduct.getQuantity();
        mQuantityField = (EditText) findViewById(R.id.product_quantity_field);
        mQuantityField.setText("" + mQuantity);

        mPriceField = (EditText) findViewById(R.id.product_price_field);
        mPriceField.setText(Util.formatPrice(mProduct.getPriceInCents()));


        mPriceField.setOnEditorActionListener(
            new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
                {
                    if(actionId == EditorInfo.IME_ACTION_DONE)
                    {
                        updatePrice();
                    }

                    return false;
                }
            });

        mProductNameField = (TextView) findViewById(R.id.product_name_field);
        mProductNameField.setText("" + mProduct.getName());
    }

    private void updatePrice()
    {
        try
        {
            int price = getPrice();
            setPrice(price);
        }
        catch (Exception e)
        {
            Toast.makeText(this, R.string.invalid_price_message, Toast.LENGTH_SHORT);
        }
    }

    private void setPrice(int aPriceInCents)
    {
        String priceString = Util.formatPrice(aPriceInCents);
        mPriceField.setText(priceString);
        mDataSource.updatePrice(mProduct, aPriceInCents);
    }

    private int getPrice()
    {
        String mPriceString = mPriceField.getText().toString();
        return Util.priceFromString(mPriceString);
    }

    public void onIncreaseQuantity(View aView)
    {
        mQuantity++;
        updateQuantity();
    }

    public void onDecreaseQuantity(View aView)
    {
        if (mQuantity > 1)
        {
            mQuantity--;
            updateQuantity();
        }
    }

    private void updateQuantity()
    {
        mQuantityField.setText(Integer.toString(mQuantity));
        mDataSource.updateQuantity(mProduct, mQuantity);
    }

}
