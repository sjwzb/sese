# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\androidDevelop\sdk\Sdk/tools/proguard/proguard-android.txt
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
-keep public class * extends android.app.Service
-keep class com.joyme.recordlib.CameraCallbackManager {
    public <methods>;
    static <methods>;
}
-keep class com.joyme.recordlib.UpdateBitmapCallback{
    public <methods>;
}
-keep class com.joyme.unitydemo.JMRecorder {
    public <methods>;
    static <methods>;
}
-keep interface com.joyme.unitydemo.OnStrartRecordListener {
    *;
}
-keep class com.joyme.recordlib.media.MediaConfig {
   public *;
}
