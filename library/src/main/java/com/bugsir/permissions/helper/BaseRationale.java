package com.bugsir.permissions.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.util.List;

/**
 *@author: BUG SIR
 *@date: 2018/4/28 16:56
 *@description:
 */
public    class BaseRationale implements IRationale {
    private IRationaleCallbacks mCallbacks;
    private RationaleDialogConfig mConfig;
    private int requestCode;

    @Override
    public IRationale setRequestCode(int requestCode) {
         this.requestCode=requestCode;
         return this;
    }

    @Override
    public IRationale setRationaleCallback(IRationaleCallbacks callback) {
        this.mCallbacks=callback;
        return this;
    }

    @Override
    public void showRationale(Context context, List<String> permissions) {
        new AlertDialog.Builder(context).setCancelable(false).setTitle(mConfig.getTitle()).setMessage(mConfig.getRationale()).setPositiveButton(mConfig.getPostive(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (mCallbacks!=null)
                {
                    mCallbacks.onRationaleAccepted(requestCode);
                }
            }
        }).setNegativeButton(mConfig.getNegative(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (mCallbacks!=null)
                {
                    mCallbacks.onRationaleDenied(requestCode);
                }
            }
        }).create().show();

    }

    @Override
    public IRationale setRationaleConfig(RationaleDialogConfig config) {
        this.mConfig=config;
        return this;
    }
}
