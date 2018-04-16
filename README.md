[![](https://jitpack.io/v/yikuo123/ApkManager.svg)](https://jitpack.io/#yikuo123/ApkManager)

# ApkManager

可以实现静默安装、卸载App

# FAQ

## 静默安装报错

必须满足其中一个条件

* 使用固件对应的system签名，并且在`AndroidManifest.xml`中声明`android:sharedUserId="android.uid.system"`
* 不使用system签名，apk放在`/system/priv-app/`中

## 静默卸载报错

必须满足跟静默安装相同的条件

## 静默卸载无反应

静默安装时会自动指定执行安装代码的的包名作为installer，如果执行卸载操作的App包名与installer不一致，可能会无法卸载

如果通过普通方式安装，也可能无法静默卸载，以为内installer包名跟执行卸载操作的App包名是不一致的

