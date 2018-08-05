package com.sdyk.android.automator.test;

import com.sdyk.android.automator.AppInfo;
import com.sdyk.automation.AndroidDevice;

public class AndroidDeviceTest {
	public static void main(String arg[]) throws Exception {
		AppInfo info = AppInfo.get(AppInfo.Defaults.Contacts);//以通讯录为测试用例

		String apkPath = "com.facebook.katana_180.0.0.35.82_free-www.apkhere.com.apk";
		String name = "name";
		String number = "123456";
		String filePath = "newFriend.txt";

		AndroidDevice nexus6 = new AndroidDevice("ZX1G323GNB", 47132);
		nexus6.initAppiumDriver();

		nexus6.getHeight();
		nexus6.getWidth();
		nexus6.installApk(apkPath);
		nexus6.listApps();
		nexus6.uninstallApp(info.appPackage);
		nexus6.startActivity(info.appPackage,info.appActivity);
		nexus6.clearContacts();
		nexus6.addContact(name,number);
		nexus6.addContactsFromFile(filePath);
		nexus6.deleteOneContact(name);
	}
}

