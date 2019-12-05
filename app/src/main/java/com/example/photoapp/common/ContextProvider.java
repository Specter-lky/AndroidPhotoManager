package com.example.photoapp.common;

import android.content.Context;

/**
 * Pure static provider class to provide the active activity context to ViewModels/Fragments.
 *
 * @author Ryan Brandt
 */
public class ContextProvider {
    private static Context activeContext;

    private ContextProvider() {}

    public static void setContext(Context context) {
        activeContext = context;
    }

    public static Context getContext() {
        return activeContext;
    }
}
