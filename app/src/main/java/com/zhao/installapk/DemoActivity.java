package com.zhao.installapk;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.ikecin.SystemAppUtils.ApkManager;

import java.io.File;

/**
 * @author zhao
 * 测试Activity
 */
public class DemoActivity extends Activity {

    private String filepath = Environment.getExternalStorageDirectory() + File.separator + "Download" + File.separator;

    private String filename = "install.apk";

    private String packagename = "com.zhao.aiddevicetest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //安装apk
    public void install(String filePath) {
        try {
            ApkManager.install(getApplicationContext(), new File(filePath));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "安装失败：" + filePath, Toast.LENGTH_LONG).show();
        }
    }

    //卸载apk
    public void uninstall(String packageName) {
        ApkManager.uninstall(getApplicationContext(), packageName);
    }

    //静默安装
    public void installSilently(String filePath, String packageName) {
        ApkManager.installSilently(getApplicationContext(), new File(filePath), packageName, new ApkManager.ApkManagerObserver() {
            @Override
            public void error(String msg) {
                Toast.makeText(getApplicationContext(), "安装失败：" + packageName + " " + msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void succeed() {
                Toast.makeText(getApplicationContext(), "安装成功：" + packageName, Toast.LENGTH_LONG).show();
            }
        });
    }

    //静默卸载
    public void uninstallSilently(String packageName) {
        ApkManager.uninstallSilently(getApplicationContext(), packageName, new ApkManager.ApkManagerObserver() {
            @Override
            public void error(String msg) {
                Toast.makeText(getApplicationContext(), "卸载失败：" + packageName + " " + msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void succeed() {
                Toast.makeText(getApplicationContext(), "卸载成功：" + packageName, Toast.LENGTH_LONG).show();
            }
        });
    }

    //启动应用
    public void launchApp(String packageName) {
        ApkManager.launchPackage(getApplicationContext(), packageName);
    }
}