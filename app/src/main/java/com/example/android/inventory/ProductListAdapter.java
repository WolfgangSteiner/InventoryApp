package com.example.android.inventory;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class ProductListAdapter extends ArrayAdapter<Product>
{
    private Context mContext;

    public ProductListAdapter(Activity aContext, ArrayList<Product> aProductList)
    {
        super(aContext, 0, aProductList);
        mContext = aContext;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View productItemView = convertView;

        if(productItemView == null)
        {
            productItemView = LayoutInflater.from(getContext()).inflate(
                R.layout.product_list_item, parent, false);
        }

        final Product product = getItem(position);

        TextView nameTextView = (TextView) productItemView.findViewById(R.id.product_name_field);
        nameTextView.setText(product.getName());

        TextView priceTextView = (TextView) productItemView.findViewById(R.id.product_price_field);
        priceTextView.setText(Util.formatPrice(product.getPriceInCents()));

        TextView quantityTextView = (TextView) productItemView.findViewById(R.id.product_quantity_field);
        quantityTextView.setText(Integer.toString(product.getQuantity()));

        productItemView.findViewById(R.id.product_sell_button).setTag(Integer.valueOf(position));

        ImageView imageView = (ImageView) productItemView.findViewById(R.id.product_image_view);

        try
        {
            File directory = mContext.getDir("images", Context.MODE_PRIVATE);
            File mypath = new File(directory, product.getImagePath());
            FileInputStream fis = new FileInputStream(mypath);
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            imageView.setImageBitmap(bitmap);
        }
        catch (Exception e)
        {
            imageView.setImageResource(android.R.drawable.ic_menu_help);
        }

        return productItemView;
    }
}
