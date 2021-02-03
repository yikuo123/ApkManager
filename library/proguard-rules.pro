# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# ��Ϊlib����ʱ����Ӧ�û������Ƴ������ķ��������ԣ�������Ҳ��ò�Ҫ����

# ������������
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

# ������ͽӿڵķ������������Ƴ�,�ų�BuildConfig
-keep public class !**.BuildConfig, * { public protected *; }
-keep public enum * { public protected *; }
-keep public interface * { *; }

# �������jni�������౻�������Ƴ�������jni�����Ĳ����뷵��ֵ���ͱ�����
-keepclasseswithmembers,includedescriptorclasses class * { native <methods>; }

# �����쳣���Ʊ�����
-keepnames class * extends java.lang.Throwable

# ����������Ʊ�����,��������ʱû��Ч����https://stackoverflow.com/questions/56057586/how-to-keep-class-constructor-argument-parameter-names-with-android-r8
-keepparameternames

-dontwarn org.apache.**
-dontwarn android.**
-dontwarn androidx.**
-dontwarn java.lang.invoke.**
-dontwarn javax.annotation.**
