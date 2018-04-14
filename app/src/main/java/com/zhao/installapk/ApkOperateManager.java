package com.zhao.installapk;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.ikecin.SystemAppUtils.ApkManager;

import java.io.File;


public class ApkOperateManager {

    //安装apk
    public static void installApk(Context context, String fileName) {
        ApkManager.install(context, Uri.parse("file://" + fileName));
    }

    //卸载apk
    public static void uninstallApk(Context context, String packageName) {
        ApkManager.uninstall(context, packageName);
    }

    //静默安装
    public static void installApkDefault(Context context, String fileName, String packageName) {
        ApkManager.installSilently(context, new File(fileName), packageName, new ApkManager.InstallObserver() {
            @Override
            public void error(String msg) {
                Toast.makeText(context, "安装失败：" + packageName + " " + msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void succeed() {
                Toast.makeText(context, "安装成功：" + packageName, Toast.LENGTH_LONG).show();
            }
        });
    }

    //静默卸载
    public static void uninstallApkDefault(Context context, String packageName) {
        ApkManager.uninstallSilently(context, packageName, new ApkManager.DeleteObserver() {
            @Override
            public void error(String msg) {
                Toast.makeText(context, "卸载失败：" + packageName + " " + msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void succeed() {
                Toast.makeText(context, "卸载成功：" + packageName, Toast.LENGTH_LONG).show();
            }
        });
    }
}