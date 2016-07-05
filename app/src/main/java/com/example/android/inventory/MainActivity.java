package com.example.android.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    ArrayList<Product> mProductList;
    ProductListAdapter mProductListAdapter;
    InventoryDataSource mDataSource;
    ListView mProductListView;
    View mNoItemsView;

    private static final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(
            new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent newProductIntent = new Intent(MainActivity.this, NewProductActivity.class);
                    startActivityForResult(newProductIntent, 0);
                }
            });


        mDataSource = new InventoryDataSource(this);

        Log.d(LOG_TAG, "DataSource is opened.");
        mDataSource.open();

        mProductList = new ArrayList<Product>();
        mProductListAdapter = new ProductListAdapter(this, mProductList);
        mProductListView = (ListView) findViewById(R.id.list_view);
        mProductListView.setAdapter(mProductListAdapter);

        mNoItemsView = findViewById(R.id.no_items_view);
        assert mNoItemsView != null;

        updateProductList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0)
        {
            if (resultCode == RESULT_OK)
            {
                String productName = data.getStringExtra("product_name");
                int priceInCents = data.getIntExtra("price", 0);
                int quantity = data.getIntExtra("quantity", 0);

                Product product = mDataSource.createProduct(productName, priceInCents, quantity);
                Util.showToast(this, "Created: " + product);

                updateProductList();
            }
        }
    }

    public void onSellProduct(View aView)
    {
        Product product = mProductList.get((Integer)aView.getTag());
        int quantity = product.getQuantity();

        if (quantity > 0)
        {
            quantity--;
        }
        
        mDataSource.updateQuantity(product, quantity);
        mProductListAdapter.notifyDataSetChanged();
    }

    private void updateProductList()
    {
        mProductList.clear();
        mDataSource.getAllProducts(mProductList);
        mProductListAdapter.notifyDataSetChanged();

        if (mProductList.isEmpty())
        {
            mProductListView.setVisibility(View.GONE);
            mNoItemsView.setVisibility(View.VISIBLE);
        }
        else
        {
            mProductListView.setVisibility(View.VISIBLE);
            mNoItemsView.setVisibility(View.GONE);
        }
    }
}
