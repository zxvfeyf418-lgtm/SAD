# تعليمات بناء 2N5A في Termux (Ubuntu عبر PRoot)

هذه الخطوات لم تُختبر فعلياً من طرفي (الشبكة هنا تحجب SDK/Gradle)، لكنها الأوامر القياسية
لمشروع Android Gradle Kotlin/Compose بإعدادات هذا المشروع (AGP 8.9.1 · Gradle 8.11.1 ·
Kotlin 2.1.0 · compileSdk/targetSdk 36 · minSdk 26).

## 1. المتطلبات داخل Termux/Ubuntu (PRoot)

```bash
apt update && apt install -y openjdk-21-jdk-headless unzip wget
java -version   # يجب أن يظهر 21.x
```

### Android SDK (سطر أوامر فقط، بدون Android Studio)

```bash
mkdir -p ~/android-sdk/cmdline-tools
cd ~/android-sdk/cmdline-tools
wget https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip -O cmdtools.zip
unzip cmdtools.zip && rm cmdtools.zip
mv cmdline-tools latest

export ANDROID_HOME=~/android-sdk
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools

yes | sdkmanager --licenses
sdkmanager "platform-tools" "platforms;android-36" "build-tools;36.0.0"
```

أضف هذين السطرين إلى `~/.bashrc` حتى لا تكررهما كل مرة:
```bash
echo 'export ANDROID_HOME=~/android-sdk' >> ~/.bashrc
echo 'export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools' >> ~/.bashrc
```

## 2. فك ضغط المشروع والبناء

```bash
cd ~
unzip 2N5A-project.zip -d 2N5A
cd 2N5A/Dolphy-App-2.6
chmod +x gradlew   # إن لم يوجد gradlew لينكس، نفّذ: gradle wrapper --gradle-version 8.11.1

./gradlew --version
./gradlew clean
./gradlew assembleDebug
```

ناتج APK سيكون في:
```
app/build/outputs/apk/debug/app-debug.apk
```

## 3. إن فشل البناء

أرسل لي النص الكامل لخطأ Gradle (من `./gradlew assembleDebug --stacktrace`) وسأصلحه مباشرة.
الأخطاء المتوقعة الأكثر احتمالاً إن حدثت:
- نسيان `import` لملف لم أفحصه يدوياً (راجعتُ كل ملف عدّلته لكن لم أُشغّل مُصَرِّف Kotlin فعلياً).
- عدم توفر `build-tools;36.0.0` بالضبط — جرّب أقرب إصدار متاح إذا رفض sdkmanager هذا الرقم تحديداً.

## 4. اختبار سريع بعد البناء الناجح

```bash
./gradlew lintDebug          # فحص موارد/أخطاء شائعة
./gradlew assembleRelease    # نسخة release (تحتاج توقيعاً لاحقاً لو أردت توزيعها)
```

لتثبيت APK مباشرة إن كان لديك جهاز متصل عبر ADB:
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

## 5. ما يجب التحقق منه يدوياً على الجهاز

- أن التطبيق يفتح باسم **2N5A** وأيقونة جديدة.
- أن الواجهة تظهر بالعربية RTL افتراضياً، وأن تبديل اللغة (روسي/إنجليزي/عربي) من الإعدادات يعمل.
- شاشة الإعدادات → حول: روابط Telegram (@e0a_e) وInstagram (@2n5a) تفتح صحيحة.
- تثبيت أي ملف `.dolphyplugin` من `plugin-examples/` لا يزال يعمل كما في Dolphy الأصلي (لم يتغيّر
  منطق التثبيت، فقط رسائل الأخطاء المعروضة أصبحت عبر موارد قابلة للترجمة).
