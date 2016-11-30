package name.caiyao.redenvelope;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.findFirstFieldByExactType;
import static de.robv.android.xposed.XposedHelpers.getObjectField;
import static de.robv.android.xposed.XposedHelpers.newInstance;
import static de.robv.android.xposed.XposedHelpers.setObjectField;
import static name.caiyao.redenvelope.MainHook.QQ_PACKAGE_NAME;

/**
 * Created by 蔡小木 on 2016/11/11 0011.
 */

public class HookUtils {

    private static final String LUCKY_MONEY_RECEIVE_UI_CLASS_NAME = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";

    private static long msgUid;
    private static String senderuin;
    private static String frienduin;
    private static int istroop;
    private static String selfuin;
    private static Context globalContext = null;
    private static Object HotChatManager = null;
    private static Object TicketManager;


    static void hookQQ(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        Context context = (Context) callMethod(callStaticMethod(findClass("android.app.ActivityThread", null), "currentActivityThread", new Object[0]), "getSystemContext", new Object[0]);
        String versionName = context.getPackageManager().getPackageInfo(loadPackageParam.packageName, 0).versionName;
        VersionUtil.init(versionName);

        findAndHookMethod("com.tencent.mobileqq.app.MessageHandlerUtils", loadPackageParam.classLoader, "a",
                "com.tencent.mobileqq.app.QQAppInterface",
                "com.tencent.mobileqq.data.MessageRecord", Boolean.TYPE, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        int msgtype = (int) getObjectField(param.args[1], "msgtype");
                        if (msgtype == -2025) {
                            msgUid = (long) getObjectField(param.args[1], "msgUid");
                            senderuin = (String) getObjectField(param.args[1], "senderuin");
                            frienduin = getObjectField(param.args[1], "frienduin").toString();
                            istroop = (int) getObjectField(param.args[1], "istroop");
                            selfuin = getObjectField(param.args[1], "selfuin").toString();
                        }
                    }
                }

        );

        findAndHookMethod("com.tencent.mobileqq.data.MessageForQQWalletMsg", loadPackageParam.classLoader, "doParse", new
                XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        if (msgUid == 0) {
                            return;
                        }
                        msgUid = 0;

                        Object mQQWalletRedPacketMsg = getObjectField(param.thisObject, "mQQWalletRedPacketMsg");
                        String redPacketId = getObjectField(mQQWalletRedPacketMsg, "redPacketId").toString();
                        String authkey = (String) getObjectField(mQQWalletRedPacketMsg, "authkey");
                        ClassLoader walletClassLoader = (ClassLoader) callStaticMethod(findClass("com.tencent.mobileqq.pluginsdk.PluginStatic", loadPackageParam.classLoader), "getOrCreateClassLoader", globalContext, "qwallet_plugin.apk");
                        String requestUrl = ("&uin=" + selfuin) +
                                "&listid=" + redPacketId +
                                "&name=" + Uri.encode("") +
                                "&answer=" +
                                "&groupid=" + (istroop == 0 ? selfuin : frienduin) +
                                "&grouptype=" + getGroupType() +
                                "&groupuin=" + senderuin +
                                "&authkey=" + authkey;

                        Class qqplugin = findClass(VersionUtil.QQPluginClass, walletClassLoader);

                        int random = Math.abs(new Random().nextInt()) % 16;
                        String reqText = (String) callStaticMethod(qqplugin, "a", globalContext, random, false, requestUrl);
                        String hongbaoRequestUrl = "https://mqq.tenpay.com/cgi-bin/hongbao/qpay_hb_na_grap.cgi?ver=2.0&chv=3" +
                                "&req_text=" + reqText +
                                "&random=" + random +
                                "&skey_type=2" +
                                "&skey=" + callMethod(TicketManager, "getSkey", selfuin);

                        Class<?> walletClass = findClass("com.tenpay.android.qqplugin.b.d", walletClassLoader);
                        Object pickObject = newInstance(walletClass, callStaticMethod(qqplugin, "a", globalContext));
                        callMethod(pickObject, "a", hongbaoRequestUrl);
                    }
                }

        );


        findAndHookMethod("com.tencent.mobileqq.activity.SplashActivity", loadPackageParam.classLoader, "doOnCreate", Bundle.class, new

                XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        globalContext = (Context) param.thisObject;
                    }
                }

        );


        findAndHookConstructor("mqq.app.TicketManagerImpl", loadPackageParam.classLoader, "mqq.app.AppRuntime", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                TicketManager = param.thisObject;
            }
        });


        findAndHookConstructor("com.tencent.mobileqq.app.HotChatManager", loadPackageParam.classLoader, "com.tencent.mobileqq.app.QQAppInterface", new

                XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        HotChatManager = param.thisObject;
                    }
                }

        );
    }

    static void hookWechat(final XC_LoadPackage.LoadPackageParam lpparam) throws PackageManager.NameNotFoundException {
        Context context = (Context) callMethod(callStaticMethod(findClass("android.app.ActivityThread", null), "currentActivityThread", new Object[0]), "getSystemContext", new Object[0]);
        String versionName = context.getPackageManager().getPackageInfo(lpparam.packageName, 0).versionName;
        VersionUtil.init1(versionName);

        findAndHookMethod(VersionUtil.getMessageClass, lpparam.classLoader, "b", Cursor.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                int type = (int) getObjectField(param.thisObject, "field_type");
                if (type == 436207665 || type == 469762097) {
                    int status = (int) getObjectField(param.thisObject, "field_status");
                    if (status == 4) {
                        return;
                    }
                    int isSend = (int) getObjectField(param.thisObject, "field_isSend");

                    String talker = getObjectField(param.thisObject, "field_talker").toString();

                    if (!isGroupTalk(talker) && isSend != 0) {
                        XposedBridge.log("!isGroupTalk(talker) && isSend != 0");
                    }
                    String content = getObjectField(param.thisObject, "field_content").toString();
                    String nativeUrlString = getFromXml(content, "nativeurl");
                    Uri nativeUrl = Uri.parse(nativeUrlString);
                    int msgType = Integer.parseInt(nativeUrl.getQueryParameter("msgtype"));
                    int channelId = Integer.parseInt(nativeUrl.getQueryParameter("channelid"));
                    String sendId = nativeUrl.getQueryParameter("sendid");
                    final Object ab = newInstance(findClass("com.tencent.mm.plugin.luckymoney.c.ab", lpparam.classLoader),
                            msgType, channelId, sendId, nativeUrlString, "", "", talker, "v1.0");
                    callMethod(callStaticMethod(findClass("com.tencent.mm.model.ah", lpparam.classLoader), VersionUtil.getNetworkByModelMethod), "a", ab, 0);
                }
            }
        });


        findAndHookMethod(LUCKY_MONEY_RECEIVE_UI_CLASS_NAME, lpparam.classLoader, VersionUtil.receiveUIFunctionName, int.class, int.class, String.class, VersionUtil.receiveUIParamName, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Button button = (Button) findFirstFieldByExactType(param.thisObject.getClass(), Button.class).get(param.thisObject);
                if (button.isShown() && button.isClickable()) {
                    button.performClick();
                }
            }
        });
    }

    static void hideModule(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        findAndHookMethod("android.app.ApplicationPackageManager", loadPackageParam.classLoader, "getInstalledApplications", int.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                List<ApplicationInfo> applicationList = (List<ApplicationInfo>) param.getResult();
                List<ApplicationInfo> resultapplicationList = new ArrayList<>();
                for (ApplicationInfo applicationInfo : applicationList) {
                    String packageName = applicationInfo.packageName;
                    if (isTarget(packageName)) {
                        log("Hid package: " + packageName);
                    } else {
                        resultapplicationList.add(applicationInfo);
                    }
                }
                param.setResult(resultapplicationList);
            }
        });
        findAndHookMethod("android.app.ApplicationPackageManager", loadPackageParam.classLoader, "getInstalledPackages", int.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                List<PackageInfo> packageInfoList = (List<PackageInfo>) param.getResult();
                List<PackageInfo> resultpackageInfoList = new ArrayList<>();

                for (PackageInfo packageInfo : packageInfoList) {
                    String packageName = packageInfo.packageName;
                    if (isTarget(packageName)) {
                        log("Hid package: " + packageName);
                    } else {
                        resultpackageInfoList.add(packageInfo);
                    }
                }
                param.setResult(resultpackageInfoList);
            }
        });
        findAndHookMethod("android.app.ApplicationPackageManager", loadPackageParam.classLoader, "getPackageInfo", String.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                String packageName = (String) param.args[0];
                if (isTarget(packageName)) {
                    param.args[0] = QQ_PACKAGE_NAME;
                    log("Fake package: " + packageName + " as " + QQ_PACKAGE_NAME);
                }
            }
        });
        findAndHookMethod("android.app.ApplicationPackageManager", loadPackageParam.classLoader, "getApplicationInfo", String.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                String packageName = (String) param.args[0];
                if (isTarget(packageName)) {
                    param.args[0] = QQ_PACKAGE_NAME;
                    log("Fake package: " + packageName + " as " + QQ_PACKAGE_NAME);
                }
            }
        });
        findAndHookMethod("android.app.ActivityManager", loadPackageParam.classLoader, "getRunningServices", int.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                List<ActivityManager.RunningServiceInfo> serviceInfoList = (List<ActivityManager.RunningServiceInfo>) param.getResult();
                List<ActivityManager.RunningServiceInfo> resultList = new ArrayList<>();

                for (ActivityManager.RunningServiceInfo runningServiceInfo : serviceInfoList) {
                    String serviceName = runningServiceInfo.process;
                    if (isTarget(serviceName)) {
                        log("Hid service: " + serviceName);
                    } else {
                        resultList.add(runningServiceInfo);
                    }
                }
                param.setResult(resultList);
            }
        });
        findAndHookMethod("android.app.ActivityManager", loadPackageParam.classLoader, "getRunningTasks", int.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                List<ActivityManager.RunningTaskInfo> serviceInfoList = (List) param.getResult();
                List<ActivityManager.RunningTaskInfo> resultList = new ArrayList<>();

                for (ActivityManager.RunningTaskInfo runningTaskInfo : serviceInfoList) {
                    String taskName = runningTaskInfo.baseActivity.flattenToString();
                    if (isTarget(taskName)) {
                        log("Hid task: " + taskName);
                    } else {
                        resultList.add(runningTaskInfo);
                    }
                }
                param.setResult(resultList);
            }
        });
        findAndHookMethod("android.app.ActivityManager", loadPackageParam.classLoader, "getRunningAppProcesses", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = (List) param.getResult();
                List<ActivityManager.RunningAppProcessInfo> resultList = new ArrayList<>();

                for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcessInfos) {
                    String processName = runningAppProcessInfo.processName;
                    if (isTarget(processName)) {
                        log("Hid process: " + processName);
                    } else {
                        resultList.add(runningAppProcessInfo);
                    }
                }
                param.setResult(resultList);
            }
        });
    }

    private static boolean isTarget(String name) {
        return name.contains("caiyao");
    }

    private static int getGroupType() throws IllegalAccessException {
        int grouptype = 0;
        if (istroop == 3000) {
            grouptype = 2;

        } else if (istroop == 1) {
            Map map = (Map) findFirstFieldByExactType(HotChatManager.getClass(), Map.class).get(HotChatManager);
            if (map != null && map.containsKey(frienduin)) {
                grouptype = 5;
            } else {
                grouptype = 1;
            }
        } else if (istroop == 0) {
            grouptype = 0;
        } else if (istroop == 1004) {
            grouptype = 4;

        } else if (istroop == 1000) {
            grouptype = 3;

        } else if (istroop == 1001) {
            grouptype = 6;
        }
        return grouptype;
    }

    private static boolean isGroupTalk(String talker) {
        return talker.endsWith("@chatroom");
    }

    private static String getFromXml(String xmlmsg, String node) throws XmlPullParserException, IOException {
        String xl = xmlmsg.substring(xmlmsg.indexOf("<msg>"));
        //nativeurl
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser pz = factory.newPullParser();
        pz.setInput(new StringReader(xl));
        int v = pz.getEventType();
        String result = "";
        while (v != XmlPullParser.END_DOCUMENT) {
            if (v == XmlPullParser.START_TAG) {
                if (pz.getName().equals(node)) {
                    pz.nextToken();
                    result = pz.getText();
                    break;
                }
            }
            v = pz.next();
        }
        return result;
    }
}
