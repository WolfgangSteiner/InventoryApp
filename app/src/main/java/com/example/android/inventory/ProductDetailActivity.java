package com.example.android.inventory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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


    public void onRemoveProduct(View aView)
    {
        new AlertDialog.Builder(this)
            .setMessage(getString(R.string.dialog_remove_entry_text))
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                  onDoRemoveProduct();
                }
            })
            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // do nothing
                }
            })
            .show();
    }

    public void onOrderProduct(View aView)
    {
        String subject = "Product order";
        String body =
            "Hi, \n\n"
            + "I would like to order 10 more boxes of "
            + mProduct.getName() + ".\n\n"
            + "Kind Regards";

        composeEmail(subject, body);
    }

    public void onDoRemoveProduct()
    {
        mDataSource.deleteProduct(mProduct);
        mProductList.remove(mProduct);
        mProduct = null;
        finish();
    }

    private void updateQuantity()
    {
        mQuantityField.setText(Integer.toString(mQuantity));
        mDataSource.updateQuantity(mProduct, mQuantity);
    }

    public void composeEmail(String subject, String text)
    {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);

        if (intent.resolveActivity(getPackageManager()) != null)
        {
            startActivity(intent);
        }
    }

}
