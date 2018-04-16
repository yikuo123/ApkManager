package com.ikecin.SystemAppUtils;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.File;
import java.util.Locale;

public class ApkManager {
    private static String TAG = ApkManager.class.getSimpleName();

    /**
     * 普通安装
     *
     * @param context context
     * @param apk     apk文件
     */
    public static void install(Context context, @NonNull File apk) throws Exception {
        checkApkFile(apk);
        install(context, Uri.fromFile(apk));
    }

    /**
     * 普通安装
     *
     * @param context context
     * @param apkUri  apk文件
     */
    @SuppressWarnings("WeakerAccess")
    public static void install(Context context, @NonNull Uri apkUri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 普通卸载
     *
     * @param context     context
     * @param packageName 包名
     */
    public static void uninstall(Context context, @NonNull String packageName) {
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        context.startActivity(intent);
    }

    /**
     * 静默安装
     *
     * @param context context
     * @param apk     apk文件
     */
    public static void installSilently(Context context, @NonNull File apk, @Nullable ApkManagerObserver observer) {
        Log.i(TAG, "Apk 路径：" + apk.getAbsolutePath());

        try {
            checkApkFile(apk);
            installSilently(context, Uri.fromFile(apk), observer);
        } catch (Exception e) {
            if (observer != null) {
                observer.error(e.getLocalizedMessage());
            }
        }
    }

    /**
     * 静默安装
     *
     * @param context context
     * @param apkUri  apk文件
     */
    @SuppressWarnings("WeakerAccess")
    public static void installSilently(Context context, @NonNull Uri apkUri, @Nullable ApkManagerObserver observer) {
        Log.i(TAG, "Apk Uri：" + apkUri.toString());

        int installFlags = 0;
        installFlags |= PackageManager.INSTALL_REPLACE_EXISTING;

        PackageManager pm = context.getPackageManager();

        try {
            //最后一个参数是安装来源的包名，静默卸载时，如果执行卸载操作的包名与安装来源包名不一致，可能无法卸载
            pm.installPackage(apkUri, new IPackageInstallObserver.Stub() {
                @Override
                public void packageInstalled(String packageName, int returnCode) throws RemoteException {
                    Log.i(TAG, String.format("静默安装：package=%s uri=%s returnCode=%d", packageName, apkUri.toString(), returnCode));

                    if (observer == null) {
                        return;
                    }

                    new Handler(Looper.getMainLooper()).post(() -> {
                        //returnCode  1表示成功
                        if (returnCode == PackageManager.INSTALL_SUCCEEDED) {
                            observer.succeed();
                        } else {
                            observer.error(String.format(Locale.getDefault(), "uri=%s returnCode=%d", apkUri.toString(), returnCode));
                        }
                    });
                }
            }, installFlags, context.getPackageName());
        } catch (Exception e) {
            if (observer != null) {
                observer.error(e.getLocalizedMessage());
            }
        }
    }

    /**
     * 静默卸载
     * 注意：如果执行卸载操作的App包名与安装来源包名不一致，可能无法卸载
     *
     * @param context     context
     * @param packageName 包名
     */
    public static void uninstallSilently(Context context, @NonNull String packageName, @Nullable ApkManagerObserver observer) {
        Log.i(TAG, "静默卸载：" + packageName);
        PackageManager pm = context.getPackageManager();

        try {
            pm.deletePackage(packageName, new IPackageDeleteObserver.Stub() {
                @Override
                public void packageDeleted(String packageName, int returnCode) throws RemoteException {
                    Log.i(TAG, String.format("静默卸载：package=%s returnCode=%d", packageName, returnCode));

                    if (observer == null) {
                        return;
                    }

                    new Handler(Looper.getMainLooper()).post(() -> {
                        //returnCode  1表示成功
                        if (returnCode == PackageManager.DELETE_SUCCEEDED) {
                            observer.succeed();
                        } else {
                            observer.error(String.format(Locale.getDefault(), "returnCode=%d", returnCode));
                        }
                    });
                }
            }, 0);
        } catch (Exception e) {
            if (observer != null) {
                observer.error(e.getLocalizedMessage());
            }
        }
    }

    /**
     * 启动目标应用
     *
     * @param context     context
     * @param packageName 包名
     */
    public static void launchApp(Context context, @NonNull String packageName) throws Exception {
        PackageManager packManager = context.getPackageManager();
        Intent resolveIntent = packManager.getLaunchIntentForPackage(packageName);
        if (resolveIntent == null) {
            throw new Exception(packageName + "不存在");
        }
        context.startActivity(resolveIntent);
    }

    private static void checkApkFile(@NonNull File apk) throws Exception {
        if (!apk.exists()) {
            throw new Exception("Apk不存在:" + apk.getAbsolutePath());
        }

        if (!apk.canRead()) {
            throw new Exception("Apk存在但无法读取：" + apk.getAbsolutePath());
        }

        if (!apk.isFile()) {
            throw new Exception("不是Apk文件：" + apk.getAbsolutePath());
        }
    }

    public static String getUid(Context context, @NonNull String packageName) throws PackageManager.NameNotFoundException {
        PackageManager pm = context.getPackageManager();
        ApplicationInfo ai = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
        return Integer.toString(ai.uid, 10);
    }

    /**
     * ApkManager观察者
     */
    public interface ApkManagerObserver {
        void succeed();

        /**
         * @param msg 错误信息
         */
        void error(String msg);

    }
}
