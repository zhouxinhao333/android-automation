package com.sdyk.automation;

import com.sdyk.android.automator.AppInfo;
import com.typesafe.config.Config;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.appium.java_client.touch.offset.PointOption;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.filters.RequestFilter;
import net.lightbody.bmp.filters.RequestFilterAdapter;
import net.lightbody.bmp.mitm.CertificateAndKeySource;
import net.lightbody.bmp.mitm.PemFileCertificateSource;
import net.lightbody.bmp.mitm.manager.ImpersonatingMitmManager;
import one.rewind.io.requester.BasicRequester;
import one.rewind.util.Configs;
import one.rewind.util.FileUtil;
import one.rewind.util.NetworkUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import se.vidstige.jadb.JadbConnection;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;
import se.vidstige.jadb.managers.PackageManager;

import java.io.*;
import java.net.URL;
import java.util.List;

public class AndroidDevice {

    public static final Logger logger = LogManager.getLogger(AndroidDevice.class.getName());


    String udid = "ZX1G323GNB";
    int appiumPort = 47454;

    public volatile boolean running = true;

    static File nodeJsExecutable = new File("C:\\Program Files\\nodejs\\node.exe");
    static File appniumMainJsFile = new File("C:\\Users\\lenovo\\AppData\\Local\\Programs\\appium-desktop\\resources\\app");

    public static String LOCAL_IP;

    // 配置设定
    static {

        Config ioConfig = Configs.getConfig(BasicRequester.class);
        LOCAL_IP = ioConfig.getString("requesterLocalIp");
        LOCAL_IP = NetworkUtil.getLocalIp();
    }

    BrowserMobProxy bmProxy;
    int proxyPort;

    AppiumDriverLocalService service;
    URL serviceUrl;
    AndroidDriver driver;

    /**
     * @param udid
     * @param appiumPort
     * @throws Exception
     */
    public AndroidDevice(String udid, int appiumPort) throws Exception {
        this.udid = udid;
    }

    public AndroidDevice() {
    }

    ;

    /**
     * 获得设备的宽度
     */
    public int getWidth() {
        return driver.manage().window().getSize().width;
    }

    /**
     * 获得设备的高度
     */
    public int getHeight() {
        return driver.manage().window().getSize().height;
    }


    public void startProxy(int port) {

        CertificateAndKeySource source =
                new PemFileCertificateSource(new File("ca.crt"), new File("pk.crt"), "sdyk");

        // tell the MitmManager to use the root certificate we just generated
        ImpersonatingMitmManager mitmManager = ImpersonatingMitmManager.builder()
                .rootCertificateSource(source)
                .build();

        bmProxy = new BrowserMobProxyServer();
        bmProxy.setTrustAllServers(true);
        bmProxy.setMitmManager(mitmManager);
        bmProxy.start(port);
        proxyPort = bmProxy.getPort();

        logger.info("Proxy started @port {}", proxyPort);

        RequestFilter filter = (request, contents, messageInfo) -> {

            //logger.info(messageInfo.getOriginalUrl());
            //logger.info(contents.getTextContents());

            return null;
        };

        bmProxy.addFirstHttpFilterFactory(new RequestFilterAdapter.FilterSource(filter, 16777216));

        bmProxy.addResponseFilter((response, contents, messageInfo) -> {

            //logger.info(messageInfo.getOriginalUrl());
            //logger.info(contents.getTextContents());
        });

    }

    public void stopProxy() {
        bmProxy.stop();
    }

