regeX
=====
Android app trying to explain regular expressions with some (fun?) exercises to check your regex knowledge.
=====
The source code can be, if you haven't already, found at GitHub (https://github.com/crocy/regeX) or pulled from a Git repository at: https://github.com/crocy/regeX.git

The project has been coded with Android Studio 0.4.0 using Gradle (wrapper) version 1.9 and Android-Gradle plugin 'com.android.tools.build:gradle:0.7.3'

To run the app either import it into Android Studio and hit Run or go to the root of the project with your console and:

- run "gradlew.bat assembleDebug" (or just "gradlew aD" for short) to assemble/build the apk that you can manually install via ADB or

- run "gradlew.bat installDebug" (or just "gradlew iD" for short) to assemble/build the apk and install it to the one connected Android device

- you can also run "gradlew.bat tasks" to se what Gradle tasks can be run with "gradlew.bat" (on Windows) or "gradlew" (on other platforms)

- on non-Windows platforms run "./gradlew" instead of "gradlew.bat"