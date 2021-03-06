package com.sdyk.android.automator;

import one.rewind.db.DaoManager;
import one.rewind.json.JSON;
import one.rewind.json.JSONable;

import java.util.HashMap;
import java.util.Map;

import static com.sdyk.android.automator.AppInfo.Defaults.*;

public class AppInfo implements JSONable<AppInfo> {

    /**
     * 在本类中定义应用程序及其包名和appActivity，在其他类的initAppiumDriver中可以通过更改应用程序名进行初始化的设置修改
     */

    public static Map<Defaults, AppInfo> apps = new HashMap<>();

    public static enum Defaults {
        WX,
        Contacts,
        IndexPage,//主页面，相当于什么也不做，只是初始化AppiumDriver
        Dingding
    }

    static {
        apps.put(WX, new AppInfo("com.tencent.mm", ".ui.LauncherUI"));
        apps.put(Contacts, new AppInfo("com.google.android.contacts", "com.android.contacts.activities.PeopleActivity"));
        apps.put(IndexPage, new AppInfo("com.google.android.googlequicksearchbox", "com.google.android.launcher.GEL"));
        apps.put(Dingding, new AppInfo("com.alibaba.android.rimet", "com.alibaba.android.rimet.biz.SplashActivity"));
    }

    public static AppInfo get(Defaults name) {
        return apps.get(name);
    }

    public String appPackage;
    public String appActivity;

    public AppInfo(String appPackage, String appActivity) {
        this.appPackage = appPackage;
        this.appActivity = appActivity;
    }

    @Override
    public String toJSON() {
        return JSON.toJson(this);
    }
}
