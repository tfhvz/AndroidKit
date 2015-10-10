package com.snicesoft.framework;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

public class ActivityMgr {
	private ActivityMgr() {
	}

	static {
		activities = new ArrayList<Activity>();
	}

	private static List<Activity> activities;

	public static void addActivity(Activity activity) {
		activities.add(activity);
	}

	public static void removeActivity(Activity activity) {
		if (activities.contains(activity)) {
			activities.remove(activity);
		}
	}

	public static void finishActivity(Class<?> activity) {
		for (Activity act : activities) {
			if (act.getClass() == activity) {
				act.finish();
				break;
			}
		}
	}

	public static void exitApp() {
		for (Activity activity : activities) {
			activity.finish();
		}
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}
}
