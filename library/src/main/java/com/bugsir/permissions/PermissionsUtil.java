package com.bugsir.permissions;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.bugsir.permissions.helper.AfterPermissionGranted;
import com.bugsir.permissions.helper.BaseRationale;
import com.bugsir.permissions.helper.IPermissionCallbacks;
import com.bugsir.permissions.helper.IRationale;
import com.bugsir.permissions.helper.IRationaleCallbacks;
import com.bugsir.permissions.helper.PermissionHelper;
import com.bugsir.permissions.helper.RationaleDialogConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *@author: BUG SIR
 *@date: 2018/9/21 16:13
 *@description: 权限工具类
 */
public    class PermissionsUtil {

    private static final String TAG = "PermissionsUtil";
    private List<String> mPermissions=new ArrayList<>();//用户传进来要申请的权限
//    private List<String> mPermissionsRequest=new ArrayList<>();//真正需要申请的权限
//    private List<String> mPermissionsGranted=new ArrayList<>();//真正需要申请的权限
    private int mRequestCode =9999;
    private IRationale mIRationale;
    private IPermissionCallbacks mPermissionsCallbacks;
    private IRationaleCallbacks mRationaleCallbacks;
    private RationaleDialogConfig mRationaleDialogConfig;
    private PermissionRequest mRequest;
    private Context mContext;
    //----------------------------------------公共方法类------------------------------------------------------
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

