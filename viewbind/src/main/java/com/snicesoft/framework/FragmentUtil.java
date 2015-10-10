package com.snicesoft.framework;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

public class FragmentUtil {
	public static void openFragment(int id, Fragment fragment,
			FragmentManager fragmentManager) {
		try {
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			List<Fragment> list = fragmentManager.getFragments();
			if (list == null || list.size() == 0) {
				transaction.add(id, fragment).commit();
			} else {
				if (list.contains(fragment)) {
					for (Fragment f : list) {
						if (fragment == f) {
							transaction.show(f);
						} else {
							transaction.hide(f);
						}
					}
					transaction.commit();
				} else {
					for (Fragment f : list) {
						transaction.hide(f);
					}
					transaction.add(id, fragment).commit();
				}
			}
		} catch (Exception e) {
		}
	}

	public static void replaceFragment(int id, Fragment fragment,
			FragmentManager fragmentManager, boolean backStack) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(id, fragment);
		if (backStack)
			transaction.addToBackStack(null);
		transaction.commit();
	}
}
