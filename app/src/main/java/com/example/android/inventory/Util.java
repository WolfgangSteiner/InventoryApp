package com.example.android.inventory;

import android.content.Context;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by wolfgang on 05.07.16.
 */
public class Util
{
    public static String formatPrice(int aPriceInCents)
    {
        return NumberFormat.getNumberInstance(Locale.US).format(aPriceInCents / 100.0);
    }

    public static int priceFromString(String aPriceString)
    {
        try
        {
            double d = NumberFormat.getNumberInstance(Locale.US).parse(aPriceString).doubleValue();
            return (int)(d * 100);
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    public static void showToast(Context aContext, String aString)
    {
        Toast.makeText(aContext, aString, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context aContext, int aStringId)
    {
        showToast(aContext, aContext.getString(aStringId));
    }
}
