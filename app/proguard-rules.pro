# 1. ROOM AND SQLITE (bắt buộc để tránh crash)
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep class com.example.app_basic.data.** { *; }
-dontwarn androidx.room.**
# 2. JETPACK COMPOSE
-keep class androidx.compose.** { *; }
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}
-keep class kotlin.Metadata { *; }
# 3. NAVIGATION AND SCREEN
-keep class com.example.app_basic.navigation.** { *; }
-keep class com.example.app_basic.screens.** { *; }
-keep class com.example.app_basic.utils.** { *; }
# 4. KOTLIN AND COROUTINES
-keep class kotlin.** { *; }
-keep class kotlinx.** { *; }
-dontwarn kotlin.**
-dontwarn kotlinx.**
# 5. ANDROID COMPONENTS
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
# Giữ các View
-keepclassmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
# 6. BẢO MẬT và GIẢM LOG
# Xóa log khi build release
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

# Obfuscate
-repackageclasses
-overloadaggressively

# 7. ENUMS AND REFLECTION
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Giữ các method có gắn @Keep
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}

