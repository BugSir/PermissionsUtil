package com.bugsir.permissions.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.util.List;

/**
 * Permissions helper for {@link Fragment} from the support library.
 */
class SupportFragmentPermissionHelper extends PermissionHelper<Fragment> {

    public SupportFragmentPermissionHelper(@NonNull Fragment host) {
        super(host);
    }

    @Override
    public void directRequestPermissions(int requestCode, @NonNull List<String> perms) {
        getHost().requestPermissions(perms.toArray(new String[perms.size()]), requestCode);
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String perm) {
        return getHost().shouldShowRequestPermissionRationale(perm);
    }

    @Override
    public Context getContext() {
        return getHost().getActivity();
    }
}
