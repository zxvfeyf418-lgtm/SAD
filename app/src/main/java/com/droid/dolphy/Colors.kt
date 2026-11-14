package com.droid.dolphy

import androidx.compose.ui.graphics.Color

// 2N5A brand palette
val DarkBackground = Color(0xFF0E0D12)
val LightBackground = Color(0xFFF7F5FA)
val BrighterSurface = Color(0xFF15141A)
val OrangeAccent = Color(0xFF8F54E2)
val OrangeBorder = Color(0xFFB58AEF)
val TextWhite = Color(0xFFFFFFFF)
val TextGray = Color(0xFFCFCCD6)
val GreenSuccess = Color(0xFFB9F6CA)

val RedThemeAccent = Color(0xFF36171F)
val GreenThemeAccent = Color(0xFF69F0AE)
val PurpleThemeAccent = Color(0xFF8F54E2)
val CyanThemeAccent = Color(0xFF18FFFF)

val SplashGradientStart = Color(0xFF0E0D12)
val SplashGradientEnd = Color(0xFF15141A)
val SplashAccentGradient = Color(0xFF8F54E2)

val OnboardingGradientStart = Color(0xFF0E0D12)
val OnboardingGradientMiddle = Color(0xFF15141A)
val OnboardingGradientEnd = Color(0xFF36171F)
val OnboardingAccent = Color(0xFF8F54E2)
val OnboardingHighlight = Color(0xFFE02ACB)

val CardBackgroundDark = Color(0xFF15141A)
val CardBackgroundLight = Color(0xFFECE8F2)
val SurfaceElevated = Color(0xFF201D27)

val OrangeGradientStart = Color(0xFF6D35B7)
val OrangeGradientMiddle = Color(0xFF8F54E2)
val OrangeGradientEnd = Color(0xFFE02ACB)
val OrangeGradientDeep = Color(0xFF36171F)

val GradientDarkStart = Color(0xFF0E0D12)
val GradientDarkMiddle = Color(0xFF15141A)
val GradientDarkEnd = Color(0xFF201D27)

val SurfaceVolumetricDark = Color(0xFF15141A)
val SurfaceVolumetricLight = Color(0xFFECE8F2)
val SurfaceHighlight = Color(0xFF30283A)

val ShadowDark = Color(0xFF000000)
val ShadowLight = Color(0xFF1A1A1A)

val GlowAccent = Color(0x668F54E2)
val GlowAccentIntense = Color(0x99E02ACB)
val GlowHighlight = Color(0x80FFFFFF)

val CardBorderDark = Color(0xFF30283A)
val CardBorderLight = Color(0xFFD7CDE4)

val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFB8B1C2)
val TextTertiary = Color(0xFF888192)

fun contrastingContentColor(color: Color): Color {
    val luminance = 0.299f * color.red + 0.587f * color.green + 0.114f * color.blue
    return if (luminance > 0.56f) Color(0xFF18120D) else Color.White
}

fun getGradientColors(accentColor: Color): List<Color> = when (accentColor) {
    OrangeAccent, PurpleThemeAccent -> listOf(OrangeGradientStart, OrangeGradientMiddle, OrangeGradientEnd)
    RedThemeAccent -> listOf(Color(0xFF241016), Color(0xFF36171F), Color(0xFF6A2638))
    GreenThemeAccent -> listOf(Color(0xFF00FF00), Color(0xFF00CC00), Color(0xFF66FF66))
    CyanThemeAccent -> listOf(Color(0xFF00DDFF), Color(0xFF00AACC), Color(0xFF66EEFF))
    else -> listOf(accentColor, accentColor.copy(alpha = 0.7f), accentColor.copy(alpha = 0.9f))
}

fun getGlowColor(accentColor: Color): Color = when (accentColor) {
    OrangeAccent, PurpleThemeAccent -> Color(0x668F54E2)
    RedThemeAccent -> Color(0x6036171F)
    GreenThemeAccent -> Color(0x6000DD44)
    CyanThemeAccent -> Color(0x6000DDFF)
    else -> Color(0x40FFFFFF)
}

fun getSplashGradientColors(accentColor: Color): List<Color> = when (accentColor) {
    OrangeAccent, PurpleThemeAccent -> listOf(Color(0xFF0E0D12), Color(0xFF20152D), Color(0xFF36171F), Color(0xFF15141A))
    RedThemeAccent -> listOf(Color(0xFF1A0000), Color(0xFF2D0000), Color(0xFF1B263B), Color(0xFF2D0000))
    GreenThemeAccent -> listOf(Color(0xFF001A00), Color(0xFF002D00), Color(0xFF1B263B), Color(0xFF002D00))
    CyanThemeAccent -> listOf(Color(0xFF001A1A), Color(0xFF002D2D), Color(0xFF1B263B), Color(0xFF002D2D))
    else -> listOf(SplashGradientStart, SplashGradientEnd, SplashGradientStart.copy(alpha = 0.8f))
}
