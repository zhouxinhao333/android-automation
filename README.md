# 使用说明


 
- **udid的获取** ：在shell中执行”adb devices”命令，返回行中显示已经连接的设备udid；
- **app的包名和Activity名** ：是打开应用程序必须的两个参数，可以在设备已经连接，且开启了对应应用程序的情况下，使用shell命令”adb shell dumpsys window w |findstr \/ |findstr name="获取app的包名和Activity名；
- **AppInfo**：该类中预设了App以及对应的包名和Activity名，且可以在后续的使用过程中自行添加新的app和对应的包名及Activity名，以方便在初始化AppiumDriver时使用；

-------------------

## AndroidDevice类

> **功能描述**：
1、初始化AppiumDriver
2、设置http/https代理，清空代理
3、通过shell命令安装apk，卸载apk，显示已经安装的应用程序，打开对应包名的应用程序
4、通讯录操作：添加指定姓名的联系人，通过txt文件批量添加联系人，清空通讯录，删除指定姓名的联系人
5、半自动操作：导出钉钉中同一个组织架构的所有联系人（除自己）

————————————————————————————————
> **使用方法**：
AndroidDeviceTest类
1、使用new新建一个AndroidDevice对象，如nexus6，需要的参数为已连接设备的udid和使用的端口号
2、初始化AppiumDriver，通过获取AppInfo来更改初始化时启动的程序
3、确认使用各方法的初始状态后进行使用，在每一个设备执行的方法后添加Thread.sleep()以保证设备的运行状态

————————————————————————————————
> **方法详细**：   
**Int getWidth();**
返回设备的宽度     
**Int getHeight(); **
返回设备的高度    
**startProxy(int port); **
添加代理    
**stopProxy(); **
清空代理    
**initAppiumDriver(); **
初始化AppiumDriver，在其中注释B处更改初始化时需要打开的应用程序。如果需要测试小程序，则将TODO后的代码取消注释。    
**installApk(String apkPath); **
以本地apk文件的路径为参数，在对象设备中进行安装     
**listApps(); **
显示对象设备中已经安装的应用程序，有多种选择      
**uninstallApp(String appPackage); **
以应用程序的包名为参数，卸载设备中已经安装的应用程序     
startActivity(String appPackage, String appActivity);
以应用程序的包名和Activity名为参数（必需），启动对应的应用程序，可以在初始化AppiumDriver后打开新程序而不必再次初始化     
**clearContacts(); **
清空设备的通讯录联系人，重置通讯录程序。本质是利用abd命令对通讯录程序进行重置，将adb命令提取后可以转换为通过应用程序包名重置应用程序的操作。     
**addContact(String name, String number); **
以欲添加联系人的姓名和电话号码为参数，向设备通讯录内添加指定的联系人     
**addContactsFromFileString filePath(); **
以本地txt文件的路径为参数，批量导入联系人，其中txt内按照“姓名+换行符+电话号码”的格式，每一行一个联系人    
**deleteOneContact(String name); **
以欲删除的联系人的姓名为参数，删除指定的联系人（如果有多个相同姓名的联系人？）     
**Temp(); **
临时方法，半自动化添加钉钉内组织架构的联系人，在添加完一页的联系人后需手动调整位置    


## AndroidDevice类
> **功能描述**：
1、初始化AppiumDriver
2、从主页面进入到本微信的朋友圈
3、创建指定名称的群组，输入想要添加组员的id
4、进入指定名称的群组
5、在已经进入的朋友圈中提取图片并截图，保存至文件
6、通过朋友姓名进入聊天界面，在聊天界面发送消息
7、通过txt文件批量添加好友，可以修改验证信息和备注
8、提取聊天记录

————————————————————————————————

> **使用方法**：
WechatSimulatorTest类
1、使用new建立一个新的WechatSimulator对象，如wechat1，此处不需要参数，因为限定在微信应用程序中
2、使用initAppiumDriver方法初始化AppiumDriver
3、进行后续的方法和操作，在每一个设备需要进行模拟操作的代码后根据设备处理的时间添加Thread.sleep();方法

————————————————————————————————
> **方法详细**：
**initAppiumDriver();**
初始化AppiumDriver，在WechatSimulator类中这个方法内部设定为启动微信，最终开启微信的主页面，因此在接下来的操作中无需再次通过adb命令启动微信应用程序。
在打开微信的主页面后，将继续进入“我”界面，获取本台设备登录的微信号和微信名，以备后续使用（数据库）。
**getIntoFriend();**
从主页面进入到当前微信的朋友圈界面，即显示自己的朋友圈内容。
**createGroupChat(String groupName,String.. . id);**
创建新群组，以想要创建的群组名和想要加入的好友名为参数。
**getIntoGroup(String groupName);**
进入群组，以想要进入的群组名为参数。
**getMoments();**
在进入的数据库中进行操作，具体见代码注释。将图片，文字，链接以及发送者的微信名存入数据库。
**getFriendFromGroupChat();**
添加一个群组中不是好友的组员，要求在已经进入了群组的情况下使用
**getSimpleFriend(String friend);**
进入与指定好友的聊天界面，以好友名为参数。
**sendMsg(String msg);**
在已经进入的聊天界面（单独好友和群组均可）发送一条消息，消息内容为字符串msg的内容。
**getChatRecord();**
在群聊或者单人聊天中，以倒序的形式将所有类型的信息存入数据库中，包括文字，图片，视频，文件，时间。文字保存在text字段下，text_type字段保存文字的类型，如文字text,时间time,或是文件file等。目前能够实现保存文件的文件名，如需获取文件可以保存后从手机文件中进行提取。
**addFriendsByFile(String filepath);**
根据txt文件批量添加好友，在txt文件中，好友的微信号、欲发送的验证信息和备注3项在同一行以制表符分隔，每一个好友进行换行。在方法中可以预设验证信息字符串变量yanzheng和备注字符串变量beizhu，或不设置备注字符串变量，使好友的备注为微信号。
备注：由于微信的限制，同一个微信号在通过此方法添加10个左右的微信好友时即会被限制，现在还没有实用的解决方法。添加的同时将本机udid，朋友的微信号，朋友的昵称（在重填备注的界面获取微信默认的备注，即朋友的昵称），和当前的日期添加到数据库的weixin_friends表中。

## 注意事项
1、在每一次需要设备进行自动操作后添加Thread.sleep();代码，运行时的判定是不根据设备是否进行了模拟操作决定的，所以需要预留足够的操作和卡顿时间
2、使用通用的方式定义元素，发现元素位置，避免使用固定的x,y值，以在多个不同分辨率的设备上进行同样的操作。如果难以通过findElement方法发现元素位置，可以使用相对位置或者math函数，通过getWidth和getHeight获取宽度和高度以在不同的分辨率下执行同样的操作。
3、不同版本的apk所安装的相同应用程序也有不同，因此尽量使用同样的apk文件进行安装。
4、尽量避免使用id的方式定义元素，因为在不同版本的应用程序和不同系统版本的手机中，相同元素的id值大部分情况下不同。
5、在WechatSimulator中，由于数据库有user_id和user_name用以表示本机的微信名和微信号，可以考虑在initAppiumDriver()中启动微信先进入个人页面进行提取入库。
6、在mysql的配置文件my.ini中修改max_allowed_packet来解决byte_array超出数据库允许sql语句长度的问题。




## 反馈与建议
- 邮箱：<870974395@qq.com>
