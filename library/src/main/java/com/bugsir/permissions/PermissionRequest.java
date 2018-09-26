package com.bugsir.permissions;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.Size;
import android.support.v4.app.Fragment;

import com.bugsir.permissions.helper.PermissionHelper;

import java.util.List;


/**
 * An immutable model object that holds all of the parameters associated with a permission request,
 * such as the permissions, request code, and rationale.
 */
public final class PermissionRequest {
    private  PermissionHelper mHelper;
    private  List<String> mPerms;
    private  int mRequestCode;

    public static PermissionRequest with(@NonNull Activity activity)
    {
        return new PermissionRequest().setPermissionHelper(PermissionHelper.newInstance(activity));
    }

    public static PermissionRequest with(@NonNull Fragment fragment)
    {
        return new PermissionRequest().setPermissionHelper(PermissionHelper.newInstance(fragment));
    }

    public static PermissionRequest with(android.app.Fragment fragment)
    {
        return new PermissionRequest().setPermissionHelper(PermissionHelper.newInstance(fragment));
    }

    private PermissionRequest setPermissionHelper(PermissionHelper helper)
    {
        this.mHelper=helper;
        return this;
    }

    public PermissionRequest setRequestCode(int reqeustCode)
    {
        this.mRequestCode=reqeustCode;
        return this;
    }

    public PermissionRequest setPermissions( @NonNull @Size(min = 1) List<String> perms)
    {
        this.mPerms=perms;
        return this;
    }


    @NonNull
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public PermissionHelper getHelper() {
        return mHelper;
    }

    @NonNull
    public List<String> getPerms() {
        return mPerms;
    }

    public int getRequestCode() {
        return mRequestCode;
    }
}
