package me.ledge.link.sdk.sdk;

import android.content.Context;
import android.content.Intent;
import me.ledge.link.sdk.sdk.vos.UserDataVo;

import java.util.ArrayList;

/**
 * Ledge Line UI helper is an extension of {@link LedgeLinkSdk} to help set up the Ledge Line UI.<br />
 * <br />
 * Make sure to call {@link #setProcessOrder} before calling {@link #startProcess}!
 * @author Wijnand
 */
public class LedgeLinkUi extends LedgeLinkSdk {

    private static ArrayList<Class<?>> mProcessOrder;

    /**
     * @return Order in which the Ledge Line Activities should be shown.
     */
    public static ArrayList<Class<?>> getProcessOrder() {
        return mProcessOrder;
    }

    /**
     * Stores a new order in which the Ledge Line Activities should be shown.
     * @param processOrder List of Activity classes.
     */
    public static void setProcessOrder(ArrayList<Class<?>> processOrder) {
        mProcessOrder = processOrder;
    }

    /**
     * Starts the Ledge Line loan offers process.
     * @param context The {@link Context} to launch the first Ledge Line screen from.
     * @param data Pre-fill user data. Use {@code null} if not needed.
     */
    public static void startProcess(Context context, UserDataVo data) {
        if (getProcessOrder() == null) {
            throw new NullPointerException("Make sure to call 'setProcessOrder(ArrayList<Class<?>>)' before trying " +
                    "to start the loan offers process!");
        }

        Intent startIntent = new Intent(context, getProcessOrder().get(0));

        if (data != null) {
            startIntent.putExtra(UserDataVo.USER_DATA_KEY, data);
        }

        context.startActivity(startIntent);
    }
}
