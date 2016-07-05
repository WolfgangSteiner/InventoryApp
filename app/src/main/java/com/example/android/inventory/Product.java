package com.example.android.inventory;

/**
 * Created by wolfgang on 04.07.16.
 */
public class Product
{
    private long mProductId;
    private String mName;
    private int mPriceInCents;
    private int mQuantity;

    private String mImagePath;

    public Product(long aProductId, String aName, int aPriceInCents, int aQuantity, String aImagePath)
    {
        this.mName = aName;
        this.mPriceInCents = aPriceInCents;
        this.mQuantity = aQuantity;
        this.mProductId = aProductId;
        this.mImagePath = aImagePath;
    }

    @Override
    public String toString()
    {
        return "Product{" +
            "mName='" + mName + '\'' +
            ", mPriceInCents='" + mPriceInCents + '\'' +
            ", mQuantity=" + mQuantity +
            ", mProductId=" + mProductId +
            ", mImagePath=" + mImagePath +
            '}';
    }

    public String getName()
    {
        return mName;
    }

    public void setName(String mName)
    {
        this.mName = mName;
    }

    public int getPriceInCents()
    {
        return mPriceInCents;
    }

    public void setPriceInCents(int mPriceInCents)
    {
        this.mPriceInCents = mPriceInCents;
    }

    public int getQuantity()
    {
        return mQuantity;
    }

    public void setQuantity(int mQuantity)
    {
        this.mQuantity = mQuantity;
    }

    public long getProductId()
    {
        return mProductId;
    }

    public void setProductId(long mProductId)
    {
        this.mProductId = mProductId;
    }

    public String getImagePath()
    {
        return mImagePath;
    }
}
