package com.example.apkManager;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ikecin.SystemAppUtils.ApkManager;

import java.io.File;

public class MainActivity extends Activity {

    private final String mApkDir = Environment.getExternalStorageDirectory() + File.separator + "Download" + File.separator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editTextPackageName = findViewById(R.id.packageName);
        EditText editTextApkName = findViewById(R.id.apkName);

        ((TextView) findViewById(R.id.textUID)).setText("UID=" + getUid());

        findViewById(R.id.btnInstall).setOnClickListener(v -> {
            String apkName = editTextApkName.getText().toString();
            install(mApkDir + apkName);
        });

        findViewById(R.id.btnUninstall).setOnClickListener(v -> {
            String packageName = editTextPackageName.getText().toString();
            uninstall(packageName);
        });

        findViewById(R.id.btnInstallSilently).setOnClickListener(v -> {
            String apkName = editTextApkName.getText().toString();
            installSilently(mApkDir + apkName);
        });

        findViewById(R.id.btnUinstallSilently).setOnClickListener(v -> {
            String packageName = editTextPackageName.getText().toString();
            uninstallSilently(packageName);
        });

        findViewById(R.id.btnLaunch).setOnClickListener(v -> {
            String packageName = editTextPackageName.getText().toString();
            launchApp(packageName);
        });
    }

    //安装apk
    public void install(String filePath) {
        try {
            ApkManager.install(getApplicationContext(), new File(filePath));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "安装失败：" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //卸载apk
    public void uninstall(String packageName) {
        ApkManager.uninstall(getApplicationContext(), packageName);
    }

    //静默安装
    public void installSilently(String filePath) {
        ApkManager.installSilently(getApplicationContext(), new File(filePath), new ApkManager.ApkManagerObserver() {
            @Override
            public void error(String msg) {
                Toast.makeText(getApplicationContext(), "安装失败：" + filePath + " " + msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void succeed() {
                Toast.makeText(getApplicationContext(), "安装成功：" + filePath, Toast.LENGTH_LONG).show();
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
        try {
            ApkManager.launchApp(getApplicationContext(), packageName);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "启动失败：" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public String getUid() {
        try {
            return ApkManager.getUid(this, BuildConfig.APPLICATION_ID);
        } catch (PackageManager.NameNotFoundException e) {
            return "包名错误";
        }
    }
}