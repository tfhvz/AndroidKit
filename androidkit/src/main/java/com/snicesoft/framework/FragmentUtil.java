package com.snicesoft.framework;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

class FragmentUtil {
    public static void replaceFragment(int id, Fragment fragment,
                                       FragmentManager fragmentManager, boolean backStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(id, fragment);
        if (backStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }
}
