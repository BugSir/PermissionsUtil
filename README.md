# PermissionsUtil
[![](https://www.jitpack.io/v/BugSir/PermissionsUtil.svg)](https://www.jitpack.io/#BugSir/PermissionsUtil)

基于[EasyPermissions](https://github.com/googlesamples/easypermissions) 1.2的进行修改，并增加自定义询问框接口Rationale,只需要要baseactivity/basefragment定义好相关接口即可

# 引用方法:<br/>
<pre><code>
工程目录gradle
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
项目gradle
	dependencies {
	        implementation 'com.github.BugSir:PermissionsUtil:v1.0.0'
	}
</code></pre>
# 使用：<br/>
### 某个权限申请：
``` java
PermissionsUtil.with(this)
.permissions(Permission.CAMERA)
.permissionCallback(this)
.requestByCode(RC_CAMERA_PERM);
```
### 整组权限申请：
``` java
PermissionsUtil.with(this)
.permissions(Permission.Group.LOCATION,Permission.Group.CONTACTS)
.permissionCallback(this)
.requestByCode(RC_LOCATION_CONTACTS_PERM);
```
### 混合权限申请：
```java
PermissionsUtil.with(this)
.permissions(Permission.CAMERA)
.permissions(Permission.Group.LOCATION,Permission.Group.CONTACTS)
.permissionCallback(this)
.requestByCode(RC_LOCATION_CONTACTS_PERM);
```
### 权限申请调回：
重写activity/fragment的此方法将回调结果传给PermissionsUtil进行处理
``` java
@Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        PermissionsUtil.onRequestPermissionsResult(requestCode, Arrays.asList(permissions), grantResults, this);
    }
```
### 权限结果调回：
```java
public interface IPermissionCallbacks{

        /**
         * @param requestCode 请求code
         * @param perms 成功权限列表
         */
    void onPermissionsGranted(int requestCode, @NonNull List<String> perms);

        /**
         * @param requestCode 请求code
         * @param perms 拒绝权限列表
         */
    void onPermissionsDenied(int requestCode, @NonNull List<String> perms);
}
```
### 自定义弹框内容
```java 
继承IRationale并实现相关的方法。具体参考BaseRationale的定义。PermissionsUtil使用：
setRationale(RationaleDialogConfig config,IRationale rationale);
其中RationaleDialogConfig是弹框的一些设置文字标题等，IRationale 自定义实现方法（两个参数不是必填）
```
### PS：申请权限组的时候必须在AndroidManifest.xml写上相关的权限，否则会被直接拒绝
