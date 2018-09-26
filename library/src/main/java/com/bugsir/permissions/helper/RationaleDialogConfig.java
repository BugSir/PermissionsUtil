package com.bugsir.permissions.helper;

import android.os.Bundle;
import android.support.annotation.StyleRes;

/**
 *@author: Lin Xiongqing
 *@date: 2018/5/2 15:56
 *@description: 被拒绝后第二次申请的权限说明框配置
 */
public    class RationaleDialogConfig   {
    private String mRationale="";
    private String mPostive="";
    private String mNegative="";
    private String mTitle="";

    private int mTheme;
    private Bundle mBundle;

    public String getRationale() {
        return mRationale;
    }

    public void setRationale(String mRationale) {
        this.mRationale = mRationale;
    }

    public String getPostive() {
        return mPostive;
    }

    public void setPostive(String mPostive) {
        this.mPostive = mPostive;
    }

    public String getNegative() {
        return mNegative;
    }

    public void setNegative(String mNegative) {
        this.mNegative = mNegative;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }
    public int getTheme() {
        return mTheme;
    }

    public void setTheme(@StyleRes int theme) {
        this.mTheme = theme;
    }
    public Bundle getBundle() {
        return mBundle;
    }

    public void setBundle(Bundle bundle) {
        this.mBundle = bundle;
    }
}
