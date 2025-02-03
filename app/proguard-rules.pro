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
# Keep Dagger-Hilt generated components
-keep class dagger.hilt.** { *; }

# Keep Hilt-generated classes and interfaces
-keep class * extends dagger.hilt.internal.GeneratedComponent { *; }

# Keep Hilt annotations (prevents them from being stripped)
-keepattributes *Annotation*

# Keep classes annotated with Hilt annotations
-keep @dagger.hilt.android.* class * { *; }
-keep @dagger.hilt.* class * { *; }

# Keep Dagger/Hilt internal code and prevent obfuscation
-keep class dagger.** { *; }
-keep class javax.inject.** { *; }
-keep class kotlin.Metadata { *; }

# Prevent shrinking of Hilt-related generated code
-keepnames class * {
    @dagger.hilt.android.internal.managers.** *;
}

# Keep Hilt's generated activity components
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

# Keep Hilt's ServiceComponent and WorkerComponent
-keep class * extends dagger.hilt.android.components.ServiceComponent { *; }

# Preserve Hilt entry points
-keep @dagger.hilt.EntryPoint class * { *; }
-keep @dagger.hilt.InstallIn class * { *; }

# Keep ViewModel annotated with @HiltViewModel
-keep @dagger.hilt.android.lifecycle.HiltViewModel class * { *; }
# If you have more data classes in this package, you can generalize the rule:
-keep class com.example.cleanarchitectureproject.data.remote.dto.coinmarket.** { *; }
-keep class com.example.cleanarchitectureproject.domain.model.** { *; }

# Keep Immutable annotation if you use androidx.compose.runtime.Immutable
-keep @androidx.compose.runtime.Immutable class * { *; }
# Keep the CoinMarketApi interface and its methods
-keep interface com.example.cleanarchitectureproject.data.remote.CoinMarketApi { *; }

# Keep Retrofit annotations (like @GET, @POST, etc.)
-keep @retrofit2.http.* class * { *; }

# Keep all Retrofit interfaces and methods
-keepclassmembers,allowobfuscation class * {
    @retrofit2.http.* <methods>;
}
