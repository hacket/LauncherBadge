# LauncherBadge

An Android library supports badge notification like iOS in Samsung ,Huawei, Xiaomi, Htc, Apex, OPPO and vivo launchers.


```kt
// add badge notification
// only miui launcher need notification; others launcher not.
LauncherBadge.setBadgeCount(context, badgeCount, notification)

// remove badge notification
LauncherBadge.removeBadge(context)
```

## Android桌面角标介绍

1. 原生系统不支持
   Android 8.0之前原生是不支持应用角标的；8.0及+的系统支持红点不支持数量

2. 适配确实是非常坑爹的需求。原生系统根本就没有这个功能，国内很多厂家效仿ios都自己定义了该功能。Android程序员就很苦逼，要适配很多机型。建议万不得已情况下还是不要进行这项工作。 其实这个功能和手机厂家没有直接的关系，而是和手机当前使用的launcher有直接关系。比如三星的手机我安装Asus桌面，这时候我们就不能适配三星launcher而要去适配华硕launcher。


## 特别适配

### 小米（支持）
小米自MIUI6.0以后，角标的显示和通知栏Notification绑在一起。目前发现只有小米需要和Notification绑在一起才能生效。
小米需要发一个通知，进入应用后红点会自动消失，测试的时候进程只能在后台，否则没有效果

* MIUI6&7桌面角标开源代码简介
  https://dev.mi.com/docs/appsmarket/technical_docs/badge/

* MIUI 6 至 MIUI 11 桌面角标适配说明
  https://dev.mi.com/console/doc/detail?pId=939

* 常见MIUI桌面应用角标问题Q&A
  https://dev.mi.com/console/doc/detail?pId=2321

#### MIUI桌面角标不显示相关问题

##### 应用发送了通知，桌面应用角标没有显示出来
首先打开应用通知设置页面，在`”设置-通知管理“`里点击应用，查看`”显示桌面图标角标“`开关是否开启。大部分应用默认是关闭状态。其次确认通知栏中是否有该应用的通知，当然不包括**媒体通知**、**进度条通知**和**常驻通知**这三种类型通知，因为默认不会统计。 最后需要确认下通知的messageCount值是否正确。

> MIUI12：设置-->通知管理-->你的App-->显示桌面图标角标

##### 桌面应用图标显示角标，在最近任务中关闭应用，角标消失
在最近任务关闭应用后，会清除掉应用的所有通知，因此桌面应用角标会隐藏。

##### 应用图标显示角标，点击应用图标后，角标消失
点击应用图标后，会默认隐藏掉应用图标角标。有如下两种方式可以重新显示：
1. 发一条新的通知，其通知id与之前发送的通知id不重复
2. 更新已发送通知的messageCount值

##### 桌面应用角标最大显示值是9（9条通知messageCount之后，超过的通知不会累计）
在通知栏显示的应用发送的通知是有条数限制的，**每一个应用最多只能显示10条通知**。在应用没有指定groupkey的情况下，超过3条系统会默认发一条autosummary的通知，将应用的通知成组显示。也就是说，在这种情况下，用户可见的通知数量是9条，在桌面显示的应用角标值也是9。开发者可以通过设置通知messageCount值来突破这一限制。


##### 通过开发者文档设置了messageCount值，但桌面应用角标显示的值不符合预期
桌面应用角标数值是累加应用在通知栏显示的除媒体、进度条和常驻通知外的所有通知的messageCount值。即并不是开发者设置了某一条通知的messageCount值后，桌面应用角标显示的就是设置的messageCount值。


##### 绑定了Notification，桌面角标数量是通知栏不同NotificationId的messageCount累计之和

##### App在前台不显示角标，只有App在后台才显示

##### 收到IM未读消息，干掉App，桌面角标消失，下次打开App也不会有角标；下次收到通知时可以显示角标？
将首次展示的Notification的id记录下来，首次展示的Notification的messageCount设置为所有未读消息数量，后续该NotificationId收到新的消息还是为所有未读消息数量；收到不同NotificationId的消息，其messageCount为该id的所有未读消息数量

##### MIUI设置第三方桌面，只有MIUI官方认证的桌面才能设置为默认桌面


### 三星（支持）

### 华为（高版本支持）

高版本的桌面支持；低版本的不支持，只有系统App支持

huawei(H60-L01) 19(4.4.2) 不支持

权限
```
setBadgeCount error Permission Denial: opening provider com.huawei.android.launcher.LauncherProvider from ProcessRecord{ec748ba 15837:me.hacket.launcherbadge/u0a123} (pid=15837, uid=10123) requires com.huawei.android.launcher.permission.READ_SETTINGS or com.huawei.android.launcher.permission.WRITE_SETTINGS		HuaweiBadger[[com.huawei.android.launcher]]


// or

java.lang.SecurityException: Permission Denial: reading com.huawei.android.launcher.LauncherProvider from pid=7786, uid=10015 requires com.huawei.android.launcher.permission.WRITE_SETTINGS: uid 10015 does not have com.huawei.android.launcher.permission.WRITE_SETTINGS.
    at android.app.ContextImpl.enforce(ContextImpl.java:1775)
    at android.app.ContextImpl.enforcePermission(ContextImpl.java:1787)
    at android.content.ContextWrapper.enforcePermission(ContextWrapper.java:561)
    at com.huawei.android.launcher.LauncherProvider.checkPermission(LauncherProvider.java:560)
    at com.huawei.android.launcher.LauncherProvider.call(LauncherProvider.java:575)
    at android.content.ContentProvider$Transport.call(ContentProvider.java:325)
    at android.content.ContentProviderNative.onTransact(ContentProviderNative.java:284)
    at android.os.Binder.execTransact(Binder.java:404)
    at dalvik.system.NativeStart.run(Native Method)
```

### OPPO（不支持，需要申请）

新款的OPPO仅支持内置应用、微信和QQ显示角标，若要使用角标功能，必须提交申请，审核通过了才能开放，官方给的具体审核标准如下：
申请角标接入规则（应用必须适配OPPO手机，保证角标功能测试通过）
```
a) 系统应用；

b) 国内外各区域用户量排名Top5的三方即时通讯类应用，且只允许显示即时通信消息类通知（如QQ、微信、facebook、line）；

c) OPPO公司内部费商业化及运营性质的办公类型即时通信应用（如Teamtalk）；

d) 国内外邮件类应用（各区域各属于用户量第一梯队的应用）。
```

### VIVO（不支持，需要申请）


### Nowa Launcher（免费版不支持，收费版支持）
nova launcher 的免费版本是没有桌面角标的功能,nova launcher prime版本才有(需要收费且国内各大应用市场没有提供下载)

http://novalauncher.com/

### Apex Launcher (免费版不支持，pro版支持)
apex launcher 也是需要收费。

https://www.apexlauncher.com/

### Microsoft Launcher

### adw launcher
adw launcher 是免费的功能正常，但是ui太难看。

### asus launcher
asus launcher 是免费的功能正常，但是发现如果连续发送未读消息，角标显示有延迟。

### 其他Launcher
* Infinix S4(XOS v5.0.0 Android9  不支持

### 官方设置
https://developer.android.com/training/notify-user/badges


## License

    Copyright 2013 Jake Wharton

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.