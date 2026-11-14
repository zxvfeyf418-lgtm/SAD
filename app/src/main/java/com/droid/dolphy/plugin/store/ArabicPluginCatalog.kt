package com.droid.dolphy.plugin.store

/**
 * Short Arabic labels for the built-in public plugin catalog.
 * Keep technical identifiers in manifests and file names unchanged.
 */
internal data class ArabicPluginCopy(
    val title: String,
    val description: String,
)

internal object ArabicPluginCatalog {
    private val entries = mapOf(
        "anti_device_detector.dolphyplugin" to ArabicPluginCopy("تقليل ظهور الجهاز", "تقليل ظهور الهاتف للأجهزة القريبة عبر إيقاف اتصالات مثل البلوتوث والواي فاي والاتصال قريب المدى والموقع."),
        "battery_level_changer_gps_spoofer.dolphyplugin" to ArabicPluginCopy("تغيير الموقع ونسبة البطارية", "تغيير الموقع الظاهر ونسبة شحن البطارية المعروضة باستخدام شيزوكو. قد لا يعمل تغيير الموقع مع بعض التطبيقات."),
        "battery_visual.dolphyplugin" to ArabicPluginCopy("تغيير نسبة البطارية المعروضة", "عرض نسبة بطارية تختارها من صفر إلى مئة بالمئة ثم الرجوع إلى النسبة الحقيقية. يحتاج إلى شيزوكو."),
        "ble_guard.dolphyplugin" to ArabicPluginCopy("الحماية من إزعاج البلوتوث", "تعديل إعدادات البحث عن أجهزة البلوتوث القريبة للمساعدة في تقليل رسائل البلوتوث المزعجة."),
        "ble_radar_locator.dolphyplugin" to ArabicPluginCopy("رادار أجهزة البلوتوث", "عرض الأجهزة القريبة على شكل رادار مع معلوماتها وإمكانية الفرز والتصفية وحفظ النتائج."),
        "ble_scanner_dex_md3e.dolphyplugin" to ArabicPluginCopy("فحص أجهزة البلوتوث القريبة", "البحث عن أجهزة البلوتوث منخفض الطاقة وعرض معلومات تفصيلية عن الأجهزة المكتشفة."),
        "bt_pwn_lab.dolphyplugin" to ArabicPluginCopy("مختبر فحص البلوتوث", "أدوات لفحص البلوتوث وتجربة أنماط الإرسال على جهازك. يتطلب أندرويد 13 وصلاحيات الجذر ووحدة البلوتوث الإضافية."),
        "cortex.dolphyplugin" to ArabicPluginCopy("تنسيق النصوص", "تحويل النص إلى تسعة أشكال زخرفية مع إمكانية نسخ النتيجة ومسح النص."),
        "dolphy_performance_booster.dolphyplugin" to ArabicPluginCopy("تجربة تحسين أداء التطبيق", "إضافة تجريبية لمحاولة تحسين أداء دولفي. النتائج غير مضمونة."),
        "droidmonitor.dolphyplugin" to ArabicPluginCopy("مراقبة حالة الهاتف", "عرض معلومات المعالج والذاكرة والتخزين والبطارية والشبكة مع سجل للمساعدة في معرفة الأخطاء."),
        "fake_battery.dolphyplugin" to ArabicPluginCopy("تغيير نسبة البطارية الظاهرة", "تغيير نسبة شحن البطارية التي تظهر على الشاشة بشكل تجريبي."),
        "fast_wifi_bruteforce.dolphyplugin" to ArabicPluginCopy("اختبار كلمات مرور الواي فاي", "أداة تدقيق للشبكات المصرح لك بفحصها باستخدام قائمة كلمات مرور. تتطلب شيزوكو وأندرويد 11 أو أحدث."),
        "hardware_status.dolphyplugin" to ArabicPluginCopy("فحص إمكانات الهاتف", "فحص عمل مستشعرات وتقنيات الهاتف مثل الاتصال قريب المدى والبلوتوث والأشعة تحت الحمراء ونظام تحديد الموقع."),
        "icloud_spoof.dolphyplugin" to ArabicPluginCopy("اختبار بث البلوتوث لأجهزة آيفون", "أداة اختبار لإشارات البلوتوث منخفض الطاقة المتعلقة بخدمات الأجهزة القريبة. استخدمها على أجهزتك وبإذن أصحابها."),
        "ir_transceiver.dolphyplugin" to ArabicPluginCopy("إرسال وتسجيل إشارات التحكم", "إرسال إشارات الأشعة تحت الحمراء وتسجيلها عبر ملحقات USB أو منفذ الصوت مع دعم ملفات أجهزة التحكم."),
        "module_checker.dolphyplugin" to ArabicPluginCopy("فحص وظائف الهاتف", "معرفة التقنيات والوظائف المتوفرة في هاتفك واختبار عملها."),
        "morse_code.dolphyplugin" to ArabicPluginCopy("إرسال رسائل مورس", "تحويل النص إلى شفرة مورس وإظهارها أو إرسالها بالصوت والاهتزاز."),
        "network_audit_pro.dolphyplugin" to ArabicPluginCopy("فحص إعدادات جهاز الشبكة", "فحص إعدادات جهاز التوجيه والخدمات المتاحة وتسجيل النتائج لأغراض تدقيق شبكتك."),
        "signal_jammer_root_v2.dolphyplugin" to ArabicPluginCopy("اختبار إشارات الاتصال", "أداة اختبار لتداخل إشارات البلوتوث والواي فاي والأشعة تحت الحمراء. تتطلب صلاحيات الجذر. استخدمها ضمن بيئة تملكها ومصرح لك باختبارها."),
        "smart_terminal.dolphyplugin" to ArabicPluginCopy("تنفيذ أوامر الهاتف", "تشغيل أوامر أندرويد وإدارة العمليات من داخل التطبيق باستخدام صلاحيات الجذر أو شيزوكو."),
        "tv5a_remote.dolphyplugin" to ArabicPluginCopy("جهاز تحكم للتلفاز", "التحكم بجهاز INEXT TV5A عبر الأشعة تحت الحمراء. يحتاج الهاتف إلى مرسل أشعة تحت الحمراء."),
        "usb_c_build_in_ir.dolphyplugin" to ArabicPluginCopy("التحكم بالأشعة تحت الحمراء", "إرسال إشارات الأشعة تحت الحمراء باستخدام المرسل المدمج أو ملحق متوافق."),
        "usb_c_ir_backend.dolphyplugin" to ArabicPluginCopy("تشغيل مرسل الأشعة عبر USB", "إرسال إشارات الأشعة تحت الحمراء باستخدام ملحق خارجي يتصل بمنفذ USB-C."),
        "usb_c_ir_combo_tx_rx.dolphyplugin" to ArabicPluginCopy("إرسال واستقبال إشارات الأشعة", "تسجيل إشارات الأشعة تحت الحمراء وإرسالها باستخدام ملحق USB-C واحد."),
        "websiteme.dolphyplugin" to ArabicPluginCopy("فتح المواقع والخدمات", "الوصول السريع إلى المواقع والخدمات المضافة مع دعم تسجيل الدخول والخروج."),
        "wi_fi_info.dolphyplugin" to ArabicPluginCopy("عرض معلومات شبكات الواي فاي", "البحث عن شبكات الواي فاي القريبة وعرض اسم الشبكة وقوة الإشارة والقناة ونوع الحماية ومعلومات الاتصال."),
        "wi_fi_wordlist_lab.dolphyplugin" to ArabicPluginCopy("إنشاء قوائم اختبار الشبكة", "إنشاء قوائم كلمات مرور تجريبية لاستخدامها في تدقيق شبكة تملكها أو لديك إذن بفحصها."),
        "wifi_stresser.dolphyplugin" to ArabicPluginCopy("اختبار تحمّل الشبكة", "قياس قدرة شبكة تملكها على تحمّل ضغط الاتصال ومراقبة الاستجابة وسجل الأخطاء."),
        "metalloiskatel.dolphyplugin" to ArabicPluginCopy("كاشف المعادن", "استخدام مستشعر المجال المغناطيسي في الهاتف للبحث عن المعادن مع المعايرة والتنبيه بالصوت والاهتزاز."),
        "metalloiskatel_pro.dolphyplugin" to ArabicPluginCopy("كاشف معادن متقدم", "البحث عن المعادن باستخدام مستشعر الهاتف مع المعايرة والتنبيه بالصوت والاهتزاز."),
        "stroitelnyy_uroven_bubble_level.dolphyplugin" to ArabicPluginCopy("ميزان استواء رقمي", "قياس ميلان السطح باستخدام الهاتف مع أوضاع للأرض والجدار والمنحدر وحفظ القياسات."),
    )

    fun find(file: String): ArabicPluginCopy? =
        entries[file.substringAfterLast('/').lowercase()]
}
