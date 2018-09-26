package com.bugsir.permissions;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

/**
 * @author: Lin Xiongqing
 * @date: 2018/5/2 16:36
 * @description: 权限设置界面
 */
public class PermissionSetting {
    private static final String ROM_TAG = Build.MANUFACTURER.toLowerCase();
    public static final int SETTING_REQUSTCODE=8888;
    public static void go2Setting(Activity context) {
        try
        {
            context.startActivityForResult(getIntent(context),SETTING_REQUSTCODE);
        }catch(Exception e)
        {
            defaultApi(context);
        }

    }

    private static Intent getIntent(Context context) {

        if (ROM_TAG.contains("huawei")) {
            return huaweiApi(context);
        } else if (ROM_TAG.contains("xiaomi")) {
            return xiaomiApi(context);
        } else if (ROM_TAG.contains("vivo")) {
            return vivoApi(context);
        } else if (ROM_TAG.contains("meizu")) {
            return meizuApi(context);
        }
        return defaultApi(context);
    }

    private static Intent defaultApi(Context context)
    {
        return new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.fromParts("package", context.getPackageName(), null));
    }

    private static Intent huaweiApi(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return defaultApi(context);
        }
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity"));
        return intent;
    }

    private static Intent xiaomiApi(Context context) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.putExtra("extra_pkgname", context.getPackageName());
        return intent;
    }
    private static Intent vivoApi(Context context) {
        Intent intent = new Intent();
        intent.putExtra("packagename", context.getPackageName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.SoftPermissionDetailActivity"));
        } else {
            intent.setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.safeguard.SoftPermissionDetailActivity"));
        }
        return intent;
    }

    private static Intent meizuApi(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            return defaultApi(context);
        }
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.putExtra("packageName", context.getPackageName());
        intent.setComponent(new ComponentName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity"));
        return intent;
    }


}
