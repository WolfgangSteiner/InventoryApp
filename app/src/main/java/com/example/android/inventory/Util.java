package com.example.android.inventory;

/**
 * Created by wolfgang on 05.07.16.
 */
public class Util
{
    public static String formatPrice(int aPriceInCents)
    {
        return "$" + (aPriceInCents / 100)
                + "." + (aPriceInCents % 100) / 10 + (aPriceInCents % 10);
    }

    public static int priceFromString(String aPriceString)
    {
        String scratch = new String(aPriceString);
        scratch.replace("$", "");
        scratch.replace(",", "");
        scratch.replace(".", "");

        return Integer.parseInt(scratch) * 100;
    }
}