        for (String perm : perms) {
            if (ContextCompat.checkSelfPermission(context, perm)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     *  判断是否拥有权限
     * @param context
     * @param perms 权限列表
     * @return
     */
    public static boolean hasPermissions(@NonNull Context context,
                                         @Size(min = 1) @NonNull List<String> perms) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (context == null) {
            throw new IllegalArgumentException("Can't check mPermissions for null context");
        }

        for (String perm : perms) {
            if (ContextCompat.checkSelfPermission(context, perm)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     *  判断是否拥有权限
     * @param context
     * @param permissions 权限列表
     * @return
     */
    public static boolean hasPermissions(@NonNull Context context,
                                          String[] ...permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (context == null) {
            throw new IllegalArgumentException("Can't check mPermissions for null context");
        }
        for (String[] temp:permissions) {
            for (String perm : temp) {
                if (ContextCompat.checkSelfPermission(context, perm)
                        != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
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

    //------------------------------------其它内部公用方法类-------------------------------------------

    /**
     * Run permission callbacks on an object that requested permissions but already has them by
     * simulating {@link PackageManager#PERMISSION_GRANTED}.
     *
     * @param object      the object requesting permissions.
     * @param requestCode the permission request code.
     * @param perms       a list of permissions requested.
     */
    private static void notifyAlreadyHasPermissions(@NonNull Object object,
                                                    int requestCode,
                                                    @NonNull List<String> perms) {
        int[] grantResults = new int[perms.size()];
        for (int i = 0; i < perms.size(); i++) {
            grantResults[i] = PackageManager.PERMISSION_GRANTED;
        }

        onRequestPermissionsResult(requestCode, perms, grantResults, object);
    }

    /**
     * Handle the result of a permission request, should be called from the calling {@link
     * Activity}'s {@link ActivityCompat.OnRequestPermissionsResultCallback#onRequestPermissionsResult(int,
     * String[], int[])} method.
     * <p>
     * If any permissions were granted or denied, the {@code object} will receive the appropriate
     * callbacks through {@link IPermissionCallbacks} and methods annotated with {@link
     * AfterPermissionGranted} will be run if appropriate.
     *
     * @param requestCode  requestCode argument to permission result callback.
     * @param permissions  permissions argument to permission result callback.
     * @param grantResults grantResults argument to permission result callback.
     * @param receivers    an array of objects that have a method annotated with {@link
     *                     AfterPermissionGranted} or implement {@link IPermissionCallbacks}.
     */
    public static void onRequestPermissionsResult(int requestCode,
                                                  @NonNull List<String> permissions,
                                                  @NonNull int[] grantResults,
                                                  @NonNull Object... receivers) {
        // Make a collection of granted and denied permissions from the request.
        List<String> granted = new ArrayList<>();
        List<String> denied = new ArrayList<>();
        for (int i = 0; i < permissions.size(); i++) {
            String perm = permissions.get(i);
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                granted.add(perm);
            } else {
                denied.add(perm);
            }
        }

        // iterate through all receivers
        for (Object object : receivers) {
            // Report granted permissions, if any.
            if (!granted.isEmpty()) {
                if (object instanceof IPermissionCallbacks) {
                    ((IPermissionCallbacks) object).onPermissionsGranted(requestCode, granted);
                }
            }

            // Report denied permissions, if any.
            if (!denied.isEmpty()) {
                if (object instanceof IPermissionCallbacks) {
                    ((IPermissionCallbacks) object).onPermissionsDenied(requestCode, denied);
                }
            }

            // If 100% successful, call annotated methods
            if (!granted.isEmpty() && denied.isEmpty()) {
                runAnnotatedMethods(object, requestCode);
            }
        }
    }

    /**
     * Find all methods annotated with {@link AfterPermissionGranted} on a given object with the
     * correct requestCode argument.
     *
     * @param object      the object with annotated methods.
     * @param requestCode the requestCode passed to the annotation.
     */
    private static void runAnnotatedMethods(@NonNull Object object, int requestCode) {
        Class clazz = object.getClass();
        if (isUsingAndroidAnnotations(object)) {
            clazz = clazz.getSuperclass();
        }

        while (clazz != null) {
            for (Method method : clazz.getDeclaredMethods()) {
                AfterPermissionGranted ann = method.getAnnotation(AfterPermissionGranted.class);
                if (ann != null) {
                    // Check for annotated methods with matching request code.
                    if (ann.value() == requestCode) {
                        // Method must be void so that we can invoke it
                        if (method.getParameterTypes().length > 0) {
                            throw new RuntimeException(
                                    "Cannot execute method " + method.getName() + " because it is non-void method and/or has input parameters.");
                        }

                        try {
                            // Make method accessible if private
                            if (!method.isAccessible()) {
                                method.setAccessible(true);
                            }
                            method.invoke(object);
                        } catch (IllegalAccessException e) {
                            Log.e(TAG, "runDefaultMethod:IllegalAccessException", e);
                        } catch (InvocationTargetException e) {
                            Log.e(TAG, "runDefaultMethod:InvocationTargetException", e);
                        }
                    }
                }
            }

            clazz = clazz.getSuperclass();
        }
    }

    /**
     * Determine if the project is using the AndroidAnnotations library.
     */
    private static boolean isUsingAndroidAnnotations(@NonNull Object object) {
        if (!object.getClass().getSimpleName().endsWith("_")) {
            return false;
        }
        try {
            Class clazz = Class.forName("org.androidannotations.api.view.HasViews");
            return clazz.isInstance(object);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Check if at least one permission in the list of denied permissions has been permanently
     * denied (user clicked "Never ask again").
     *
     * @param host              context requesting permissions.
     * @param deniedPermissions list of denied permissions, usually from {@link
     *                          IPermissionCallbacks#onPermissionsDenied(int, List)}
     * @return {@code true} if at least one permission in the list was permanently denied.
     */
    public static boolean somePermissionPermanentlyDenied(@NonNull Activity host,
                                                          @NonNull List<String> deniedPermissions) {
        return PermissionHelper.newInstance(host)
                .somePermissionPermanentlyDenied(deniedPermissions);
    }

    /**
     * @see #somePermissionPermanentlyDenied(Activity, List)
     */
    public static boolean somePermissionPermanentlyDenied(@NonNull Fragment host,
                                                          @NonNull List<String> deniedPermissions) {
        return PermissionHelper.newInstance(host)
                .somePermissionPermanentlyDenied(deniedPermissions);
    }

    /**
     * @see #somePermissionPermanentlyDenied(Activity, List).
     */
    public static boolean somePermissionPermanentlyDenied(@NonNull android.app.Fragment host,
                                                          @NonNull List<String> deniedPermissions) {
        return PermissionHelper.newInstance(host)
                .somePermissionPermanentlyDenied(deniedPermissions);
    }

    //-------------------------------------对外具体调用及实现类------------------------------------------------
    private PermissionsUtil(Context context)
    {
        this.mContext=context;
    }
    public static PermissionsUtil with(Activity activity)
    {
        return new PermissionsUtil(activity).setPermissionsReqeust(PermissionRequest.with(activity));
    }

    public static PermissionsUtil with(Fragment fragment)
    {
        return new PermissionsUtil(fragment.getContext()).setPermissionsReqeust(PermissionRequest.with(fragment));
    }

    public static PermissionsUtil with(android.app.Fragment fragment)
    {
        return new PermissionsUtil(fragment.getActivity()).setPermissionsReqeust(PermissionRequest.with(fragment));
    }

    private PermissionsUtil setPermissionsReqeust(PermissionRequest reqeust)
    {
        this.mRequest=reqeust;
        return this;
    }

    /**
     * 权限组
     * @param permissions {@link Permission}
     * @return
     */
    public PermissionsUtil permissions(String[] ...permissions)
    {//暂时存储权限申请
        for (String[] temp:permissions) {
            mPermissions.addAll(Arrays.asList(temp));
        }
        return this;
    }

    /**
     * 单个权限
     * @param permissions {@link Permission}
     * @return
     */
    public PermissionsUtil permissions(String ...permissions)
    {//暂时存储权限申请
        mPermissions.addAll(Arrays.asList(permissions));
        return this;
    }

    /**
     * 自定义权限被拒绝后，第二申请时的弹框权限说明
     * @param rationale
     * @return
     */
    public PermissionsUtil setRationale(RationaleDialogConfig config,IRationale rationale)
    {
        this.mRationaleDialogConfig=config;
        this.mIRationale=rationale;
        return this;
    }

    /**
     * @param callbacks 权限申请结果回调
     * @return
     */
    public PermissionsUtil permissionCallback(IPermissionCallbacks callbacks)
    {
        this.mPermissionsCallbacks=callbacks;
        return this;
    }

    /**
     * @param callbacks 二次申请弹框按钮的回调（也可在自定义rationale的时候设置，若使用默认也可回调，后续操作用户要自己做）
     * @return
     */
    public PermissionsUtil rationaleCallback(IRationaleCallbacks callbacks)
    {
        this.mRationaleCallbacks=callbacks;
        return this;
    }

    public void request()
    {
        requestByCode(9999);
    }
    /**
     * @param requestcode 默认9999
     * @return
     */
    public void requestByCode(int requestcode)
    {
        this.mRequestCode =requestcode;
        mRequest.setRequestCode(mRequestCode).setPermissions(mPermissions);
        if (mRationaleDialogConfig==null)
        {
            mRationaleDialogConfig=new RationaleDialogConfig();
            mRationaleDialogConfig.setTitle(mContext.getString(R.string.permissionsutil_title));
            mRationaleDialogConfig.setRationale(mContext.getString(R.string.permissionsutil_rationale));
            mRationaleDialogConfig.setPostive(mContext.getString(R.string.permissionsutil_postive));
            mRationaleDialogConfig.setNegative(mContext.getString(R.string.permissionsutil_negative));

        }

        if (hasPermissions(mContext, mRequest.getPerms())) {
            notifyAlreadyHasPermissions(
                    mRequest.getHelper().getHost(), mRequest.getRequestCode(), mRequest.getPerms());
            return;
        }

        if (mIRationale==null)
        {
            mIRationale=new BaseRationale();
            mIRationale.setRationaleConfig(this.mRationaleDialogConfig).setRationaleCallback(mRationaleCallbacks==null?new IRationaleCallbacks() {
                @Override
                public void onRationaleAccepted(int requestCode) {
                    mRequest.getHelper().directRequestPermissions(mRequestCode,mPermissions);
                }

                @Override
                public void onRationaleDenied(int requestCode) {
                    if (mPermissionsCallbacks!=null)
                    {
                        mPermissionsCallbacks.onPermissionsDenied(mRequestCode, mPermissions);
                    }
                }
            }:mRationaleCallbacks);
        }

        mRequest.getHelper().requestPermissions(mIRationale,mRequestCode,mPermissions);

    }

}
