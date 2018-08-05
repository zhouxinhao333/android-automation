package com.sdyk.automation;

public class GenyMotionManager {

	public GenyMotionManager(){
	}

	/**
	 * 启动genymotion.exe
	 */
	public static void startGenyMotion(){

		Util.exeCall("C:\\Program Files\\Genymobile\\Genymotion\\genymotion.exe");
	}


}
