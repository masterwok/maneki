# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontobfuscate
-dontshrink

-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.SerializationKt
-keep,includedescriptorclasses class com.masterwok.shrimplesearch.**$$serializer { *; }
-keepclassmembers class com.masterwok.shrimplesearch.common.data.models.*.** {
    *** Companion;
}
-keepclasseswithmembers class com.masterwok.shrimplesearch.common.data.models.*.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keepnames class * extends androidx.fragment.app.Fragment
-keepnames class * extends androidx.fragment.app.FragmentActivity
