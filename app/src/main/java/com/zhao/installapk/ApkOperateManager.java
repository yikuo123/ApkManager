package com.zhao.installapk;

import android.content.Context;
import android.net.Uri;
import android.os.RemoteException;
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
    public static void installApkDefaul(Context context, String fileName, String pakcageName, boolean isopen) {
        try {
            ApkManager.installSilently(context, new File(fileName), pakcageName, new ApkManager.MyPackageInstallObserver() {
                @Override
                public void packageInstalled(String packageName, int returnCode) throws RemoteException {
                    super.packageInstalled(packageName, returnCode);
                }
            });
        } catch (Exception e) {
            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //静默卸载
    public static void uninstallApkDefaul(Context context, String packageName) {
        ApkManager.uninstallSilently(context, packageName, new ApkManager.MyPackageDeleteObserver() {
            @Override
            public void packageDeleted(String packageName, int returnCode) {
                super.packageDeleted(packageName, returnCode);
            }
        });
    }
}