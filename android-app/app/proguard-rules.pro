# Keep Proguard rules for Retrofit & Room if needed
-keepattributes *Annotation*
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
