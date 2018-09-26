package com.bugsir.permissions.helper;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import java.util.List;

/**
 * Permissions helper for {@link Activity}.
 */
class ActivityPermissionHelper extends PermissionHelper<Activity> {

    public ActivityPermissionHelper(Activity host) {
        super(host);
    }

    @Override
    public void directRequestPermissions(int requestCode, @NonNull List<String> perms) {
        ActivityCompat.requestPermissions(getHost(), perms.toArray(new String[perms.size()]), requestCode);
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String perm) {
        return ActivityCompat.shouldShowRequestPermissionRationale(getHost(), perm);
    }

    @Override
    public Context getContext() {
        return getHost();
    }
}
