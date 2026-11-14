package com.droid.dolphy.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Use Android's clean system sans-serif for an iOS-inspired appearance.
// SF Pro is Apple's proprietary font and is not bundled or redistributed here.
private val AppFontFamily = FontFamily.SansSerif

fun buildAppTypography(useFlipperFont: Boolean, fontScale: Float): Typography {
    val safeScale = (if (useFlipperFont) fontScale else 1f).coerceIn(0.85f, 1.2f)
    val base = Typography()

    fun style(
        original: androidx.compose.ui.text.TextStyle,
        weight: FontWeight = FontWeight.Normal,
        spacing: Float = 0.1f
    ) = original.copy(
        fontFamily = AppFontFamily,
        fontWeight = weight,
        fontSize = original.fontSize * safeScale,
        letterSpacing = spacing.sp
    )

    return base.copy(
        displayLarge = style(base.displayLarge, FontWeight.Bold, 0.2f),
        displayMedium = style(base.displayMedium, FontWeight.Bold, 0.2f),
        displaySmall = style(base.displaySmall, FontWeight.Bold, 0.2f),
        headlineLarge = style(base.headlineLarge, FontWeight.SemiBold, 0.15f),
        headlineMedium = style(base.headlineMedium, FontWeight.SemiBold, 0.15f),
        headlineSmall = style(base.headlineSmall, FontWeight.SemiBold, 0.15f),
        titleLarge = style(base.titleLarge, FontWeight.SemiBold, 0.1f),
        titleMedium = style(base.titleMedium, FontWeight.Medium, 0.1f),
        titleSmall = style(base.titleSmall, FontWeight.Medium, 0.1f),
        bodyLarge = style(base.bodyLarge),
        bodyMedium = style(base.bodyMedium),
        bodySmall = style(base.bodySmall),
        labelLarge = style(base.labelLarge, FontWeight.Medium),
        labelMedium = style(base.labelMedium, FontWeight.Medium),
        labelSmall = style(base.labelSmall, FontWeight.Medium),
    )
}

fun buildExpressiveTypography(useFlipperFont: Boolean, fontScale: Float): Typography {
    val base = buildAppTypography(useFlipperFont, fontScale)
    return base.copy(
        displayLarge = base.displayLarge.copy(fontSize = base.displayLarge.fontSize * 1.12f),
        displayMedium = base.displayMedium.copy(fontSize = base.displayMedium.fontSize * 1.1f),
        displaySmall = base.displaySmall.copy(fontSize = base.displaySmall.fontSize * 1.08f),
        headlineLarge = base.headlineLarge.copy(fontSize = base.headlineLarge.fontSize * 1.08f),
        headlineMedium = base.headlineMedium.copy(fontSize = base.headlineMedium.fontSize * 1.06f),
        headlineSmall = base.headlineSmall.copy(fontSize = base.headlineSmall.fontSize * 1.04f),
        titleLarge = base.titleLarge.copy(fontSize = base.titleLarge.fontSize * 1.05f),
        titleMedium = base.titleMedium.copy(fontSize = base.titleMedium.fontSize * 1.04f),
        titleSmall = base.titleSmall.copy(fontSize = base.titleSmall.fontSize * 1.02f),
    )
}

val Typography = buildAppTypography(useFlipperFont = false, fontScale = 1f)
