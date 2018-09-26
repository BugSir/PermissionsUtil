package com.bugsir.permissions.helper;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * @author: BUG SIR
 * @date: 2018/9/25 15:09
 * @description: 权限申请结果回调
 */
public interface IPermissionCallbacks{

        /**
         * @param requestCode 请求code
         * @param perms 成功权限列表
         */
    void onPermissionsGranted(int requestCode, @NonNull List<String> perms);

        /**
         * @param requestCode 请求code
         * @param perms 拒绝权限列表
         */
    void onPermissionsDenied(int requestCode, @NonNull List<String> perms);
}
