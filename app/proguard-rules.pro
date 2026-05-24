# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep data model classes (for Firebase serialization when added)
-keep class com.focusguardpro.model.** { *; }

# Keep ViewModel classes
-keep class com.focusguardpro.viewmodel.** { *; }

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# TODO: Uncomment when Firebase is added
# -keep class com.google.firebase.** { *; }
# -keep class com.google.android.gms.** { *; }
