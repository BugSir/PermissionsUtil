package com.bugsir.permissions.helper;

import android.content.Context;

import java.util.List;


/**
 * @author: BUG SIR
 * @date: 2018/4/28 16:32
 * @description: 自定义权限申请说明提示框接口
 */
public interface IRationale {

    void showRationale(Context context, List<String> strings);
    IRationale setRequestCode(int requestCode);
    IRationale setRationaleConfig(RationaleDialogConfig config);
    IRationale setRationaleCallback(IRationaleCallbacks callback);
}