    /**
     * 设置设备Wifi代理
     *
     * @param mobileSerial
     */
    public void setupWifiProxy(String mobileSerial) {

        try {

            JadbConnection jadb = new JadbConnection();

            // TODO
            // 需要调用process 启动adb daemon

            List<JadbDevice> devices = jadb.getDevices();

            for (JadbDevice d : devices) {

                if (d.getSerial().equals(mobileSerial)) {

                    execShell(d, "settings", "put", "global", "http_proxy", LOCAL_IP + ":" + proxyPort);
                    // execShell(d, "settings", "put", "global", "http_proxy", "10.0.0.51:49999");

                    // 只需要第一次加载
					/*d.push(new File("ca.crt"),
							new RemoteFile("/sdcard/_certs/ca.crt"));*/

					/*String ssid = "SDYK-AI";
					String password = "sdyk-ai@2018";

					try {
						new PackageManager(d).install(new File("proxy-setter-debug-0.2.1.apk"));
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("bmProxy-setter already installed.");
					}

					execShell(d,"am", "start",
							"-n", "tk.elevenk.proxysetter/.MainActivity",
							"-e", "ssid", ssid,
							"-e", "clear", "true");

					Thread.sleep(2000);

					if(password == null) {
						execShell(d,"am", "start",
								"-n", "tk.elevenk.proxysetter/.MainActivity",
								"-e", "host", LOCAL_IP,
								"-e", "port", String.valueOf(proxyPort),
								"-e", "ssid", ssid,
								"-e", "reset-wifi", "true");
					} else {
						execShell(d,"am", "start",
								"-n", "tk.elevenk.proxysetter/.MainActivity",
								"-e", "host", LOCAL_IP,
								"-e", "port", String.valueOf(proxyPort),
								"-e", "ssid", ssid,
								"-e", "bypass", password,
								"-e", "reset-wifi", "true");
					}*/

                    Thread.sleep(2000);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置设备Wifi代理
     *
     * @param mobileSerial
     */
    public void installApk(String mobileSerial, String fileName) {

        try {

            JadbConnection jadb = new JadbConnection();

            List<JadbDevice> devices = jadb.getDevices();

            for (JadbDevice d : devices) {

                if (d.getSerial().equals(mobileSerial)) {

                    new PackageManager(d).install(new File(fileName));
                    Thread.sleep(2000);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @param mobileSerial
     */
    public void removeWifiProxy(String mobileSerial) {

        try {

            JadbConnection jadb = new JadbConnection();

            List<JadbDevice> devices = jadb.getDevices();

            for (JadbDevice d : devices) {

                if (d.getSerial().equals(mobileSerial)) {

                    execShell(d, "settings", "delete", "global", "http_proxy");
                    execShell(d, "settings", "delete", "global", "https_proxy");
                    execShell(d, "settings", "delete", "global", "global_http_proxy_host");
                    execShell(d, "settings", "delete", "global", "global_http_proxy_port");

                    Thread.sleep(2000);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void execShell(JadbDevice d, String command, String... args) throws IOException, JadbException {

        InputStream is = d.executeShell(command, args);

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder builder = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n"); //appende a new line
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.info(builder.toString());
    }


    /**
     * 初始化AppiumDriver
     *
     * @throws Exception
     */
    public void initAppiumDriver() throws Exception {

        DesiredCapabilities serverCapabilities = new DesiredCapabilities();

        serverCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        serverCapabilities.setCapability(MobileCapabilityType.UDID, udid); // udid是设备的唯一标识
        serverCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 3600);

        AppiumDriverLocalService service = new AppiumServiceBuilder()
                .withCapabilities(serverCapabilities)
                .usingPort(appiumPort)
                .withArgument(GeneralServerFlag.LOG_LEVEL, "info")
                .build();

        service.start();

        // 从AppInfo中选择需要启动的程序，如果不需要启动可以选择index
        AppInfo info = AppInfo.get(AppInfo.Defaults.Contacts);

        URL serviceUrl = service.getUrl();
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("app", "");
        capabilities.setCapability("appPackage", info.appPackage); // App包名
        capabilities.setCapability("appActivity", info.appActivity); // App启动Activity
        capabilities.setCapability("fastReset", false);
        capabilities.setCapability("fullReset", false);
        capabilities.setCapability("noReset", true);

        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.ANDROID_UIAUTOMATOR2);
        // TODO 下面两行代码如果不添加 是否不能进行小程序的？
        // capabilities.setCapability("chromeOptions", ImmutableMap.of("androidProcess", webViewAndroidProcessName));
        // webViewAndroidProcessName = "com.tencent.mm:appbrand0"; App中的加载WebView的进程名

        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, udid);

        driver = new AndroidDriver(new URL("http://127.0.0.1:" + appiumPort + "/wd/hub"), capabilities);

        Thread.sleep(5000);
    }

    /**
     * 以apk文件的路径为参数，安装apk
     */
    public void installApk(String apkPath) {
        String commandStr = "adb -s " + udid + " install " + apkPath;
        Util.exeCmd(commandStr);
    }

    /**
     * 返回已经安装的应用程序
     */
    public void listApps() {
        String commandStr = "adb -s " + udid + " shell pm list packages -3"; //-3为第三方应用（[-f] [-d] [-e] [-s] [-3] [-i] [-u]）
        Util.exeCmd(commandStr);
    }

    /**
     * 以app的包名为参数，卸载选择的应用程序
     *
     * @param appPackage
     */
    public void uninstallApp(String appPackage) {//卸载选择的app
        String commandStr = "adb -s " + udid + " uninstall " + appPackage; //app="com.ss.android.ugc.aweme"
        Util.exeCmd(commandStr);
    }

    /**
     * 清空通讯录
     * 该方法是利用adb命令中的clear，同时可以以应用程序的包名为参数，修改为重置app的方法
     */
    public void clearContacts() {
        String commandStr = "adb -s " + udid + " shell pm clear com.android.providers.contacts";
        Util.exeCmd(commandStr);
    }

    /**
     * 通过包名和Activity名，打开选择的应用程序
     *
     * @param appPackage
     * @param appActivity
     */
    public void startActivity(String appPackage, String appActivity) {
        String commandStr = "adb -s " + udid + " shell am start " + appPackage + "/" + appActivity;
        Util.exeCmd(commandStr);
    }


    /**
     * 添加指定姓名和电话的单个联系人
     * 通过+按钮，输入姓名和电话号码后返回
     * @param name
     * @param number
     * @throws InterruptedException
     */
    public void addContact(String name, String number) throws InterruptedException {

        WebElement addButton = driver.findElementByAccessibilityId("添加新联系人");
        addButton.click();//点击添加新联系人
        Thread.sleep(500);
        driver.findElement(By.xpath("//android.widget.EditText[contains(@text,'姓名')]")).sendKeys(name);
        driver.findElement(By.xpath("//android.widget.EditText[contains(@text,'电话')]")).sendKeys(number);
        driver.findElementById("com.android.contacts:id/menu_save").click();//点击保存
        Thread.sleep(1500);
        //driver.pressKeyCode(4);//安卓机器的返回键
        driver.navigate().back();//安卓机器的返回键
        Thread.sleep(500);
    }

    /**
     * 通过txt文件批量导入联系人
     * 通过通讯录界面的+按钮，加入联系人后返回，循环添加
     * @param filePath
     * @throws Exception
     */
    public void addContactsFromFile(String filePath) throws Exception {

        // adb 启动 contacts

        String src = FileUtil.readFileByLines(filePath);

        for (String contact : src.split("\\n|\\r\\n")) {

            String[] token = contact.split("\\t");

            WebElement addButton = driver.findElementByAccessibilityId("添加新联系人");
            addButton.click();//点击添加新联系人

            Thread.sleep(500);

            driver.findElement(By.xpath("//android.widget.EditText[contains(@text,'姓名')]")).sendKeys(token[0]);
            driver.findElement(By.xpath("//android.widget.EditText[contains(@text,'电话')]")).sendKeys(token[1]);

            Thread.sleep(500);
            driver.findElementById("com.android.contacts:id/menu_save").click();//点击保存

            Thread.sleep(500);
            //driver.pressKeyCode(4);//安卓机器的返回键

            driver.navigate().back();//安卓机器的返回键
            Thread.sleep(500);

        }
    }


    /**
     * 删除指定姓名的联系人
     *
     * @param name
     * @throws InterruptedException
     */
    public void deleteOneContact(String name) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            List<WebElement> lis = driver.findElements(By.xpath("//android.widget.TextView[contains(@text,name)]"));
            if (lis.size() != 0) {
                driver.findElement(By.xpath("//android.widget.TextView[contains(@text,name)]")).click();
                Thread.sleep(700);
                driver.findElementByAccessibilityId("更多选项").click();
                Thread.sleep(700);
                driver.findElement(By.xpath("//android.widget.TextView[contains(@text,'删除')]")).click();
                Thread.sleep(700);
                driver.findElement(By.xpath("//android.widget.Button[contains(@text,'删除'"));
                Thread.sleep(200);
            }
            TouchAction action1 = new TouchAction(driver).press(PointOption.point(700, 2360)).waitAction().moveTo(PointOption.point(700, 540)).release();
            action1.perform();
            Thread.sleep(300);
        }
    }


    /**
     * 导出所有钉钉的联系人，半自动
     *
     * @throws Exception
     */
    public void temp() throws Exception {
        driver.findElement(By.xpath("//android.widget.TextView[contains(@text,'通讯录')]")).click();
        Thread.sleep(500);
        driver.findElement(By.xpath("//android.widget.TextView[contains(@text,'组织架构')]")).click();
        Thread.sleep(200);
        for (int j = 0; j < 50; j++) {

            List<WebElement> lis = driver.findElementsById("com.alibaba.android.rimet:id/item_contact");

            for (int i = 0; i < lis.size(); i++) {
                lis.get(i).click();
                Thread.sleep(300);
                System.out.println(driver.findElementById("com.alibaba.android.rimet:id/cell_subTitle"));
                Thread.sleep(300);
                driver.findElementByAccessibilityId("返回");
            }

            TouchAction action1 = new TouchAction(driver).press(PointOption.point(700, 2360)).waitAction().moveTo(PointOption.point(700, 200)).release();
            action1.perform();
            Thread.sleep(400);
        }
    }

    public void getDingdingCompanyContact() throws Exception {
        driver.findElement(By.xpath("//android.widget.TextView[contains(@text,'通讯录')]")).click();
        Thread.sleep(500);
        driver.findElementByAccessibilityId("企业广场").click();
        Thread.sleep(10000);
        System.out.println(driver.getContext());

    }


    public static void main(String arg[]) throws Exception {

        AndroidDevice nexus6 = new AndroidDevice();
        nexus6.initAppiumDriver();

        Thread.sleep(300);
        nexus6.getDingdingCompanyContact();

    }

}
