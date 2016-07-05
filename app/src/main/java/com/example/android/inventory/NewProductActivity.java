package com.example.android.inventory;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.UUID;


public class NewProductActivity extends AppCompatActivity
{
    private final int RESULT_LOAD_IMAGE = 0;
    private int mQuantity;
    private EditText mQuantityField;
    private EditText mPriceField;
    private EditText mProductNameField;
    private ImageView mImageView;
    private String mImagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        mQuantityField = (EditText) findViewById(R.id.product_quantity_field);

        mPriceField = (EditText) findViewById(R.id.product_price_field);

        mPriceField.setOnEditorActionListener(
            new TextView.OnEditorActionListener()
            {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
                {
                    if (actionId == EditorInfo.IME_ACTION_DONE)
                    {
                        updatePrice();
                    }

                    return false;
                }
            });

        mProductNameField = (EditText) findViewById(R.id.product_name_field);
        mImageView = (ImageView) findViewById(R.id.image_view);
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
            intent.putExtra("image_path", mImagePath);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void onAddImage(View aView)
    {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE)
        {
            if (resultCode == RESULT_OK)
            {
                Uri selectedImage = data.getData();
                // source http://stackoverflow.com/questions/28530332/how-to-get-image-from-gallery
                InputStream inputStream = null;

                if (ContentResolver.SCHEME_CONTENT.equals(selectedImage.getScheme()))
                {
                    try
                    {
                        inputStream = this.getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                        Util.showToast(this, "Could not load image...");
                        return;
                    }
                } else
                {
                    if (ContentResolver.SCHEME_FILE.equals(selectedImage.getScheme()))
                    {
                        try
                        {
                            inputStream = new FileInputStream(selectedImage.getPath());
                        } catch (FileNotFoundException e)
                        {
                            e.printStackTrace();
                            Util.showToast(this, "Could not load image...");
                            return;
                        }
                    }
                }

                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inSampleSize = 4;

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, opts);
                mImageView.setImageBitmap(bitmap);
                mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                try
                {
                    String filePath = writeBitmapToFile(bitmap);
                    mImagePath = filePath;
                }
                catch (Exception e)
                {
                    Util.showToast(this, "Could not save image.");
                }

                //mImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            }
        }
    }

    private String writeBitmapToFile(Bitmap bitmap) throws Exception
    {
        String filename = UUID.randomUUID().toString();
        File directory = this.getDir("images", Context.MODE_PRIVATE);
        File mypath = new File(directory,filename);
        FileOutputStream fos = new FileOutputStream(mypath);

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();


        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);

        return filename;
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

        if (mImagePath.isEmpty())
        {
            Util.showToast(this, R.string.product_image_empty_message);
            return false;
        }

        return true;
    }
}
