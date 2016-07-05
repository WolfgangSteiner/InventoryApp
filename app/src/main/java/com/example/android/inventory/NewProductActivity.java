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

public class NewProductActivity extends AppCompatActivity
{
    private int mQuantity;
    private EditText mQuantityField;
    private EditText mPriceField;
    private EditText mProductNameField;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        mQuantityField = (EditText) findViewById(R.id.product_quantity_field);

        mPriceField = (EditText) findViewById(R.id.product_price_field);

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

        mProductNameField = (EditText) findViewById(R.id.product_name_field);

        mQuantity = 1;
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

    public void onAddNewProduct(View aView)
    {
        if (validateValues())
        {
            Intent intent = new Intent();
            intent.putExtra("product_name", getProductName());
            intent.putExtra("price", getPrice());
            intent.putExtra("quantity", mQuantity);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void updateQuantity()
    {
        mQuantityField.setText(Integer.toString(mQuantity));
    }


    private void setPrice(int aPriceInCents)
    {
        String priceString = Util.formatPrice(aPriceInCents);
        mPriceField.setText(priceString);
    }

    private int getPrice()
    {
        String mPriceString = mPriceField.getText().toString();
        return Util.priceFromString(mPriceString);
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

    private String getProductName()
    {
        return mProductNameField.getText().toString().trim();
    }

    private boolean validateValues()
    {
        if (getProductName().isEmpty())
        {
            Util.showToast(this, R.string.product_name_empty_message);
            return false;
        }

        return true;
    }
}
