package me.ledge.link.sdk.ui.storages;

import android.graphics.Color;

import java.util.concurrent.ExecutionException;

import java8.util.concurrent.CompletableFuture;
import java8.util.concurrent.CompletionException;
import me.ledge.link.api.exceptions.ApiException;
import me.ledge.link.api.vos.requests.base.UnauthorizedRequestVo;
import me.ledge.link.api.vos.responses.config.ConfigResponseVo;
import me.ledge.link.api.vos.responses.config.ContextConfigResponseVo;

import static me.ledge.link.sdk.sdk.LedgeLinkSdk.getApiWrapper;

/**
 * Stores UI related data.
 * @author Adrian
 */
public class UIStorage {

    private Integer mPrimaryColor;
    private Integer mSecondaryColor;
    private ContextConfigResponseVo mConfig;
    private final static String DEFAULT_PRIMARY_COLOR = "9ADE83";
    private final static String DEFAULT_SECONDARY_COLOR = "F2F2F2";

    private static UIStorage mInstance;

    /**
     * Creates a new {@link UIStorage} instance.
     */
    private UIStorage() {
        init();
    }

    /**
     * @return The single instance of this class.
     */
    public static synchronized UIStorage getInstance() {
        if (mInstance == null) {
            mInstance = new UIStorage();
        }

        return mInstance;
    }

    public void init() {
        CompletableFuture
                .supplyAsync(() -> {
                    try {
                        return getApiWrapper().getUserConfig(new UnauthorizedRequestVo());
                    } catch (ApiException e) {
                        return null;
                    }
                })
                .thenAccept(this::setConfig);
    }

    public void setConfig(ContextConfigResponseVo config) {
        mConfig = config;
        setColors();
    }

    public synchronized int getPrimaryColor() {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            if(mPrimaryColor != null) {
                return mPrimaryColor;
            }
            else {
                try {
                    mConfig = getApiWrapper().getUserConfig(new UnauthorizedRequestVo());
                    setColors();
                    return mPrimaryColor;
                } catch (ApiException e) {
                    throw new CompletionException(e);
                }
            }
        });

        return (Integer) getResultFromFuture(future);
    }

    public synchronized int getSecondaryColor() {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            if(mSecondaryColor != null) {
                return mSecondaryColor;
            }
            else {
                try {
                    mConfig = getApiWrapper().getUserConfig(new UnauthorizedRequestVo());
                    setColors();
                    return mSecondaryColor;
                } catch (ApiException e) {
                    throw new CompletionException(e);
                }
            }
        });

        return (Integer) getResultFromFuture(future);
    }

    public synchronized ConfigResponseVo getContextConfig() {
        CompletableFuture<ConfigResponseVo> future = CompletableFuture.supplyAsync(() -> {
            if(mConfig != null) {
                return mConfig.projectConfiguration;
            }
            else {
                try {
                    mConfig = getApiWrapper().getUserConfig(new UnauthorizedRequestVo());
                    return mConfig.projectConfiguration;
                } catch (ApiException e) {
                    throw new CompletionException(e);
                }
            }
        });

        return (ConfigResponseVo) getResultFromFuture(future);
    }

    public synchronized int getPrimaryContrastColor() {
        int red = Color.red(mPrimaryColor);
        int green = Color.green(mPrimaryColor);
        int blue = Color.blue(mPrimaryColor);
        return getBrightness(red, green, blue) > 200 ? Color.BLACK : Color.WHITE;
    }

    private int getBrightness(int red, int green, int blue) {
        // As per http://stackoverflow.com/a/2241471
        return (int)Math.sqrt(red * red * .299 +
                            green * green * .587 +
                            blue * blue * .114);
    }

    private void setColors() {
        String hexColor = mConfig.projectConfiguration.primaryColor;
        if(hexColor == null) {
            hexColor = DEFAULT_PRIMARY_COLOR;
        }
        mPrimaryColor = convertHexToInt(hexColor);
        hexColor = mConfig.projectConfiguration.secondaryColor;
        if(hexColor == null) {
            hexColor = DEFAULT_SECONDARY_COLOR;
        }
        mSecondaryColor = convertHexToInt(hexColor);
    }

    private int convertHexToInt(String hexColor) {
        return Color.parseColor("#"+hexColor);
    }

    private Object getResultFromFuture(CompletableFuture future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            future.completeExceptionally(e);
            throw new CompletionException(e);
        }
    }

}
