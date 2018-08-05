package com.sdyk.automation;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class AndroidDeviceManager {

	private static AndroidDeviceManager instance;

	public static int DefaultAppiumPort = 47100;

	public static void getInstance() {
		synchronized (AndroidDeviceManager.class) {
			if(instance == null) {
				instance = new AndroidDeviceManager();
			}
		}
	}

	public Map<String, AndroidDevice> devices = new HashMap<>();

	/**
	 *
	 */
	private AndroidDeviceManager() {

	}

	/**
	 *
	 * @param udid
	 * @throws Exception
	 */
	public synchronized void initDevice(String udid) throws Exception {
		int port = DefaultAppiumPort + devices.keySet().size();
		devices.put(udid, new AndroidDevice(udid, port));
	}

	/**
	 *
	 * @param udid
	 * @return
	 */
	public AndroidDevice getDevice(String udid) {
		return devices.get(udid);
	}

	/**
	 *
	 * @param udid
	 */
	public void stopDevice(String udid) {

	}
}
