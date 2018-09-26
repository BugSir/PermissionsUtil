package com.bugsir.permissions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 *@author: BUG SIR
 *@date: 2018/9/21 16:54
 *@description: 权限申请辅助activity
 */
public    class PermissionActivity extends Activity   {
    public static final String KEY_PERMISSIONS="key_permissions";
    public static void start(final Context context, ArrayList<String> permissions) {
        Intent starter = new Intent(context, PermissionActivity.class);
        starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        starter.putStringArrayListExtra(KEY_PERMISSIONS,permissions);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
