# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/ld/Library/Android/sdk/tools/proguard/proguard-android.txt
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


##指定代码的压缩级别
#-optimizationpasses 5
#
##不去忽略非公共的库类
#-dontskipnonpubliclibraryclasses
#
# #优化  不优化输入的类文件
#-dontoptimize
#
# #预校验
#-dontpreverify
#
#-dontwarn io.rong.push.**
#
##gson
##如果用用到Gson解析包的，直接添加下面这几行就能成功混淆，不然会报错。
#-keepattributes Signature
## Gson specific classes
#-keep class sun.misc.Unsafe { *; }
## Application classes that will be serialized/deserialized over Gson
#-keep class com.google.gson.** { *; }
#-keep class com.google.gson.stream.** { *; }
#
##mob
#-keep class android.net.http.SslError
#-keep class android.webkit.**{*;}
#-keep class cn.sharesdk.**{*;}
#-keep class com.sina.**{*;}
#-keep class m.framework.**{*;}
#-keep class **.R$* {*;}
#-keep class **.R{*;}
#-dontwarn cn.sharesdk.**
#-dontwarn **.R$*
#
##butterknife
#-keep class butterknife.** { *; }
#-dontwarn butterknife.internal.**
#-keep class **$$ViewBinder { *; }
#
#-keepclasseswithmembernames class * {
#    @butterknife.* <fields>;
#}
#
#-keepclasseswithmembernames class * {
#    @butterknife.* <methods>;
#}
#
## 如果使用了Gson之类的工具要使被它解析的JavaBean类即实体类不被混淆。
#-keep class com.matrix.app.entity.json.** { *; }
#-keep class com.matrix.appsdk.network.model.** { *; }
#
######混淆保护自己项目的部分代码以及引用的第三方jar包library#######
##如果在当前的application module或者依赖的library module中使用了第三方的库，并不需要显式添加规则
##-libraryjars xxx
##添加了反而有可能在打包的时候遭遇同一个jar多次被指定的错误，一般只需要添加忽略警告和保持某些class不被混淆的声明。
##以libaray的形式引用了开源项目,如果不想混淆 keep 掉，在引入的module的build.gradle中设置minifyEnabled=false
#-keep class com.nineoldandroids.** { *; }
#-keep interface com.nineoldandroids.** { *; }
#-dontwarn com.nineoldandroids.**
#-keepattributes Exceptions,InnerClasses
#
#-keepattributes Signature
#
## RongCloud SDK
#-keep class io.rong.** {*;}
#-keep class * implements io.rong.imlib.model.MessageContent {*;}
#-dontwarn io.rong.push.**
#-dontnote com.xiaomi.**
#-dontnote com.google.android.gms.gcm.**
#-dontnote io.rong.**
#
## VoIP
#-keep class io.agora.rtc.** {*;}
#
## Location
#-keep class com.amap.api.**{*;}
#-keep class com.amap.api.services.**{*;}
#
## 红包
#-keep class com.google.gson.** { *; }
#-keep class com.uuhelper.Application.** {*;}
#-keep class net.sourceforge.zbar.** { *; }
#-keep class com.google.android.gms.** { *; }
#-keep class com.alipay.** {*;}
#-keep class com.jrmf360.rylib.** {*;}
#
#-ignorewarnings
#-keep class io.rong.app.DemoNotificationReceiver {*;}