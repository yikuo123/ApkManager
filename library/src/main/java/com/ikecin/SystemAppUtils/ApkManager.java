package com.ikecin.SystemAppUtils;


import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import java.io.File;

public class ApkManager {
    private static String TAG = ApkManager.class.getSimpleName();

    /**
     * 普通安装
     *
     * @param context    context
     * @param apkFileUri apk文件
     */
    public static void install(Context context, Uri apkFileUri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(apkFileUri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 普通卸载
     *
     * @param context     context
     * @param packageName 包名
     */
    public static void uninstall(Context context, String packageName) {
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        context.startActivity(intent);
    }

    /**
     * 静默安装
     *
     * @param context     context
     * @param apkFileUri  apk文件
     * @param packageName 包名
     * @throws Exception 错误
     */
    public static void installSilently(Context context, Uri apkFileUri, String packageName) throws Exception {
        Log.d(TAG, "开始静默安装：" + apkFileUri.toString() + packageName);
        File file = new File(fileName);

        if (!file.exists()) {
            throw new Exception(apkFileUri.toString() + "文件不存在");
        }

        int installFlags = 0;
        installFlags |= PackageManager.INSTALL_REPLACE_EXISTING;

        PackageManager pm = context.getPackageManager();
        pm.installPackage(Uri.fromFile(file), new MyPackageInstallObserver(), installFlags, packageName);
    }

    /**
     * 静默卸载
     *
     * @param context     context
     * @param packageName 包名
     */
    public static void uninstallSilently(Context context, String packageName) {
        Log.d(TAG, "开始静默卸载：" + packageName);
        PackageManager pm = context.getPackageManager();
        pm.deletePackage(packageName, new MyPackageDeleteObserver(), 0);
    }

    /**
     * 启动目标应用
     *
     * @param context     context
     * @param packageName 包名
     */
    public static void launchPackage(Context context, String packageName) {
        PackageManager packManager = context.getPackageManager();
        Intent resolveIntent = packManager.getLaunchIntentForPackage(packageName);
        context.startActivity(resolveIntent);
    }

    /**
     * 静默安装观察者
     */
    public static class MyPackageInstallObserver extends IPackageInstallObserver.Stub {
        MyPackageInstallObserver() {
        }

        /**
         * @param packageName 包名
         * @param returnCode  1表示成功
         */
        @Override
        public void packageInstalled(String packageName, int returnCode) throws RemoteException {
            Log.d(TAG, "静默安装执行结果：" + packageName + (returnCode == 1 ? "成功" : "失败"));

        }
    }

    /**
     * 静默卸载观察者
     */
    public static class MyPackageDeleteObserver extends IPackageDeleteObserver.Stub {
        MyPackageDeleteObserver() {
        }

        /**
         * @param packageName 包名
         * @param returnCode  1表示成功
         */
        @Override
        public void packageDeleted(String packageName, int returnCode) {
            Log.d(TAG, "静默卸载执行结果：" + packageName + (returnCode == 1 ? "成功" : "失败"));
        }
    }
}
