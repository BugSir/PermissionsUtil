package com.bugsir.permissions.helper;

/**
 * @author: BUG SIR
 * @date: 2018/9/25 15:19
 * @description: 权限申请说明弹框的点击事件
 */
public interface IRationaleCallbacks {

    /**
     * 接受建议
     * @param requestCode
     */
    void onRationaleAccepted(int requestCode);


    /**
     * 仍然拒绝
     * @param requestCode
     */
    void onRationaleDenied(int requestCode);
}
