package com.sdyk.android.automator.test;


import com.sdyk.android.automator.AppInfo;
import com.sdyk.automation.WechatSimulator;

public class WechatSimulatorTest {
	public static void main(String arg[]) throws Exception {
		AppInfo info = AppInfo.get(AppInfo.Defaults.WX);


		WechatSimulator wechat1 = new WechatSimulator();
		wechat1.initAppiumDriver();


	}
}
