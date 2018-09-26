package com.bugsir.permissions.sample;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.bugsir.permissions.Permission;
import com.bugsir.permissions.PermissionSetting;
import com.bugsir.permissions.PermissionsUtil;
import com.bugsir.permissions.helper.AfterPermissionGranted;
import com.bugsir.permissions.helper.IPermissionCallbacks;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IPermissionCallbacks{
    private static final int RC_CAMERA_PERM = 999;
    private static final int RC_LOCATION_CONTACTS_PERM = 124;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Button click listener that will request one permission.
        findViewById(R.id.button_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraTask();
            }
        });

        // Button click listener that will request two permissions.
        findViewById(R.id.button_location_and_contacts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationAndContactsTask();
            }
        });
    }
    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void cameraTask() {
        if (PermissionsUtil.hasPermissions(this, Permission.CAMERA)) {
            Toast.makeText(this, "TODO: Camera things", Toast.LENGTH_LONG).show();
        } else {
            PermissionsUtil.with(this).permissions(Permission.CAMERA).permissionCallback(this).requestByCode(RC_CAMERA_PERM);
        }
    }

    @AfterPermissionGranted(RC_LOCATION_CONTACTS_PERM)
    public void locationAndContactsTask() {
        if (PermissionsUtil.hasPermissions(this,Permission.Group.LOCATION,Permission.Group.CONTACTS)) {
            Toast.makeText(this, "TODO: Location and Contacts things", Toast.LENGTH_LONG).show();
        } else {
            PermissionsUtil.with(this).permissions(Permission.Group.LOCATION,Permission.Group.CONTACTS).permissionCallback(this).requestByCode(RC_LOCATION_CONTACTS_PERM);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        PermissionsUtil.onRequestPermissionsResult(requestCode, Arrays.asList(permissions), grantResults, this);
    }
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        List<String > text=Permission.transformText(this,perms);
        Toast.makeText(this,text.get(0),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (PermissionsUtil.somePermissionPermanentlyDenied(this, perms)) {
            new AlertDialog.Builder(this).setCancelable(false).setTitle("权限申请提示").setMessage("您拒绝了相关权限申请，请到权限设置界面打开权限").setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    PermissionSetting.go2Setting(MainActivity.this);
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).create().show();

        }
    }
}
