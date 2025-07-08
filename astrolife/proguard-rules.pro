-repackageclasses 'free'
-printconfiguration proguard-merged-config.txt
-flattenpackagehierarchy ''
-obfuscationdictionary "D:\android\GitHub\exe.txt"
-classobfuscationdictionary "D:\android\GitHub\facebook\proguard\examples\dictionaries\compact.txt"
-packageobfuscationdictionary "D:\android\GitHub\facebook\proguard\examples\dictionaries\windows.txt"

-keep class ai.horo.astrolife.free.gethoroscope.horoscope.beans.* { *; }
-keepclassmembers class ai.horo.astrolife.free.gethoroscope.horoscope.beans.*

-dontwarn android.media.LoudnessCodecController$OnLoudnessCodecUpdateListener
-dontwarn android.media.LoudnessCodecController

-repackageclasses 'a'