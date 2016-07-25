package com.snicesoft.framework;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

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

    public static Activity getCurrent() {
        if (activities.size() > 0)
            return activities.get(activities.size() - 1);
        return null;
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
