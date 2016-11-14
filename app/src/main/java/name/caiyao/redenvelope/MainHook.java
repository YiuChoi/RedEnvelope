package name.caiyao.redenvelope;

import android.os.Build;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static name.caiyao.redenvelope.HookUtils.hideModule;
import static name.caiyao.redenvelope.HookUtils.hookQQ;
import static name.caiyao.redenvelope.HookUtils.hookWechat;

/**
 * Created by 蔡小木 on 2016/11/11 0011.
 */

public class MainHook implements IXposedHookLoadPackage {

    public static final String QQ_PACKAGE_NAME = "com.tencent.mobileqq";
    public static final String WECHAT_PACKAGE_NAME = "com.tencent.mm";


    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {


        if (loadPackageParam.packageName.equals(QQ_PACKAGE_NAME)) {
            hideModule(loadPackageParam);
            int ver = Build.VERSION.SDK_INT;
            if (ver < 21) {
                findAndHookMethod("com.tencent.common.app.BaseApplicationImpl", loadPackageParam.classLoader, "onCreate", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        hookQQ(loadPackageParam);
                    }
                });
            } else {
                hookQQ(loadPackageParam);
            }
        } else if (loadPackageParam.packageName.equals(WECHAT_PACKAGE_NAME)) {
            hideModule(loadPackageParam);
            hookWechat(loadPackageParam);
        }
    }
}
