package com.droid.dolphy

import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Nfc
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.droid.dolphy.hid.HidKeyboardActivity
import com.droid.dolphy.plugin.PluginIcons
import com.droid.dolphy.plugin.PluginRegistry
import com.droid.dolphy.plugin.store.PluginTranslator
import com.droid.dolphy.plugin.model.OtherSections

data class FunctionDestination(
    val icon: ImageVector,
    val title: String,
    val description: String,
    val route: String,
    val section: String,
    val requiresRoot: Boolean = false,
    val isPlugin: Boolean = false,
    val pluginId: String = "",
    val screenId: String = "",
)

@Composable
fun functionDestinationSections(): List<Pair<String, List<FunctionDestination>>> {
    val revision by PluginRegistry.revision.collectAsState()
    val pluginCards by PluginRegistry.otherCards.collectAsState()
    val context = LocalContext.current
    var translatedPluginCards by remember { mutableStateOf<Map<String, Pair<String, String>>>(emptyMap()) }

    LaunchedEffect(revision, pluginCards) {
        PluginTranslator.initialize(context)
        val translated = linkedMapOf<String, Pair<String, String>>()
        pluginCards.forEach { card ->
            fun hasArabic(value: String): Boolean = value.any { it in '\u0600'..'\u06FF' }
            fun needsTranslation(value: String): Boolean = value.any { it in 'A'..'Z' || it in 'a'..'z' }
            val translatedTitle = if (needsTranslation(card.title)) PluginTranslator.translate(card.title) else card.title
            val translatedDescription = if (needsTranslation(card.description)) PluginTranslator.translate(card.description) else card.description
            val title = translatedTitle?.takeIf { hasArabic(it) }
                ?: card.title.takeIf { hasArabic(it) }
                ?: "إضافة"
            val description = translatedDescription?.takeIf { hasArabic(it) }
                ?: card.description.takeIf { hasArabic(it) }
                ?: "وصف الإضافة غير متوفر بالعربية حالياً."
            val names = listOf(card.title, translatedTitle.orEmpty(), title).distinct().filter { it.isNotBlank() }
            val cleanDescription = names.fold(description.trim()) { value, name ->
                value.replaceFirst(
                    Regex("^\\\\s*${Regex.escape(name)}\\\\s*[:：–—-]\\\\s*", RegexOption.IGNORE_CASE),
                    "",
                )
            }.trim().ifBlank { description.trim() }
            translated["${card.pluginId}/${card.screenId}/${card.title}"] = title to cleanDescription
        }
        translatedPluginCards = translated
    }

    val bySection = androidx.compose.runtime.remember(revision, pluginCards) { PluginRegistry.otherBySection() }

    val irFlipperTitle = stringResource(R.string.ir_flipper_remotes)
    val irFlipperDesc = stringResource(R.string.other_ir_flipper_desc)
    val irStormTitle = stringResource(R.string.ir_storm)
    val irStormDesc = stringResource(R.string.other_ir_storm_desc)
    val irJammerTitle = stringResource(R.string.ir_jammer)
    val irJammerDesc = stringResource(R.string.other_ir_jammer_desc)
    val irUnivTitle = stringResource(R.string.ir_universal_remotes)
    val irUnivDesc = stringResource(R.string.other_ir_universal_desc)

    val audioScanTitle = stringResource(R.string.audio_scanner_title)
    val audioScanDesc = stringResource(R.string.audio_scanner_desc)
    val scooterTitle = stringResource(R.string.scooter_hack_title)
    val scooterDesc = stringResource(R.string.scooter_hack_card_desc)
    val nrfDesc = stringResource(R.string.nrf_scanner_description)
    val chatDesc = stringResource(R.string.other_dolphy_chat_desc)
    val hidTitle = stringResource(R.string.other_hid)
    val hidDesc = stringResource(R.string.other_hid_desc)

    val nfcTitle = stringResource(R.string.other_nfc)
    val nfcDesc = stringResource(R.string.other_nfc_desc)
    val qrTitle = stringResource(R.string.other_qr_tools)
    val qrDesc = stringResource(R.string.other_qr_tools_desc)
    val netHubDesc = stringResource(R.string.network_hub_card_description)
    val tvCastTitle = stringResource(R.string.smarttv_cast_title)
    val tvCastDesc = stringResource(R.string.smarttv_cast_card_description)
    val lanTitle = stringResource(R.string.lan_tools_title)
    val lanDesc = stringResource(R.string.lan_tools_subtitle)

    return run {
        fun plugins(section: String): List<FunctionDestination> {
            return bySection[section].orEmpty().map { card ->
                val translated = translatedPluginCards["${card.pluginId}/${card.screenId}/${card.title}"]
                FunctionDestination(
                    icon = PluginIcons.resolve(card.icon),
                    title = translated?.first ?: card.title.takeIf { it.any { ch -> ch in '\u0600'..'\u06FF' } } ?: "إضافة",
                    description = translated?.second ?: card.description.takeIf { it.any { ch -> ch in '\u0600'..'\u06FF' } } ?: "وصف الإضافة غير متوفر بالعربية حالياً.",
                    route = "plugin/${card.pluginId}/${card.screenId}",
                    section = section,
                    isPlugin = true,
                    pluginId = card.pluginId,
                    screenId = card.screenId,
                )
            }
        }

        val sections = mutableListOf<Pair<String, List<FunctionDestination>>>()
        sections += OtherSections.INFRARED to listOf(
            FunctionDestination(Icons.Default.Computer, irFlipperTitle, irFlipperDesc, "other/ir_flipper_home", OtherSections.INFRARED),
            FunctionDestination(Icons.Default.Warning, irStormTitle, irStormDesc, "other/ir_storm", OtherSections.INFRARED),
            FunctionDestination(Icons.Default.WifiTethering, irJammerTitle, irJammerDesc, "other/ir_jammer", OtherSections.INFRARED),
            FunctionDestination(Icons.Default.Tv, irUnivTitle, irUnivDesc, "other/universal_remotes_home", OtherSections.INFRARED),
        ) + plugins(OtherSections.INFRARED)
        val compactStyle = LocalContext.current
            .getSharedPreferences("DolphyPrefs", android.content.Context.MODE_PRIVATE)
            .getInt("app_style_mode", 0) == 1
        val bleSpamCards = if (compactStyle) listOf(
            FunctionDestination(Icons.Default.Bolt, stringResource(R.string.catalog_ble_title), stringResource(R.string.catalog_ble_desc), "ble_spam_screen", OtherSections.BLUETOOTH),
            FunctionDestination(Icons.Default.Link, stringResource(R.string.catalog_bond_title), stringResource(R.string.catalog_bond_desc), "bond_spam_screen", OtherSections.BLUETOOTH),
        ) else emptyList()
        sections += OtherSections.BLUETOOTH to (bleSpamCards + listOf(
            FunctionDestination(Icons.Default.BluetoothAudio, audioScanTitle, audioScanDesc, "other/audio_scanner", OtherSections.BLUETOOTH),
            FunctionDestination(Icons.Default.ElectricScooter, scooterTitle, scooterDesc, "other/scooter_hack", OtherSections.BLUETOOTH),
            FunctionDestination(Icons.Default.Bluetooth, stringResource(R.string.catalog_nrf_title), nrfDesc, "other/nrf_scanner", OtherSections.BLUETOOTH),
            FunctionDestination(Icons.Default.Chat, stringResource(R.string.catalog_chat_title), chatDesc, "other/dolphy_chat_global", OtherSections.BLUETOOTH),
            FunctionDestination(Icons.Default.Keyboard, hidTitle, hidDesc, "hid", OtherSections.BLUETOOTH),
            FunctionDestination(Icons.Default.BluetoothDisabled, stringResource(R.string.catalog_bluetooth_test_title), stringResource(R.string.catalog_bluetooth_test_desc), "other/bluetooth_jammer", OtherSections.BLUETOOTH),
        ) + plugins(OtherSections.BLUETOOTH))
        sections += OtherSections.OTHER to listOf(
            FunctionDestination(Icons.Outlined.Nfc, nfcTitle, nfcDesc, "other/nfc_tools", OtherSections.OTHER),
            FunctionDestination(Icons.Default.QrCodeScanner, qrTitle, qrDesc, "other/qr_tools", OtherSections.OTHER),
            FunctionDestination(Icons.Filled.WifiOff, stringResource(R.string.catalog_network_title), netHubDesc, "other/network_diagnostic_hub", OtherSections.OTHER),
            FunctionDestination(Icons.Default.Cast, tvCastTitle, tvCastDesc, "other/smarttv_cast", OtherSections.OTHER),
            FunctionDestination(Icons.Default.Router, lanTitle, lanDesc, "other/lan_scanner", OtherSections.OTHER),
        ) + plugins(OtherSections.OTHER)

        val pluginSection = plugins(OtherSections.PLUGINS)
        if (pluginSection.isNotEmpty()) sections += OtherSections.PLUGINS to pluginSection
        bySection.keys.filter { !OtherSections.isBuiltin(it) }.sorted().forEach { section ->
            sections += section to plugins(section)
        }
        sections.filter { it.second.isNotEmpty() }
    }
}

fun openFunctionDestination(destination: FunctionDestination, navController: NavController, context: android.content.Context) {
    if (destination.route == "hid") {
        context.startActivity(Intent(context, HidKeyboardActivity::class.java))
    } else {
        navController.navigate(destination.route)
    }
}
