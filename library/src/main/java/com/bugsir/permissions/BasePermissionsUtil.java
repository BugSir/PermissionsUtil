package com.bugsir.permissions;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *@author: BUG SIR
 *@date: 2018/9/21 16:13
 *@description: 权限工具类
 */
public    class BasePermissionsUtil {
    private Context mContext;
    private List<String> mPermissions=new ArrayList<>();//用户传进来要申请的权限
    private List<String> mPermissionsRequest=new ArrayList<>();//真正需要申请的权限
    private BasePermissionsUtil(Context context)
    {
        this.mContext=context;
    }

    public static BasePermissionsUtil with(Context context) {
        return new BasePermissionsUtil(context);
    }

    /**
     * 权限组
     * @param permissions
     * @return
     */
    public BasePermissionsUtil permissions(String[] ...permissions)
    {//暂时存储权限申请
        for (String[] temp:permissions) {
            mPermissions.addAll(Arrays.asList(temp));
        }
        return this;
    }

    /**
     * 单个权限
     * @param permissions
     * @return
     */
    public BasePermissionsUtil permissions(String ...permissions)
    {//暂时存储权限申请
        mPermissions.addAll(Arrays.asList(permissions));
        return this;
    }


    //-----------------------------------------------公共方法使用类--------------------------------------------------
    /**
     *  判断是否拥有权限
     * @param context
     * @param perms 权限列表
     * @return
     */
    public static boolean hasPermissions(@NonNull Context context,
                                         @Size(min = 1) @NonNull String... perms) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (context == null) {
            throw new IllegalArgumentException("Can't check mPermissions for null context");
        }

        for (String perm : perms) {//这里要优化下，返回需要申请的
            if (ContextCompat.checkSelfPermission(context, perm)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取应用权限
     *
     * @param packageName 包名
     * @return 清单文件中的权限列表
     */
    public static List<String> getPermissions(Context context,final String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            return Arrays.asList(
                    pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
                            .requestedPermissions
            );
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
