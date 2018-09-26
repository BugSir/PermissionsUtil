package com.bugsir.permissions.helper;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Permissions helper for {@link Fragment} from the framework.
 */
class FrameworkFragmentPermissionHelper extends PermissionHelper<Fragment> {

    public FrameworkFragmentPermissionHelper(@NonNull Fragment host) {
        super(host);
    }

    @Override
    @SuppressLint("NewApi")
    public void directRequestPermissions(int requestCode, @NonNull List<String> perms) {
        getHost().requestPermissions(perms.toArray(new String[perms.size()]), requestCode);
    }

    @Override
    @SuppressLint("NewApi")
    public boolean shouldShowRequestPermissionRationale(@NonNull String perm) {
        return getHost().shouldShowRequestPermissionRationale(perm);
    }

    @Override
    public Context getContext() {
        return getHost().getActivity();
    }
}
