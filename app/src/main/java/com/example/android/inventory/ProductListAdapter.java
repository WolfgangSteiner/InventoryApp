package com.example.android.inventory;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ProductListAdapter extends ArrayAdapter<Product>
{
    public ProductListAdapter(Activity aContext, ArrayList<Product> aProductList)
    {
        super(aContext, 0, aProductList);
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

        return productItemView;
    }
}
